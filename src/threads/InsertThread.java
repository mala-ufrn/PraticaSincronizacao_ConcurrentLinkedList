package threads;

import collection.ConcurrentLinkedList;

/**
 * this class is a simple thread to test concurrent insertions on the {@code ConcurrentLinkedList}. 
 * 
 * @author hugo
 * @see ConcurrentLinkedList
 */
public class InsertThread extends Thread {

	/**
	 * Thread name
	 */
	String name;
	
	/**
	 * Shared concurrent linked list
	 */
	ConcurrentLinkedList concurrentLinkedList;
	
	/**
	 * Value to insert
	 */
	Integer intentedValue;

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
	 *   value to insert
	 */
	public InsertThread(ConcurrentLinkedList concurrentLinkedList, Integer threadNumber, String format, Integer intentedValue) {
		this.concurrentLinkedList = concurrentLinkedList;
		this.intentedValue = intentedValue;
		this.name = "I" + String.format(format, threadNumber);
	}

	/* (non-Javadoc)
	 * Attempt to insert a value on the shared list
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		concurrentLinkedList.insert(name, intentedValue);
	}
}
