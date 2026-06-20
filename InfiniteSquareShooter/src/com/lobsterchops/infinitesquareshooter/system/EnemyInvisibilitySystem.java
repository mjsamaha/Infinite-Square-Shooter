package com.lobsterchops.infinitesquareshooter.system;

import com.lobsterchops.infinitesquareshooter.config.types.BehaviourFlag;
import com.lobsterchops.infinitesquareshooter.model.UpdateContext;
import com.lobsterchops.infinitesquareshooter.model.entity.Enemy;

public class EnemyInvisibilitySystem {

	private static final long VISIBLE_MS = 1800L;

	public void update(Enemy enemy, UpdateContext context) {
		if (!enemy.hasBehaviour(BehaviourFlag.CAN_TURN_INVISIBLE)) {
			return;
		}

		long now = context.elapsedMillis();

		if (enemy.getInvisibilityStartedAt() == 0L) {
			enemy.setInvisibilityStartedAt(now);
			return;
		}

		long elapsed = now - enemy.getInvisibilityStartedAt();
		long invisibleMs = enemy.getStats().invisibilityMs();
		long cycleMs = VISIBLE_MS + invisibleMs;
		long cyclePosition = elapsed % cycleMs;

		enemy.setInvisible(cyclePosition > VISIBLE_MS);
	}
}