package com.awesome.pizza.application.data.collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.util.Pair;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest()
@TestMethodOrder(MethodOrderer.MethodName.class)
class SetBlockingQueueTests {

	@Autowired
	@Qualifier("testCache")
	private SetBlockingQueue<Long> queue;

	@TestConfiguration
	static class TestConfig {
		@Bean("testCache")
		SetBlockingQueue<Long> testCache() {
			BlockingQueue<Long> queue = new ArrayBlockingQueue<>(2);
			return new SetBlockingQueue<>(queue);
		} // pizzaOrderCache
	} // TestConfig

	@BeforeEach
	void setUp() {
		queue.clear();
	} // setUp

	/* ***************************************************************************/
	/* * [offer] *****************************************************************/
	/* ***************************************************************************/

	@DisplayName("[offer] New items are inserted in the queue")
	@Test()
	void offer_whenNewItem_thenInserted() {
		Long el = 1L;

		boolean answer = queue.offer(el);

		assertTrue(answer);
		assertEquals(1, queue.size());
		assertEquals(el, queue.peek());
	} // offer_whenNewItem_thenInserted

	@DisplayName("[offer] Duplicate items are not inserted in the queue")
	@Test()
	void offer_whenDuplicateItem_thenNotInserted() {
		Long el = 1L;

		boolean firstAnswer = queue.offer(el);
		boolean secondAnswer = queue.offer(el);

		assertTrue(firstAnswer);
		assertFalse(secondAnswer);
		assertEquals(1, queue.size());
		assertEquals(el, queue.peek());
	} // offer_whenDuplicateItem_thenNotInserted

	@DisplayName("[offer] Item are inserted in the queue following the order of insertion")
	@Test()
	void offer_whenItemsInserted_thenOrderRespected() {
		Long el1 = 1L;
		Long el2 = 2L;
		queue.offer(el1);
		queue.offer(el2);

		assertEquals(el1, queue.poll());
		assertEquals(el2, queue.peek());
	} // offer_whenItemsInserted_thenOrderRespected

	@DisplayName("[offer] When the queue is full, items are not inserted in the queue")
	@Test()
	void offer_whenFull_thenNotInserted() {
		Long el1 = 1L;
		Long el2 = 2L;
		Long el3 = 3L;

		boolean firstAnswer = queue.offer(el1);
		boolean secondAnswer = queue.offer(el2);
		boolean thirdAnswer = queue.offer(el3);

		assertTrue(firstAnswer);
		assertTrue(secondAnswer);
		assertFalse(thirdAnswer);
		assertEquals(2, queue.size());
		assertFalse(queue.contains(el3));

	} // offer_whenFull_thenNotInserted

	/* ***************************************************************************/
	/* * [offer(wTimeout)] *******************************************************/
	/* ***************************************************************************/

	@DisplayName("[offer(wTimeout)] New items are inserted in the queue")
	@Test()
	void offerwTimeout_whenNewItem_thenInserted() throws InterruptedException {
		Long el = 1L;

		boolean answer = queue.offer(el, 500, TimeUnit.MILLISECONDS);

		assertTrue(answer);
		assertEquals(1, queue.size());
		assertEquals(el, queue.peek());
	} // offerwTimeout_whenNewItem_thenInserted

	@DisplayName("[offer(wTimeout)] Duplicate items are not inserted in the queue")
	@Test()
	void offerwTimeout_whenDuplicateItem_thenNotInserted() throws InterruptedException {
		Long el = 1L;

		boolean firstAnswer = queue.offer(el, 500, TimeUnit.MILLISECONDS);
		boolean secondAnswer = queue.offer(el, 500, TimeUnit.MILLISECONDS);

		assertTrue(firstAnswer);
		assertFalse(secondAnswer);
		assertEquals(1, queue.size());
		assertEquals(el, queue.peek());
	} // offerwTimeout_whenDuplicateItem_thenNotInserted

	@DisplayName("[offer(wTimeout)] Item are inserted in the queue following the order of insertion")
	@Test()
	void offerwTimeout_whenItemsInserted_thenOrderRespected() throws InterruptedException {
		Long el1 = 1L;
		Long el2 = 2L;
		queue.offer(el1, 500, TimeUnit.MILLISECONDS);
		queue.offer(el2, 500, TimeUnit.MILLISECONDS);

		assertEquals(el1, queue.poll());
		assertEquals(el2, queue.peek());
	} // offerwTimeout_whenItemsInserted_thenOrderRespected

	@DisplayName("[offer(wTimeout)] When the queue is full, items are not inserted in the queue")
	@Test()
	void offerwTimeout_whenFull_thenNotInserted() throws InterruptedException {
		Long el1 = 1L;
		Long el2 = 2L;
		Long el3 = 3L;

		boolean firstAnswer = queue.offer(el1, 500, TimeUnit.MILLISECONDS);
		boolean secondAnswer = queue.offer(el2, 500, TimeUnit.MILLISECONDS);
		boolean thirdAnswer = queue.offer(el3, 500, TimeUnit.MILLISECONDS);

		assertTrue(firstAnswer);
		assertTrue(secondAnswer);
		assertFalse(thirdAnswer);
		assertEquals(2, queue.size());
		assertFalse(queue.contains(el3));
	} // offerwTimeout_whenFull_thenNotInserted

	@DisplayName("[offer(wTimeout)] If while inserting space is freed in the queue, then it inserts the element")
	@Test()
	void offerwTimeout_whenSpaceFreeWhileWaiting_thenInsert() throws InterruptedException, ExecutionException {
		// Filling the queue
		Long el1 = 1L;
		Long el2 = 2L;
		queue.offer(el1);
		queue.offer(el2);

		Long el3 = 3L;
		Pair<Boolean, Long> consumerProducer = testConsumerProducer(
				() -> queue.offer(el3, 500, TimeUnit.MILLISECONDS),
				() -> queue.poll());

		assertTrue(consumerProducer.getFirst());
		assertTrue(queue.contains(el3));
		assertEquals(el1, consumerProducer.getSecond());
	} // offerwTimeout_whenSpaceFreeWhileWaiting_thenInsert

	/* ***************************************************************************/
	/* * [poll] ******************************************************************/
	/* ***************************************************************************/

	@DisplayName("[poll] Polling retrieves the first element in the queue and removes it ")
	@Transactional
	@Test()
	void poll_whenQueueNotEmpty_thenRetrievesCorrectly() {
		Long el = 1L;
		queue.offer(el);
		Long poll = queue.poll();

		assertEquals(el, poll);
		assertEquals(0, queue.size());
		assertFalse(queue.contains(el));
	} // poll_whenQueueNotEmpty_thenRetrievesCorrectly

	@DisplayName("[poll] Elements are removed following the order of insertion")
	@Transactional
	@Test()
	void poll_whenMultipleItems_thenRetrievesCorrectOrder() {
		Long el1 = 1L;
		Long el2 = 2L;
		queue.offer(el1);
		queue.offer(el2);
		Long poll1 = queue.poll();
		Long poll2 = queue.poll();

		assertEquals(el1, poll1);
		assertEquals(el2, poll2);
		assertFalse(queue.contains(el1));
		assertFalse(queue.contains(el2));
	} // poll_whenMultipleItems_thenRetrievesCorrectOrder

	@DisplayName("[poll] If the queue is empty, it returns null")
	@Transactional
	@Test()
	void poll_whenQueueEmpty_thenReturnsNull() {
		Long taken = queue.poll();
		assertNull(taken);
	} // poll_whenQueueEmpty_thenReturnsNull

	/* ***************************************************************************/
	/* * UTIL ********************************************************************/
	/* ***************************************************************************/
	private <P, C> Pair<P, C> testConsumerProducer(Callable<P> producer, Callable<C> consumer)
			throws InterruptedException, ExecutionException {
		ExecutorService exec = Executors.newFixedThreadPool(2);
		CountDownLatch ready = new CountDownLatch(2);
		CountDownLatch start = new CountDownLatch(1);
		CountDownLatch done = new CountDownLatch(2);
		Future<P> prodFuture = exec.submit(() -> {
			try {
				ready.countDown();
				start.await();
				return producer.call();
			} finally {
				done.countDown();
			}
		});
		Future<C> consFuture = exec.submit(() -> {
			try {
				ready.countDown();
				start.await();
				return consumer.call();
			} finally {
				done.countDown();
			}
		});
		ready.await();
		start.countDown();
		done.await();
		P pRes = prodFuture.get();
		C cRes = consFuture.get();
		exec.shutdown();

		return Pair.of(pRes, cRes);
	} // testConsumerProducer

} // SetBlockingQueueTests
