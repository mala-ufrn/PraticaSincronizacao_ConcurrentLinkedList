package threads;

import collection.ConcurrentLinkedList;

/**
 * this class is a simple thread to test concurrent searches on the {@code ConcurrentLinkedList}. 
 * 
 * @author hugo
 * @see ConcurrentLinkedList
 */
public class SearchThread extends Thread {

	/**
	 * Thread name
	 */
	String name;
	
	/**
	 * Shared concurrent linked list
	 */
	ConcurrentLinkedList concurrentLinkedList;
	
	/**
	 * Value to search
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
	 *   value to search
	 */
	public SearchThread(ConcurrentLinkedList concurrentLinkedList, Integer threadNumber, String format, Integer targetValue) {
		this.concurrentLinkedList = concurrentLinkedList;
		this.targetValue = targetValue;
		this.name = "B" + String.format(format, threadNumber);
	}

	/* (non-Javadoc)
	 * Attempt to search a value on the shared list
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		concurrentLinkedList.search(name, targetValue);
	}
}
