package com.lobsterchops.infinitesquareshooter.config;

public final class GameConfig {

	// Primary timing configuration.
	public static final int TARGET_FPS = 60;
	public static final double DRAW_INTERVAL_NANOS = 1_000_000_000.0 / TARGET_FPS;
	public static final long TIMER_INTERVAL_NANOS = 1_000_000_000L;

	// Compatibility aliases (safe during migration).
	public static final int FPS = TARGET_FPS;
	public static final double DRAW_INTERVAL = DRAW_INTERVAL_NANOS;
	public static final long TIMER_INTERVAL = TIMER_INTERVAL_NANOS;

	private GameConfig() {
		
	}

}
