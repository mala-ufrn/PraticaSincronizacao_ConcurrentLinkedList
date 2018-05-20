package threads;

import collection.ConcurrentLinkedList;

/**
 * @author hugo
 *
 */
public class InsertThread extends Thread {

	ConcurrentLinkedList concurrentLinkedList;
	int intentedValue;

	public InsertThread(ConcurrentLinkedList concurrentLinkedList, int intentedValue) {
		this.concurrentLinkedList = concurrentLinkedList;
		this.intentedValue = intentedValue;
	}

	@Override
	public void run() {
		concurrentLinkedList.insert(intentedValue);
	}
}
