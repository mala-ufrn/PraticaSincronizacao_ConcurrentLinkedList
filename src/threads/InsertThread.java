package threads;

import collection.ConcurrentLinkedList;

/**
 * @author hugo
 *
 */
public class InsertThread extends Thread {

	ConcurrentLinkedList concurrentLinkedList;
	Integer intentedValue;

	public InsertThread(ConcurrentLinkedList concurrentLinkedList, Integer intentedValue) {
		this.concurrentLinkedList = concurrentLinkedList;
		this.intentedValue = intentedValue;
	}

	@Override
	public void run() {
		concurrentLinkedList.insert(intentedValue);
	}
}
