package com.lobsterchops.infinitesquareshooter.wave;

import com.lobsterchops.infinitesquareshooter.config.types.EnemyType;

public record SpawnEntry(
		EnemyType type,
		int count,
		int groupSize,
		long delayBetweenGroupsMs
) {
	public static SpawnEntry single(EnemyType type, int count, long delayMs) {
		return new SpawnEntry(type, count, 1, delayMs);
	}

	public static SpawnEntry group(EnemyType type, int count, int groupSize, long delayMs) {
		return new SpawnEntry(type, count, groupSize, delayMs);
	}
}