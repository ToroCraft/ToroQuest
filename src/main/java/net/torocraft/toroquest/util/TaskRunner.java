package net.torocraft.toroquest.util;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class TaskRunner {
	private static ConcurrentHashMap<Runnable, Integer> runQueue = new ConcurrentHashMap<Runnable, Integer>();

	public static void run() {
		if (runQueue.size() < 1) {
			return;
		}
		runNextQueueItem();
	}

	protected static void runNextQueueItem() {
		Runnable nextRunnable = popRunQueue();
		if (nextRunnable != null) {
			nextRunnable.run();
		}
	}

	public static void queueTask(Runnable task, int delay) {
		runQueue.put(task, delay);
	}

	private static Runnable popRunQueue() {
		Entry<Runnable, Integer> next = null;
		for (Entry<Runnable, Integer> n : runQueue.entrySet()) {
			next = n;
			break;
		}

		if (next == null) {
			return null;
		}

		if (next.getValue() < 1) {
			runQueue.remove(next.getKey());
			return next.getKey();
		} else {
			next.setValue(next.getValue() - 1);
			return null;
		}
	}
}
