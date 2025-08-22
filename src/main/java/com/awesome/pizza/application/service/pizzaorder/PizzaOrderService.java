package com.awesome.pizza.application.service.pizzaorder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.awesome.pizza.application.configuration.CacheConfig;
import com.awesome.pizza.application.data.abstraction.APModelService;
import com.awesome.pizza.application.data.collection.SetBlockingQueue;
import com.awesome.pizza.application.data.exception.APApplicationException;
import com.awesome.pizza.application.data.exception.APException;
import com.awesome.pizza.application.data.exception.APNotFoundException;
import com.awesome.pizza.application.data.exception.APTooManyRequestException;
import com.awesome.pizza.application.model.PizzaOrder;
import com.awesome.pizza.application.model.PizzaOrderStatus;
import com.awesome.pizza.application.repository.PizzaOrderRepository;
import com.awesome.pizza.application.repository.PizzaOrderStatusRepository;

import jakarta.annotation.PostConstruct;

@Service
@Transactional(rollbackFor = Throwable.class)
public class PizzaOrderService extends APModelService<PizzaOrder, PizzaOrderRepository> {

	private final SetBlockingQueue<Long> queue;
	private final PizzaOrderStatusRepository statusDao;
	/**
	 * Update timestamp of the last order which was skipped during the auto-filling
	 * of the queue, due to the queue reaching full capacity.
	 */
	private final AtomicReference<LocalDateTime> lastSkippedTst;

	public PizzaOrderService(PizzaOrderRepository dao, PizzaOrderStatusRepository statusDao,
			@Qualifier(CacheConfig.PIZZA_ORDER_CACHE) SetBlockingQueue<Long> queue) {
		super(dao);
		this.statusDao = statusDao;
		this.queue = queue;
		this.lastSkippedTst = new AtomicReference<>(null);
	} // PizzaOrderService

	@PostConstruct
	void init() {
		// At startup it checkes the databases for new orders, eventually filling the
		// queue
		recomputeOrderQueue();
	} // init

	@Override
	protected void beforeInsert(PizzaOrder model) throws APException {
		// Assigning a random code
		model.setCode(UUID.randomUUID().toString());
		// Setting the status to NEW
		model.setStatusId(PizzaOrderStatus.NEW);
	} // beforeInsert

	/**
	 * Cache the order ID in the queue
	 * 
	 * @throws APTooManyRequestException If the queue is full
	 * @throws APException               If there is a problem while inserting into
	 *                                   the queue
	 */
	@Override
	protected void afterInsert(PizzaOrder model) throws APException {
		// Checking for orders that were skipped (this should never happen)
		checkForSkippedOrdersAndFill(model);
		try {
			// It tries to put the order in the queue. If it's full or there's a problem, an
			// exception is thrown, triggering a rollback
			if (!queue.offer(model.getId(), 5000, TimeUnit.MILLISECONDS)) {
				throw new APTooManyRequestException();
			}
			// Two different catch clauses in order to distinguish between a full queue or
			// an error from another source
		} catch (APTooManyRequestException e) {
			throw e;
		} catch (Exception e) {
			throw new APException(e);
		}
	} // afterInsert

	/* ***************************************************************************/
	/* * SPEC ********************************************************************/
	/* ***************************************************************************/

	/**
	 * Retrieves the order corresponding to the input code and returns the
	 * associated status entity.
	 */
	public PizzaOrderStatus getOrderStatusByCode(String code) throws APException {
		PizzaOrder model = dao().findByCode(code).orElseThrow(APNotFoundException::new);
		Long statusId = model.getStatusId();
		return statusDao.findById(statusId).orElseThrow(APNotFoundException::new);
	} // checkOrderStatusByCode

	/**
	 * Returns the list of orders in the cache, paginated according to the input
	 * {@link Pageable} object.
	 */
	public Page<PizzaOrder> getNewOrders(Pageable pageable) throws APException {
		long offset = pageable.getOffset();
		int pageSize = pageable.getPageSize();
		int size = queue.size();
		List<PizzaOrder> list = queue.stream().skip(offset).limit(pageSize).map(id -> dao().findById(id).orElse(null))
				.toList();
		return new PageImpl<>(list, pageable, size);
	} // getNewOrders

	/**
	 * Retrieves the next new order in the cache, setting its status to
	 * {@link PizzaOrderStatus#IN_PROGRESS}. If the cache is empty it'll return
	 * {@code null}
	 */
	public PizzaOrder takeNextOrder() {
		Long orderId = queue.poll();
		// If the queue was empty, returns null
		if (orderId == null) { return null; }
		PizzaOrder order = dao().findById(orderId).orElseThrow(APNotFoundException::new);
		// If the order in the queue is not new, throws an exception
		if (!ObjectUtils.nullSafeEquals(PizzaOrderStatus.NEW, order.getStatusId())) {
			throw new APApplicationException(PizzaOrderMessage.INVALID_ORDER_STATUS);
		}
		return update (order.setStatusId(PizzaOrderStatus.IN_PROGRESS));
	} // takeNextOrder

	/**
	 * Retrieves the order corresponding to the input code and sets its status to
	 * {@link PizzaOrderStatus#READY}.
	 */
	public PizzaOrder closeOrder(String code) {
		PizzaOrder model = dao().findByCode(code).orElseThrow(APNotFoundException::new);
		return update(model.setStatusId(PizzaOrderStatus.READY));
	} // closeOrder

	/* ***************************************************************************/
	/* * UTIL ********************************************************************/
	/* ***************************************************************************/

	/**
	 * Method which clears the cache and re-fills it using data from the database.
	 * More specifically all orders with status {@link PizzaOrderStatus#NEW} are
	 * retrieved, ordered by their update-timestamp.
	 */
	private void recomputeOrderQueue() {
		queue.clear();
		List<PizzaOrder> newOrders = dao().findAllByStatusIdOrderByTsuAsc(PizzaOrderStatus.NEW);
		fillOrderQueue(newOrders);
	} // recomputeOrderQueue

	/**
	 * Checks if there are any skipped orders that should be inserted into the queue
	 * before the specified order. If skipped orders are found, they are added to
	 * the queue in the correct order.
	 * 
	 * @param newOrder The new order whose {@link PizzaOrder#tsu} serves as the
	 *                 reference point for skipped orders.
	 */
	private void checkForSkippedOrdersAndFill(PizzaOrder newOrder) {
		LocalDateTime start = getLastSkippedTst();
		// If there were no skipped order, it does nothing
		if (start == null) { return; }
		LocalDateTime end = newOrder.getTsu();
		// Fetches all orders skipped before the input order and after the timestamp of
		// the last skipped order
		List<PizzaOrder> orders = dao()
				.findByStatusIdAndTsuGreaterThanEqualAndTsuBeforeOrderByTsuAsc(PizzaOrderStatus.NEW, start, end);

		fillOrderQueue(orders);
	} // checkForSkippedOrdersAndFill

	/** Tries to fill the queue using the input orders. */
	private void fillOrderQueue(List<PizzaOrder> orders) {
		// Sets the last skipped order timestamp to null
		setLastSkippedTst(null);
		// For every input order, tries to insert it in the queue
		for (PizzaOrder order : orders) {
			// If there's no space in the queue filling is interrupted
			if (!queue.offer(order.getId())) {
				// It caches the timestamp of the first out of capacity order
				setLastSkippedTst(order.getTsu());
				break;
			} // if
		} // for
	} // fillOrderQueue

	private LocalDateTime getLastSkippedTst() {
		return this.lastSkippedTst.get();
	} // getLastSkippedTst

	private void setLastSkippedTst(LocalDateTime tst) {
		this.lastSkippedTst.set(tst);
	} // setLastSkippedTst

} // PizzaOrderService