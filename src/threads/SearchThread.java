package threads;

import collection.ConcurrentLinkedList;

/**
 * @author hugo
 *
 */
public class SearchThread extends Thread {

	ConcurrentLinkedList concurrentLinkedList;
	int targetValue;

	public SearchThread(ConcurrentLinkedList concurrentLinkedList, int targetValue) {
		this.concurrentLinkedList = concurrentLinkedList;
		this.targetValue = targetValue;
	}

	@Override
	public void run() {
		concurrentLinkedList.search(targetValue);
	}
}
