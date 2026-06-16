package com.lobsterchops.infinitesquareshooter.core;

import com.lobsterchops.infinitesquareshooter.config.GameConfig;

public class GameLoop {

	private final Runnable updateTick;
	private final Runnable requestRepaint;

	private volatile boolean running = true;

	public GameLoop(Runnable updateTick, Runnable requestRepaint) {
		this.updateTick = updateTick;
		this.requestRepaint = requestRepaint;
	}

	public void run() {
		double delta = 0.0; // accumulates time to determine when to update/render
		long lastTime = System.nanoTime(); // tracks the last time we updated/rendered
		long timer = 0L; // accumulates time for FPS calculation
		int frameCount = 0; // counts frames rendered in the current second

		while (running) {
			long currentTime = System.nanoTime();
			long elapsed = currentTime - lastTime;
			lastTime = currentTime;

			delta += elapsed / GameConfig.DRAW_INTERVAL;
			timer += elapsed;

			while (delta >= 1) {
				updateTick.run();
				requestRepaint.run();
				delta--;
				frameCount++; // count each rendered frame
			}

			if (timer >= GameConfig.TIMER_INTERVAL) {
				System.out.println("FPS: " + frameCount); // real FPS over 1 second
				frameCount = 0; // reset for next second
				timer = 0L;
			}
		}
	}

	public void stop() {
		running = false;
	}
}
