package com.lobsterchops.infinitesquareshooter.model;

import com.lobsterchops.infinitesquareshooter.config.GameConfig;

public record UpdateContext(
		GameWorld world,
		long tick,
		long elapsedMillis,
		float fixedDeltaSeconds
) {

	public static UpdateContext fixed(GameWorld world, long tick, long elapsedMillis) {
		return new UpdateContext(
				world,
				tick,
				elapsedMillis,
				1f / GameConfig.TARGET_FPS
		);
	}

	public SpawnService spawnService() {
		return world.getSpawnService();
	}
}