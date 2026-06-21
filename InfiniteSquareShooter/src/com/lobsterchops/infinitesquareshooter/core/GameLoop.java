package com.lobsterchops.infinitesquareshooter.core;

import com.lobsterchops.infinitesquareshooter.config.GameConfig;
import com.lobsterchops.infinitesquareshooter.render.DebugMetrics;
import com.lobsterchops.infinitesquareshooter.utils.FpsCounter;

public class GameLoop {

	private final Runnable updateTick;
	private final Runnable requestRepaint;
	
	private final DebugMetrics debugMetrics;
	private final FpsCounter fpsCounter = new FpsCounter();

	private volatile boolean running = true;

	public GameLoop(Runnable updateTick, Runnable requestRepaint, DebugMetrics debugMetrics) {
		this.updateTick = updateTick;
		this.requestRepaint = requestRepaint;
		this.debugMetrics = debugMetrics;
	}

	public void run() {

        double delta = 0.0;
        long lastTime = System.nanoTime();

        while (running) {

            long currentTime = System.nanoTime();
            long elapsed = currentTime - lastTime;
            lastTime = currentTime;

            delta += calculateDelta(elapsed);

            processUpdates(delta);
            delta %= 1; // keeps leftover fractional time

            fpsCounter.frame(elapsed);

            updateFpsIfNeeded();
        }
    }
	
	private double calculateDelta(long elapsedNanos) {
		return elapsedNanos / GameConfig.DRAW_INTERVAL;
	}
	
	private void processUpdates(double delta) {
		while (delta >= 1) {
			updateTick.run();
			requestRepaint.run();
			delta--;
		}
	}
	
	private void updateFpsIfNeeded() {
		if (fpsCounter.shouldUpdate()) {
			debugMetrics.setFps(fpsCounter.consumeFps());
		}
	}

	public void stop() {
		running = false;
	}
}
