package com.lobsterchops.infinitesquareshooter.config;

/**
 * Global constants that don't belong to any specific game entity.
 *
 * Simple primitives like these don't need records or enums — a final class with
 * static constants is the right tool. Only move values here if they are truly
 * global and not entity-specific.
 */
public final class GameConfig {

	public static final int TARGET_FPS = 60;
	public static final double DRAW_INTERVAL_NANOS = 1_000_000_000.0 / TARGET_FPS;
	public static final long TIMER_INTERVAL_NANOS = 1_000_000_000L;

	public static final int FPS = TARGET_FPS;
	public static final double DRAW_INTERVAL = DRAW_INTERVAL_NANOS;
	public static final long TIMER_INTERVAL = TIMER_INTERVAL_NANOS;

	/** Multiplier applied to enemy spawn rate each wave. */
	public static final float SPAWN_RATE_SCALE_PER_WAVE = 1.1f;

	/** Milliseconds between the end of one wave and the start of the next. */
	public static final long BETWEEN_WAVE_DELAY_MS = 3_000L;

	/** Number of waves per act before a boss fight. */
	public static final int WAVES_PER_ACT = 12;

	/** Multiplier applied to all enemy stats in endless mode per cycle. */
	public static final float ENDLESS_DIFFICULTY_SCALE = 1.15f;

	private GameConfig() {

	}

}
