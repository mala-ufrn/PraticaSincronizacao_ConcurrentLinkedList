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
 * This class basically launches the application and test the implemented classes
 * 
 * @author hugo
 */
public class Main {

	public static final int NUM_SEARCHTHREADS = 300;
	public static final int NUM_INSERTTHREADS = 100;
	public static final int NUM_REMOVETHREADS = 50;
	
	public static final String FORMAT = "%03d";
	
	/**
	 * Application main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		ConcurrentLinkedList sharedList = new ConcurrentLinkedList();
		
		List<Thread> threadsB = new ArrayList<>(NUM_SEARCHTHREADS);
		List<Thread> threadsI = new ArrayList<>(NUM_INSERTTHREADS);
		List<Thread> threadsR = new ArrayList<>(NUM_REMOVETHREADS);
		
		Random random = new Random();
		
		// Instantiation of all threads objects
		threadsB = IntStream.range(0, NUM_SEARCHTHREADS)
				.mapToObj(i -> new SearchThread(sharedList, i, FORMAT, new Integer(random.nextInt(NUM_INSERTTHREADS))))
				.collect(Collectors.toList());
		threadsI = IntStream.range(0, NUM_INSERTTHREADS)
				.mapToObj(i -> new InsertThread(sharedList, i, FORMAT, new Integer(i)))
				.collect(Collectors.toList());
		threadsR = IntStream.range(0, NUM_REMOVETHREADS)
				.mapToObj(i -> new RemoveThread(sharedList, i, FORMAT, new Integer(random.nextInt(NUM_INSERTTHREADS))))
				.collect(Collectors.toList());
		
		// Prepares the launchers
		ThreadsLauncher tlB1 = new ThreadsLauncher(threadsB.subList(0, 99));
		ThreadsLauncher tlB2 = new ThreadsLauncher(threadsB.subList(100, 199));
		ThreadsLauncher tlB3 = new ThreadsLauncher(threadsB.subList(200, 299));
		ThreadsLauncher tlI1 = new ThreadsLauncher(threadsI.subList(0, 49));
		ThreadsLauncher tlI2 = new ThreadsLauncher(threadsI.subList(50, 99));
		ThreadsLauncher tlR1 = new ThreadsLauncher(threadsR.subList(0, 24));
		ThreadsLauncher tlR2 = new ThreadsLauncher(threadsR.subList(25, 49));
		
		// Launch the threads
		tlB1.start();
		tlB2.start();
		tlB3.start();
		tlI1.start();
		tlI2.start();
		tlR1.start();
		tlR2.start();
		
		// Wait all operations
		try {
			tlB1.join();
			tlB2.join();
			tlB3.join();
			tlI1.join();
			tlI2.join();
			tlR1.join();
			tlR2.join();
		} catch (InterruptedException e) {
			System.err.println("Failure in execute join on a ThreadLauncher, generated Exception:");
			e.printStackTrace();
		}
	}
	
	/**
	 * Launches the thread lists
	 * 
	 * @author hugo
	 */
	private static class ThreadsLauncher extends Thread {
		
		List<Thread> threadList;
		int delay; // milliseconds between two launches

		public ThreadsLauncher(List<Thread> threadList) {
			super();
			this.threadList = threadList;
			this.delay = 5000 / threadList.size();
		}

		@Override
		public void run() {
			threadList.stream().forEach(t -> {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					System.err.println("Failure in execute the sleep operation on the ThreadsLauncher, generated Exception:");
					e.printStackTrace();
				}
				t.run();
			});
			
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
