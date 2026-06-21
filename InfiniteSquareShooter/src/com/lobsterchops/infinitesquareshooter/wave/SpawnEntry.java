package com.lobsterchops.infinitesquareshooter.wave;

import com.lobsterchops.infinitesquareshooter.config.types.EnemyType;

public record SpawnEntry(
		EnemyType type,
		int count,
		int groupSize,
		long delayBetweenGroupsMs
) {
	/**
	 * Creates a spawn entry for a single enemy type with the specified count and delay between spawns.
	 * @param type
	 * @param count
	 * @param delayMs
	 * @return
	 */
	public static SpawnEntry single(EnemyType type, int count, long delayMs) {
		return new SpawnEntry(type, count, 1, delayMs);
	}

	/**
	 * Creates a spawn entry for a group of enemies of the specified type, with the given count, group size, and delay between groups.
	 * @param type
	 * @param count
	 * @param groupSize
	 * @param delayMs
	 * @return
	 */
	public static SpawnEntry group(EnemyType type, int count, int groupSize, long delayMs) {
		return new SpawnEntry(type, count, groupSize, delayMs);
	}
}