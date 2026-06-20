package com.lobsterchops.infinitesquareshooter.system;

import com.lobsterchops.infinitesquareshooter.config.types.BehaviourFlag;
import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.model.UpdateContext;
import com.lobsterchops.infinitesquareshooter.model.entity.Enemy;
import com.lobsterchops.infinitesquareshooter.model.entity.Player;

public class EnemyDashSystem {

	private static final long DASH_DURATION_MS = 220L;

	public void update(Enemy enemy, UpdateContext context) {
		if (!enemy.hasBehaviour(BehaviourFlag.CAN_DASH)) {
			return;
		}

		long now = context.elapsedMillis();

		if (enemy.isDashing()) {
			if (now - enemy.getDashStartedAt() >= DASH_DURATION_MS) {
				enemy.setDashing(false);
			}
			return;
		}

		if (now - enemy.getLastDashTime() < enemy.getStats().dashCooldownMs()) {
			return;
		}

		Player player = context.world().getPlayer();

		if (player == null || player.isDead()) {
			return;
		}

		Vector2 dashDirection = enemy.getPosition().directionTo(player.getPosition());

		enemy.setVelocity(dashDirection.multiply(enemy.getStats().speed() * 3.4f));
		enemy.setDashing(true);
		enemy.setDashStartedAt(now);
		enemy.setLastDashTime(now);
	}
}