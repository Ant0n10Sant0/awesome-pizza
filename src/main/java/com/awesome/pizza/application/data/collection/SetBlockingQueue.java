package com.awesome.pizza.application.data.collection;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * A {@link BlockingQueue} that additionally enforces uniqueness of its
 * elements.
 */
public class SetBlockingQueue<T> {
	private final BlockingQueue<T> queue;
	private final Set<T> inserted = ConcurrentHashMap.newKeySet();

	public SetBlockingQueue(BlockingQueue<T> queue) {
		this.queue = queue;
	} // SetBlockingQueue

	/**
	 * Inserts immediately the specified element into the queue if it is not already
	 * present in the queue and if it is possible to do so without violating
	 * capacity restrictions.
	 * 
	 * @param e the element to add
	 * @return {@code true} if the element was added to the queue, else
	 *         {@code false}
	 * @throws ClassCastException       if the class of the specified element
	 *                                  prevents it from being added to the queue
	 * @throws NullPointerException     if the specified element is null
	 * @throws IllegalArgumentException if some property of the specified element
	 *                                  prevents it from being added to the queue
	 */
	public boolean offer(T e) {
		// Checks if the element is already present in the set
		if (inserted.add(e)) {
			// Tries to offer it, in case of failure removes the element from the set
			if (!queue.offer(e)) {
				inserted.remove(e);
				return false;
			}
			return true;
		}
		return false;
	} // offer

	/**
	 * Inserts the specified element into this queue, if it's not already present,
	 * waiting up to the specified wait time if necessary for space to become
	 * available.
	 * 
	 * @param e       the element to add
	 * @param timeout how long to wait before giving up, in units of {@code unit}
	 * @param unit    a {@code TimeUnit} determining how to interpret the
	 *                {@code timeout} parameter
	 * @return {@code true} if successful, or {@code false} if the specified waiting
	 *         time elapses before space is available
	 * @throws InterruptedException     if interrupted while waiting
	 * @throws ClassCastException       if the class of the specified element
	 *                                  prevents it from being added to the queue
	 * @throws NullPointerException     if the specified element is null
	 * @throws IllegalArgumentException if some property of the specified element
	 *                                  prevents it from being added to the queue
	 */
	public boolean offer(T e, long timeout, TimeUnit unit) throws InterruptedException {
		// Checks if the element is already present in the set
		if (inserted.add(e)) {
			try {
				// Tries to offer it, in case of failure removes the element from the set
				if (!queue.offer(e, timeout, unit)) {
					inserted.remove(e);
					return false;
				}
				return true;
			} catch (Exception exc) {
				// In case of exception removes the element and rethrows the exception
				inserted.remove(e);
				throw exc;
			}
		}
		return false;
	} // offer
	
	/**
	 * Retrieves and removes the head of this queue, or returns {@code null} if this
	 * queue is empty.
	 */
	public T poll () {
		T el = queue.poll();
		if (el == null) { return null; }
		inserted.remove(el);
		return el;
	} // poll
	
	/**
	 * Retrieves, but does not remove, the head of this queue, or returns
	 * {@code null} if this queue is empty.
	 */
	public T peek () { return queue.peek(); }

	/**
	 * Returns {@code true} if the queue contains the specified element. Uses the
	 * underlying {@link KeySetView} to perform the check.
	 * 
	 * @param o element whose presence in the queue is to be tested
	 * @return {@code true} if the queue contains the specified element
	 */
	public boolean contains(T e) {
		return inserted.contains(e);
	} // contains

	/** Invokes {@link BlockingQueue#stream()} */
	public Stream<T> stream() { return queue.stream(); }

	/** Clears the queue and the set. */
	public void clear() {
		// I'm clearing first the queue because otherwise a thread that was able to
		// insert an element between the clearing of the set and the clearing of the
		// queue, would leave the collection in an incoherent state
		queue.clear();
		inserted.clear();
	} // clear
	
	/** Invokes {@link BlockingQueue#size()} */
	public int size () { return queue.size(); }

} // SetBlockingQueue
