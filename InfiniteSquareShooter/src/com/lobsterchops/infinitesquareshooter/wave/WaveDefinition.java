package com.lobsterchops.infinitesquareshooter.wave;

import java.util.List;

import com.lobsterchops.infinitesquareshooter.config.types.BossType;

public record WaveDefinition(
		int waveNumber,
		String label,
		List<SpawnEntry> spawns,
		long baseSpawnDelayMs,
		String notes,
		BossType bossType,
		boolean endless
) {
	public boolean isBossWave() {
		return bossType != null;
	}

	public boolean isEndless() {
		return endless;
	}
}