package com.awesome.pizza.application.service.pizzaorder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.awesome.pizza.application.configuration.CacheConfig;
import com.awesome.pizza.application.data.abstraction.APModelService;
import com.awesome.pizza.application.data.exception.APException;
import com.awesome.pizza.application.data.exception.APNotFoundException;
import com.awesome.pizza.application.data.exception.APTooManyRequestException;
import com.awesome.pizza.application.model.PizzaOrder;
import com.awesome.pizza.application.model.PizzaOrderStatus;
import com.awesome.pizza.application.repository.PizzaOrderRepository;
import com.awesome.pizza.application.repository.PizzaOrderStatusRepository;
import com.awesome.pizza.application.util.DateTimeUtils;

@Service
@Transactional(rollbackFor = Throwable.class)
public class PizzaOrderService extends APModelService<PizzaOrder, PizzaOrderRepository> {

	private final BlockingQueue<Long> queue;
	private final PizzaOrderStatusRepository statusDao;
	private final AtomicLong lastTst;

	public PizzaOrderService(PizzaOrderRepository dao, PizzaOrderStatusRepository statusDao,
			@Qualifier(CacheConfig.PIZZA_ORDER_CACHE) BlockingQueue<Long> queue) {
		super(dao);
		this.statusDao = statusDao;
		this.queue = queue;
		this.lastTst = new AtomicLong(0);

		recomputeOrderQueue();
	} // PizzaOrderService

	@Override
	protected void beforeInsert(PizzaOrder model) throws APException {
		// Assigning a random code
		model.setCode(UUID.randomUUID().toString());
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
		checkForSkippedOrders();
		try {
			if (!queue.offer(model.getId(), 500, TimeUnit.MILLISECONDS)) {
				throw new APTooManyRequestException();
			}
			// If succesful, update the last timestamp
			setLastTst(model.getTsu());
		} catch (APTooManyRequestException e) {
			throw e;
		} catch (Exception e) {
			throw new APException(e);
		}
	} // afterInsert

	/* ***************************************************************************/
	/* * SPEC ********************************************************************/
	/* ***************************************************************************/

	public PizzaOrderStatus getOrderStatusByCode(String code) throws APException {
		PizzaOrder model = dao().findByCode(code).orElseThrow(APNotFoundException::new);
		Long statusId = model.getStatusId();
		return statusDao.findById(statusId).orElseThrow(APNotFoundException::new);
	} // checkOrderStatusByCode

	public List<PizzaOrder> getNewOrders() throws APException {
		return queue.stream().limit(10L).map(id -> dao().findById(id).orElse(null)).toList();
	} // getNewOrders

	public PizzaOrder takeOrder() {
		Long orderId = queue.poll();
		if (orderId == null) { return null; }
		PizzaOrder order = dao().findById(orderId).orElseThrow(APNotFoundException::new);
		return update (order.setStatusId(PizzaOrderStatus.IN_PROGRESS));
	} // takeOrder

	/* ***************************************************************************/
	/* * UTIL ********************************************************************/
	/* ***************************************************************************/

	private void recomputeOrderQueue() {
		queue.clear();
		List<PizzaOrder> newOrders = dao().findAllByStatusIdOrderByTsuAsc(PizzaOrderStatus.NEW);
		fillOrderQueue(newOrders);
	} // recomputeOrderQueue

	private void checkForSkippedOrders () {
		LocalDateTime tst = getLastTst();
		List<PizzaOrder> orders = dao().findAllByStatusIdAndTsuAfterOrderByTsuAsc(PizzaOrderStatus.NEW, tst);
		fillOrderQueue(orders);
	} // checkForSkippedOrders

	private void fillOrderQueue(List<PizzaOrder> newOrders) {
		for (PizzaOrder order : newOrders) {
			// If there's no space in the queue filling is interrupted
			if (!queue.offer(order.getId())) {
				setLastTst(order.getTsu());
				break;
			} // if
		} // for
	} // fillOrderQueue

	private LocalDateTime getLastTst() {
		return DateTimeUtils.fromEpochMillis(this.lastTst.get());
	} // getLastTst

	private void setLastTst(LocalDateTime tst) {
		this.lastTst.set(DateTimeUtils.toEpochMillis(tst));
	} // setLastTst

} // PizzaOrderService