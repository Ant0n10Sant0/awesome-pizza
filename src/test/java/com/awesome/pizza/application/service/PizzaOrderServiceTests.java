package com.awesome.pizza.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

import com.awesome.pizza.application.APTest;
import com.awesome.pizza.application.configuration.CacheConfig;
import com.awesome.pizza.application.data.collection.SetBlockingQueue;
import com.awesome.pizza.application.data.exception.APApplicationException;
import com.awesome.pizza.application.data.exception.APNotFoundException;
import com.awesome.pizza.application.data.exception.APTooManyRequestException;
import com.awesome.pizza.application.model.PizzaOrder;
import com.awesome.pizza.application.model.PizzaOrderStatus;
import com.awesome.pizza.application.repository.PizzaOrderRepository;
import com.awesome.pizza.application.service.pizzaorder.PizzaOrderMessage;
import com.awesome.pizza.application.service.pizzaorder.PizzaOrderService;

@SpringBootTest(properties = "awesome.pizza.pizza-order-cache.size=2")
@TestMethodOrder(MethodOrderer.MethodName.class)
class PizzaOrderServiceTests extends APTest {

	@Autowired
	private PizzaOrderService service;
	
	@MockitoSpyBean
	private PizzaOrderRepository dao;

	@MockitoSpyBean(CacheConfig.PIZZA_ORDER_CACHE)
	private SetBlockingQueue<Long> queue;

	@BeforeEach
	void setUp() {
		doReturn(List.of()).when(dao).findAllByStatusIdOrderByTsuAsc(anyLong());
		doReturn(List.of()).when(dao).findByStatusIdAndTsuGreaterThanEqualAndTsuBeforeOrderByTsuAsc(anyLong(), any(),
				any());
		queue.clear();
	} // setUp

	/* ***************************************************************************/
	/* * [insert] ****************************************************************/
	/* ***************************************************************************/

	@DisplayName("[insert] Valid orders are inserted calling the repository")
	@Transactional
	@Test()
	void insertSucces() {
		PizzaOrder order = new PizzaOrder();
		doAnswer(invocation -> invocation.getArgument(0)).when(dao).save(any());
		service.insert(order);

		verify(dao).save(order);
	} // insertSucces

	/* ***************************************************************************/
	/* * [beforeInsert] **********************************************************/
	/* ***************************************************************************/

	@DisplayName("[beforeInsert] New orders have a non-empty code and status NEW")
	@Transactional
	@Test()
	void beforeInsert_whenOrderSaved_thenStatusNewAndValidCode() {
		PizzaOrder order = new PizzaOrder();
		doAnswer(invocation -> invocation.getArgument(0)).when(dao).save(any());
		order = service.insert(order);

		assertEquals(PizzaOrderStatus.NEW, order.getStatusId());
		assertNotNull(order.getCode());
		assertFalse(order.getCode().isEmpty());
	} // beforeInsert_whenOrderSaved_thenStatusNewAndValidCode

	/* ***************************************************************************/
	/* * [afterInsert] ***********************************************************/
	/* ***************************************************************************/

	@DisplayName("[afterInsert] New orders are inserted in the queue")
	@Transactional
	@Test()
	void afterInsert_whenOrderSaved_thenInsertedInQueue() {
		PizzaOrder order = new PizzaOrder().setId(1L);
		doAnswer(invocation -> invocation.getArgument(0)).when(dao).save(any());
		order = service.insert(order);

		assertEquals(1, queue.size());
		assertEquals(order.getId(), queue.peek());
	} // afterInsert_whenOrderSaved_thenInsertedInQueue

	@DisplayName("[afterInsert] New orders are inserted in the queue respecting the order of insertion")
	@Transactional
	@Test()
	void afterInsert_whenOrdersSaved_thenQueueOrderRespected() {
		PizzaOrder order1 = new PizzaOrder().setId(1L);
		PizzaOrder order2 = new PizzaOrder().setId(2L);
		
		doAnswer(invocation -> invocation.getArgument(0)).when(dao).save(any());
		order1 = service.insert(order1);
		doAnswer(invocation -> invocation.getArgument(0)).when(dao).save(any());
		order2 = service.insert(order2);

		assertEquals(2, queue.size());
		assertEquals(order1.getId(), queue.poll());
		assertEquals(order2.getId(), queue.poll());
	} // afterInsert_whenOrdersSaved_thenQueueOrderRespected

	@DisplayName("[afterInsert] When Queue is full, then at insertion of an order an exception is raised")
	@Transactional
	@Test()
	void afterInsert_whenQueueFull_thenThrows() {
		PizzaOrder order1 = new PizzaOrder().setId(1L);
		PizzaOrder order2 = new PizzaOrder().setId(2L);
		PizzaOrder order3 = new PizzaOrder().setId(3L);
		
		doAnswer(invocation -> invocation.getArgument(0)).when(dao).save(any());
		service.insert(order1);
		doAnswer(invocation -> invocation.getArgument(0)).when(dao).save(any());
		service.insert(order2);

		doAnswer(invocation -> invocation.getArgument(0)).when(dao).save(any());
		assertAPException(APTooManyRequestException.class, () -> service.insert(order3));
	} // afterInsert_whenQueueFull_thenThrows

	/* ***************************************************************************/
	/* * [takeNextOrder] *********************************************************/
	/* ***************************************************************************/

	@DisplayName("[takeNextOrder] Taking the next order retrieves the order in the queue, setting the status to IN_PROGRESS")
	@Transactional
	@Test()
	void takeNextOrder_whenQueueNotEmpty_thenRetrievesCorrectly() {
		PizzaOrder inserted = new PizzaOrder().setId(1L).setStatusId(PizzaOrderStatus.NEW);
		queue.offer(inserted.getId());
		doReturn(Optional.of(inserted)).when(dao).findById(anyLong());
		doAnswer(invocation -> invocation.getArgument(0)).when(dao).save(any());

		PizzaOrder taken = service.takeNextOrder();

		verify(dao).findById(inserted.getId());
		verify(dao).save(taken);

		assertEquals(inserted.getId(), taken.getId());
		assertEquals(PizzaOrderStatus.IN_PROGRESS, taken.getStatusId());
	} // takeNextOrder_whenQueueNotEmpty_thenRetrievesCorrectly

	@DisplayName("[takeNextOrder] Orders are taken following the order of insertion")
	@Transactional
	@Test()
	void takeNextOrder_whenMultipleOrders_thenRetrievesCorrectOrder() {
		PizzaOrder order1 = new PizzaOrder().setId(1L).setStatusId(PizzaOrderStatus.NEW);
		PizzaOrder order2 = new PizzaOrder().setId(2L).setStatusId(PizzaOrderStatus.NEW);
		queue.offer(order1.getId());
		queue.offer(order2.getId());

		doReturn(Optional.of(order1)).when(dao).findById(anyLong());
		doAnswer(invocation -> invocation.getArgument(0)).when(dao).save(any());
		PizzaOrder taken1 = service.takeNextOrder();

		doReturn(Optional.of(order2)).when(dao).findById(anyLong());
		doAnswer(invocation -> invocation.getArgument(0)).when(dao).save(any());
		PizzaOrder taken2 = service.takeNextOrder();

		assertEquals(order1.getId(), taken1.getId());
		assertEquals(order2.getId(), taken2.getId());
	} // takeNextOrder_whenMultipleOrders_thenRetrievesCorrectOrder

	@DisplayName("[takeNextOrder] If the retrieved order id is not valid, then throws an exception")
	@Transactional
	@Test()
	void takeNextOrder_whenOrderIdNotValid_thenThrows() {
		PizzaOrder order = new PizzaOrder().setId(-1L);
		queue.offer(order.getId());

		assertAPException(APNotFoundException.class, () -> service.takeNextOrder());
	} // takeNextOrder_whenOrderNotNew_thenThrows

	@DisplayName("[takeNextOrder] If the retrieved order is not NEW, then throws an exception")
	@Transactional
	@Test()
	void takeNextOrder_whenOrderNotNew_thenThrows() {
		PizzaOrder order = new PizzaOrder().setId(1L);
		queue.offer(order.getId());

		doReturn(Optional.of(order)).when(dao).findById(anyLong());

		assertAPException(
				APApplicationException.class,
				() -> service.takeNextOrder(),
				PizzaOrderMessage.INVALID_ORDER_STATUS.getMsg());
	} // takeNextOrder_whenOrderNotNew_thenThrows
	
	@DisplayName("[takeNextOrder] If the queue is empty, it returns null")
	@Transactional
	@Test()
	void takeNextOrder_whenQueueEmpty_thenReturnsNull() {
		PizzaOrder taken = service.takeNextOrder();
		assertNull(taken);
	} // takeNextOrder_whenQueueEmpty_thenReturnsNull

} // PizzaOrderServiceTest
