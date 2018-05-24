package threads;

import collection.ConcurrentLinkedList;

/**
 * this class is a simple thread to test concurrent removes on the {@code ConcurrentLinkedList}. 
 * 
 * @author hugo
 * @see ConcurrentLinkedList
 */
public class RemoveThread extends Thread {

	/**
	 * Thread name
	 */
	String name;
	
	/**
	 * Shared concurrent linked list
	 */
	ConcurrentLinkedList concurrentLinkedList;
	
	/**
	 * Value to remove
	 */
	Integer targetValue;
	
	/**
	 * Constructs a thread instance
	 * 
	 * @param concurrentLinkedList
	 *   Shared concurrent linked list
	 * @param threadNumber
	 *   thread serial number
	 * @param format
	 *   format to serial number (ex.: %03d
	 * @param intentedValue
	 *   value to remove
	 */
	public RemoveThread(ConcurrentLinkedList concurrentLinkedList, Integer threadNumber, String format, Integer targetValue) {
		this.concurrentLinkedList = concurrentLinkedList;
		this.targetValue = targetValue;
		this.name = "R" + String.format(format, threadNumber);
	}

	/* (non-Javadoc)
	 * Attempt to remove a value on the shared list
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		concurrentLinkedList.remove(name, targetValue);
	}
}
