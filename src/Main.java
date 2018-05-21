import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import collection.ConcurrentLinkedList;
import threads.InsertThread;
import threads.RemoveThread;
import threads.SearchThread;

/**
 * @author hugo
 *
 */
public class Main {

	public static final int NUM_SEARCHTHREADS = 200;
	public static final int NUM_INSERTTHREADS = 200;
	public static final int NUM_REMOVETHREADS = 100;
	
	public static void main(String[] args) {
		
		ConcurrentLinkedList sharedList = new ConcurrentLinkedList();
		
		List<Thread> threadsB = new ArrayList<>(NUM_SEARCHTHREADS);
		List<Thread> threadsI = new ArrayList<>(NUM_INSERTTHREADS);
		List<Thread> threadsR = new ArrayList<>(NUM_REMOVETHREADS);
		
		Random random = new Random();
		
		// Instantiation of all threads objects
		threadsB = IntStream.range(0, NUM_SEARCHTHREADS)
				.mapToObj(i -> new SearchThread(sharedList, new Integer(random.nextInt(NUM_INSERTTHREADS))))
				.collect(Collectors.toList());
		
		
		threadsI = IntStream.range(0, NUM_INSERTTHREADS)
				.mapToObj(i -> new InsertThread(sharedList, new Integer(i)))
				.collect(Collectors.toList());
		
		threadsR = IntStream.range(0, NUM_REMOVETHREADS)
				.mapToObj(i -> new RemoveThread(sharedList, new Integer(random.nextInt(NUM_INSERTTHREADS))))
				.collect(Collectors.toList());
		
		ThreadsLauncher tlB = new ThreadsLauncher(threadsB);
		ThreadsLauncher tlI = new ThreadsLauncher(threadsI);
		ThreadsLauncher tlR = new ThreadsLauncher(threadsR);
		
		tlB.start();
		tlR.start();
		tlI.start();
		
		try {
			tlB.join();
			tlI.join();
			tlR.join();
		} catch (InterruptedException e) {
			System.err.println("Failure in execute join on a ThreadLauncher, generated Exception:");
			e.printStackTrace();
		}
	}
	
	private static class ThreadsLauncher extends Thread {
		
		List<Thread> threadList;

		public ThreadsLauncher(List<Thread> threadList) {
			super();
			this.threadList = threadList;
		}

		@Override
		public void run() {
			threadList.stream().forEach(t -> t.run());
			
			threadList.stream().forEach(t -> {
				try {
					t.join();
				} catch (InterruptedException e) {
					System.err.println("Failure in execute join on a list internal Thread, generated Exception:");
					e.printStackTrace();
				}
			});
		}
	}
}
