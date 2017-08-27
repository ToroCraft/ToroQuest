package net.torocraft.toroquest.util;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class Timer {
	
	public static final Timer INSTANCE = new Timer();

	private ConcurrentHashMap<Runnable, Integer> queue = new ConcurrentHashMap<Runnable, Integer>();

	@SubscribeEvent
	public void handleWorldTick(WorldTickEvent event) {
		if (queue.size() < 1) {
			return;
		}
		runNextQueueItem();
	}

	public void addScheduledTask(Runnable task) {
		addScheduledTask(task, 0);
	}

	public void addScheduledTask(Runnable task, int delay) {
		queue.put(task, delay);
	}

	private void runNextQueueItem() {
		Runnable nextRunnable = popRunQueue();
		if (nextRunnable != null) {
			nextRunnable.run();
		}
	}

	private Runnable popRunQueue() {
		Entry<Runnable, Integer> next = null;
		for (Entry<Runnable, Integer> n : queue.entrySet()) {
			next = n;
			break;
		}

		if (next == null) {
			return null;
		}

		if (next.getValue() < 1) {
			queue.remove(next.getKey());
			return next.getKey();
		} else {
			next.setValue(next.getValue() - 1);
			return null;
		}
	}

}
