package com.lobsterchops.infinitesquareshooter.model.entity;

import com.lobsterchops.infinitesquareshooter.config.ConfigRegistry;
import com.lobsterchops.infinitesquareshooter.config.stats.EnemyStats;
import com.lobsterchops.infinitesquareshooter.config.types.EnemyType;
import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.system.EnemyBehaviorSystem;

public class EnemyFactory {

	private final EnemyBehaviorSystem behaviorSystem;

	public EnemyFactory(EnemyBehaviorSystem behaviorSystem) {
		this.behaviorSystem = behaviorSystem;
	}

	public Enemy createEnemy(EnemyType type, Vector2 position) {
		EnemyStats stats = ConfigRegistry.enemy(type);
		return new Enemy(type, stats, position, behaviorSystem);
	}
}