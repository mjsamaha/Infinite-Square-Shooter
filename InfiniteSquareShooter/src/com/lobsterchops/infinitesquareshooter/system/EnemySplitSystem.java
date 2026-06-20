package com.lobsterchops.infinitesquareshooter.system;

import com.lobsterchops.infinitesquareshooter.config.types.BehaviourFlag;
import com.lobsterchops.infinitesquareshooter.config.types.EnemyType;
import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.model.UpdateContext;
import com.lobsterchops.infinitesquareshooter.model.entity.Enemy;

public class EnemySplitSystem {

	public void update(Enemy enemy, UpdateContext context) {
		if (!enemy.isDead()) {
			return;
		}

		if (!enemy.hasBehaviour(BehaviourFlag.SPLITS_ON_DEATH)) {
			return;
		}

		int splitCount = enemy.getStats().splitCount();

		for (int index = 0; index < splitCount; index++) {
			float angle = (float) ((Math.PI * 2) / splitCount * index);
			Vector2 offset = new Vector2(
					(float) Math.cos(angle) * 18f,
					(float) Math.sin(angle) * 18f
			);

			context.spawnService().spawnEnemy(resolveChildType(enemy.getType()), enemy.getPosition().add(offset));
		}
	}

	private EnemyType resolveChildType(EnemyType parentType) {
		return switch (parentType) {
			case SPLITTER_I -> EnemyType.BASIC_I;
			case SPLITTER_II -> EnemyType.BASIC_II;
			case SPLITTER_III -> EnemyType.BASIC_III;
			default -> EnemyType.BASIC_I;
		};
	}
}