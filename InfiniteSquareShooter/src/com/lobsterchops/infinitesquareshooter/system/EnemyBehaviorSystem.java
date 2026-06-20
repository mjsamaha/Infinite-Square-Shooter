package com.lobsterchops.infinitesquareshooter.system;

import com.lobsterchops.infinitesquareshooter.model.UpdateContext;
import com.lobsterchops.infinitesquareshooter.model.entity.Enemy;

public class EnemyBehaviorSystem {

	private final EnemyMovementSystem movementSystem = new EnemyMovementSystem();
	private final EnemyDashSystem dashSystem = new EnemyDashSystem();
	private final EnemyShootingSystem shootingSystem = new EnemyShootingSystem();
	private final EnemyInvisibilitySystem invisibilitySystem = new EnemyInvisibilitySystem();
	private final EnemyBombSystem bombSystem = new EnemyBombSystem();

	public void update(Enemy enemy, UpdateContext context) {
		invisibilitySystem.update(enemy, context);
		movementSystem.update(enemy, context);
		dashSystem.update(enemy, context);
		shootingSystem.update(enemy, context);
		bombSystem.update(enemy, context);
	}
}