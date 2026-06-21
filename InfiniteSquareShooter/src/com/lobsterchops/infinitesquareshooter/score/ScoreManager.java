package com.lobsterchops.infinitesquareshooter.score;

/**
 * Tracks the player's score for the current run, including combo
 * multipliers for rapid kills and flat bonuses for clearing waves
 * or defeating bosses.
 */
public class ScoreManager {
	
	private static final long COMBO_WINDOW_MS = 1500L;
	private static final float COMBO_MULTIPLIER_STEP = 0.1f;
	private static final float MAX_COMBO_MULTIPLIER = 3.0f;
	
	private static final int WAVE_BONUS_PER_WAVE = 100;
	
	private int score;
	private int comboCount;
	private long lastKillAtMs;
	
	/** Awards points for a kill, scaled by the current combo multiplier. */
	public void addKillScore(int baseValue, long nowMs) {
		if (nowMs - lastKillAtMs > COMBO_WINDOW_MS) {
			comboCount = 0;
		}

		comboCount++;
		lastKillAtMs = nowMs;

		score += Math.round(baseValue * currentMultiplier());
	}
	
	/** Awards a flat bonus for completing a wave. */
	public void addWaveBonus(int waveNumber) {
		score += waveNumber * WAVE_BONUS_PER_WAVE;
	}

	/** Awards a boss's configured score value on defeat. */
	public void addBossBonus(int bossScoreValue) {
		score += bossScoreValue;
	}

	/** Adds a flat amount with no combo scaling — for pickups, etc. */
	public void addBonus(int amount) {
		score += Math.max(0, amount);
	}

	/** Decays the combo once the kill window has expired. Call once per tick. */
	public void tick(long nowMs) {
		if (comboCount > 0 && nowMs - lastKillAtMs > COMBO_WINDOW_MS) {
			comboCount = 0;
		}
	}

	public void reset() {
		score = 0;
		comboCount = 0;
		lastKillAtMs = 0;
	}

	public int getScore() {
		return score;
	}

	public int getComboCount() {
		return comboCount;
	}

	public float getComboMultiplier() {
		return currentMultiplier();
	}

	private float currentMultiplier() {
		return Math.min(MAX_COMBO_MULTIPLIER, 1f + comboCount * COMBO_MULTIPLIER_STEP);
	}

}
