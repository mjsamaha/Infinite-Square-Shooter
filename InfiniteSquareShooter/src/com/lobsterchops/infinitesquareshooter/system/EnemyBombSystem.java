package com.lobsterchops.infinitesquareshooter.system;

import com.lobsterchops.infinitesquareshooter.config.types.BehaviourFlag;
import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.model.UpdateContext;
import com.lobsterchops.infinitesquareshooter.model.entity.Enemy;
import com.lobsterchops.infinitesquareshooter.model.entity.Player;

public class EnemyBombSystem {

	private static final long DEFAULT_BOMB_COOLDOWN_MS = 2200L;

	public void update(Enemy enemy, UpdateContext context) {
		if (!enemy.hasBehaviour(BehaviourFlag.DROPS_BOMBS)) {
			return;
		}

		long now = context.elapsedMillis();

		if (now - enemy.getLastBombTime() < DEFAULT_BOMB_COOLDOWN_MS) {
			return;
		}

		Player player = context.world().getPlayer();

		if (player == null || player.isDead()) {
			return;
		}

		Vector2 direction = enemy.getPosition().directionTo(player.getPosition());
		context.spawnService().spawnEnemyBomb(enemy.getPosition(), direction);

		enemy.setLastBombTime(now);
	}
}