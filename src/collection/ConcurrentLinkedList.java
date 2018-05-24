package collection;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * this class is a linked list with a blocking structure for handling concurrent operations.
 * 
 * Data structure functionalities are obtained by delegation to a {@code java.util.LinkedList object}.
 * The blocking structure used is {@code java.util.concurrent.locks.ReentrantReadWriteLock}
 * 
 * @author hugo
 * @see java.util.LinkedList
 * @see java.util.concurrent.locks.ReentrantReadWriteLock
 */
public class ConcurrentLinkedList {
	
	/**
	 * The encapsuled linked list
	 */
	private LinkedList<Integer> linkedList;
	
	/**
	 * The main blocking structure, it has a special lock to treat differentily readers and writers
	 */
	private ReentrantReadWriteLock rwl;
	
	/**
	 * The guard condition
	 */
	private boolean writing;
	
	/**
	 * {@code Condition} that controlls the blocked writers (insert or remove) in a intermediate block
	 */
	private Condition writersCondition;

	/**
	 * Contruct a empty list with a read-write explicit lock
	 */
	public ConcurrentLinkedList() {
		linkedList = new LinkedList<>();
		rwl = new ReentrantReadWriteLock(true);
		writing = false;
		writersCondition = rwl.writeLock().newCondition();
	}
	
	/**
	 * Realizes the search on the list. The blocking structures permits that search operations ocurs 
	 * parallelly with others searches and insertions
	 * 
	 * @param threadName
	 *   Thread Name to print on the standart output
	 * @param value
	 *   Searched integer value
	 */
	public void search(String threadName, Integer value) {
		
		int index;
		
		System.out.println("[" + threadName + "](param: "+ value + ") - Trying aquire the lock");
		rwl.readLock().lock();
		try {
			System.out.println("[" + threadName + "](param: "+ value + ") - Starting the search operation");
			
			index = linkedList.indexOf(value);
			
			if (index > -1) {
				System.out.println("[" + threadName + "](param: "+ value + ") - found, index: " + index + ".");
			}
			else {
				System.out.println("[" + threadName + "](param: "+ value + ") - not found.");
			}
		} finally {
			rwl.readLock().unlock();
		}
	}

	/**
	 * Inserts the given value in the last position in the linked list. This operation blocks the 
	 * simultaneous access of other write operations, like removals and even other inserts. Acquires
	 * writeLock and sets the guard condition to signal writing, locks the lock, inserts, and finally
	 * gets writeLock again to clear the guard condition.
	 * 
	 * @param threadName
	 *   Thread Name to print on the standart output
	 * @param value
	 *   Value to insert
	 */
	public void insert(String threadName, Integer value) {
		
		System.out.println("[" + threadName + "](param: "+ value + ") - Trying aquire the lock");
		rwl.writeLock().lock();
		try {
			// Guard condition
			while (writing) {
				System.out.println("[" + threadName + "](param: "+ value + ") - Another thread is writing, await...");
				writersCondition.await();
			}
			
			/*
			 * This surrounding lock is only to set the shared boolean guard condition, the insertion goes release the
			 * lock and permit readers operate normally 
			 */
			
			writing = true;
			System.out.println("[" + threadName + "](param: "+ value + ") - Starting the insert operation");
			
		} catch (InterruptedException e) {
			System.err.println("Failure in execute the insert operation, generated Exception:");
			e.printStackTrace();
			
		} finally {
			rwl.writeLock().unlock();
		}
		
		// Now, with outher writers locked in the guard codition variable, execute the insertion operation
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		linkedList.addLast(value);
		System.out.println("[" + threadName + "](param: "+ value + ") - Value inserted.");
		
		// Aquiring access and free the guard condition
		rwl.writeLock().lock();
		try {
			writing = false;
			writersCondition.signal(); // Awake one awaiting writer
		} finally {
			rwl.writeLock().unlock();
		}
	}
	
	/**
	 * Removes the given value, if it's already in the list. This blocks all the other simultaneous
	 * operations using the writeLock and the guard condition.
	 * 
	 * @param threadName
	 *   Thread Name to print on the standart output
	 * @param value
	 *   Value to attempt removes
	 */
	public void remove(String threadName, Integer value) {
		
		boolean sucess;
		
		System.out.println("[" + threadName + "](param: "+ value + ") - Trying aquire the lock");
		rwl.writeLock().lock();
		
		try {
			// Guard condition
			while (writing) {
				System.out.println("[" + threadName + "](param: "+ value + ") - Another thread is writing, await...");
				writersCondition.await();
			}
			
			// The remove, that is a writing operation, also sets the guard condition
			writing = true;
			System.out.println("[" + threadName + "](param: "+ value + ") - Starting the remove operation");
		
			// Now, the remove operation...
			Thread.sleep(50);
			sucess = linkedList.remove(value);
			
			if (sucess) {
				System.out.println("[" + threadName + "](param: "+ value + ") - Value removed.");
			}
			else {
				System.out.println("[" + threadName + "](param: "+ value + ") - Value not removed (probably it wasn't in the list)");
			}
			
			writing = false;
			writersCondition.signal(); // Awake one awaiting writer
			
		} catch (InterruptedException e) {
			System.err.println("Failure in execute the remove operation, generated Exception:");
			e.printStackTrace();
			
		} finally {
			rwl.writeLock().unlock();
		}
	}
}
