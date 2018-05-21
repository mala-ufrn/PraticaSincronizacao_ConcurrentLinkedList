package collection;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author hugo
 *
 */
public class ConcurrentLinkedList {
	private LinkedList<Integer> linkedList;
	private ReentrantReadWriteLock rwl;
	private boolean writing;
	private Condition writingCondition;

	public ConcurrentLinkedList() {
		linkedList = new LinkedList<>();
		rwl = new ReentrantReadWriteLock(true);
		writing = false;
		writingCondition = rwl.writeLock().newCondition();
	}
	
	public void search(Integer value) {
		
		int index;
		
		System.out.println("Trying aquire the lock to start search operation, target value: ("+ value.intValue() + ")");
		rwl.readLock().lock();
		try {
			System.out.println("Starting the search operation, value: (" + value.intValue() + ")");
			index = linkedList.indexOf(value);
			
			if (index > -1) {
				System.out.println("Value (" + value.intValue() + ") found, index: " + index + ".");
			}
			else {
				System.out.println("Value (" + value.intValue() + ") not found.");
			}
		} finally {
			rwl.readLock().unlock();
		}
	}

	public void insert(Integer value) {
		
		System.out.println("Trying aquire the lock to start insert operation, value: ("+ value.intValue() + ")");
		rwl.writeLock().lock();
		try {
			// Guard condition
			while (writing) {
				System.out.println("Another thread is writing, await... - operation: insert ("+ value.intValue() + ")");
				writingCondition.await();
			}
			
			/*
			 * This surrounding lock is only to set the shared boolean guard condition, the insertion goes release the
			 * lock and permit readers operate normally 
			 */
			
			writing = true;
			
		} catch (InterruptedException e) {
			System.err.println("Failure in execute writingCondition.await(), generated Exception:");
			e.printStackTrace();
			
		} finally {
			rwl.writeLock().unlock();
		}
		
		// Now, with outher writers locked in the guard codition variable, execute the insertion operation
		linkedList.addLast(value);
		System.out.println("Value (" + value.intValue() + ") inserted.");
		
		// Aquiring access and free the guard condition
		rwl.writeLock().lock();
		try {
			writing = false;
			writingCondition.signal(); // Awake one awaiting writer
		} finally {
			rwl.writeLock().unlock();
		}
	}
	
	public void remove(Integer value) {
		
		boolean sucess;
		
		System.out.println("Trying aquire the lock to start remove operation, target value: ("+ value.intValue() + ")");
		rwl.writeLock().lock();
		
		try {
			// Guard condition
			while (writing) {
				System.out.println("Another thread is writing, await... - operation: remove ("+ value.intValue() + ")");
				writingCondition.await();
			}
			
			// The remove, that is a writing operation, also sets the guard condition
			writing = true;
		
			// Now, the remove operation...
			sucess = linkedList.remove(value);
			
			if (sucess) {
				System.out.println("Value (" + value.intValue() + ") removed.");
			}
			else {
				System.out.println("Value (" + value.intValue() + ") not removed (probably it wasn't in the list)");
			}
			
			writing = false;
			writingCondition.signal(); // Awake one awaiting writer
			
		} catch (InterruptedException e) {
			System.err.println("Failure in execute writingCondition.await(), generated Exception:");
			e.printStackTrace();
			
		} finally {
			rwl.writeLock().unlock();
		}
	}
}
