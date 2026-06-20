package com.lobsterchops.infinitesquareshooter.system;

import java.util.ArrayList;
import java.util.List;

import com.lobsterchops.infinitesquareshooter.config.types.BehaviourFlag;
import com.lobsterchops.infinitesquareshooter.config.types.EnemyType;
import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.model.GameObject;
import com.lobsterchops.infinitesquareshooter.model.UpdateContext;
import com.lobsterchops.infinitesquareshooter.model.entity.Enemy;

public class EnemyDeathSystem {

	private static final double POWER_UP_DROP_CHANCE = 0.12;

	public void update(UpdateContext context) {
		List<GameObject> objects = new ArrayList<>(context.world().getObjects());

		for (GameObject object : objects) {
			if (!(object instanceof Enemy enemy)) {
				continue;
			}

			if (!enemy.isDead() || enemy.isDeathHandled()) {
				continue;
			}

			handleEnemyDeath(enemy, context);
			enemy.markDeathHandled();
		}
	}

	private void handleEnemyDeath(Enemy enemy, UpdateContext context) {
		context.world().addScore(enemy.getScoreValue());

		spawnSplitChildren(enemy, context);
		spawnExplosion(enemy, context);
		rollPowerUpDrop(enemy, context);
	}

	private void spawnSplitChildren(Enemy enemy, UpdateContext context) {
		if (!enemy.hasBehaviour(BehaviourFlag.SPLITS_ON_DEATH)) {
			return;
		}

		int splitCount = enemy.getStats().splitCount();

		for (int index = 0; index < splitCount; index++) {
			float angle = (float) ((Math.PI * 2) / splitCount * index);

			Vector2 offset = new Vector2(
					(float) Math.cos(angle) * 22f,
					(float) Math.sin(angle) * 22f
			);

			context.spawnService().spawnEnemy(
					resolveChildType(enemy.getType()),
					enemy.getPosition().add(offset)
			);
		}
	}

	private void spawnExplosion(Enemy enemy, UpdateContext context) {
		// Hook for Phase 11 visual effects.
	}

	private void rollPowerUpDrop(Enemy enemy, UpdateContext context) {
		if (Math.random() > POWER_UP_DROP_CHANCE) {
			return;
		}

		// Hook for Phase 9 power-ups.
	}

	private EnemyType resolveChildType(EnemyType parentType) {
		if (parentType == EnemyType.SPLITTER_I) return EnemyType.BASIC_I;
		if (parentType == EnemyType.SPLITTER_II) return EnemyType.BASIC_II;
		if (parentType == EnemyType.SPLITTER_III) return EnemyType.BASIC_III;

		return EnemyType.BASIC_I;
	}
}