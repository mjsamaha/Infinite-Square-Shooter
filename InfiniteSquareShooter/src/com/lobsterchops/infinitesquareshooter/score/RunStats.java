package com.lobsterchops.infinitesquareshooter.score;

import com.lobsterchops.infinitesquareshooter.model.GameWorld;

/**
 * Tracks per-run statistics independent of score — used for the
 * game-over summary and, later, high score persistence.
 */
public class RunStats {

	private int kills;
	private int shotsFired;
	private int highestWaveReached = 1;
	private long elapsedMillis;

	public void recordKill() {
		kills++;
	}

	public void recordShotFired() {
		shotsFired++;
	}

	/** Pulls elapsed time and wave progress from the world. Call once per tick. */
	public void sync(GameWorld world) {
		elapsedMillis = world.getElapsedMillis();
		highestWaveReached = Math.max(highestWaveReached, world.getWaveNumber());
	}

	/** Kills per shot fired — a rough stand-in until real hit/miss tracking lands. */
	public float getAccuracy() {
		return shotsFired == 0 ? 0f : (float) kills / shotsFired;
	}

	public void reset() {
		kills = 0;
		shotsFired = 0;
		highestWaveReached = 1;
		elapsedMillis = 0;
	}

	public int getKills() {
		return kills;
	}

	public int getShotsFired() {
		return shotsFired;
	}

	public int getHighestWaveReached() {
		return highestWaveReached;
	}

	public long getElapsedMillis() {
		return elapsedMillis;
	}
}