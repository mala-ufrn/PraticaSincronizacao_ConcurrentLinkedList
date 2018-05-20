package threads;

import collection.ConcurrentLinkedList;

/**
 * @author hugo
 *
 */
public class SearchThread extends Thread {

	ConcurrentLinkedList concurrentLinkedList;
	Integer targetValue;

	public SearchThread(ConcurrentLinkedList concurrentLinkedList, Integer targetValue) {
		this.concurrentLinkedList = concurrentLinkedList;
		this.targetValue = targetValue;
	}

	@Override
	public void run() {
		concurrentLinkedList.search(targetValue);
	}
}
