package threads;

import collection.ConcurrentLinkedList;

/**
 * @author hugo
 *
 */
public class RemoveThread extends Thread {

	ConcurrentLinkedList concurrentLinkedList;
	Integer targetValue;
	
	public RemoveThread(ConcurrentLinkedList concurrentLinkedList, Integer targetValue) {
		this.concurrentLinkedList = concurrentLinkedList;
		this.targetValue = targetValue;
	}

	@Override
	public void run() {
		concurrentLinkedList.remove(targetValue);
	}
}
