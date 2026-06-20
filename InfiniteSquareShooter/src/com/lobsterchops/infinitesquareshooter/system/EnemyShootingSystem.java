package com.lobsterchops.infinitesquareshooter.system;

import com.lobsterchops.infinitesquareshooter.config.stats.ProjectileStats;
import com.lobsterchops.infinitesquareshooter.config.types.BehaviourFlag;
import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.model.UpdateContext;
import com.lobsterchops.infinitesquareshooter.model.entity.Enemy;
import com.lobsterchops.infinitesquareshooter.model.entity.Player;

public class EnemyShootingSystem {

	public void update(Enemy enemy, UpdateContext context) {
		if (!enemy.getStats().isShooting()) {
			return;
		}

		if (!enemy.hasBehaviour(BehaviourFlag.SHOOTS_SINGLE)
				&& !enemy.hasBehaviour(BehaviourFlag.SHOOTS_SPREAD)
				&& !enemy.hasBehaviour(BehaviourFlag.SHOOTS_HOMING)) {
			return;
		}

		ProjectileStats projectile = enemy.getStats().projectile();
		long now = context.elapsedMillis();

		if (now - enemy.getLastShotTime() < projectile.cooldownMs()) {
			return;
		}

		Player player = context.world().getPlayer();

		if (player == null || player.isDead()) {
			return;
		}

		Vector2 direction = enemy.getPosition().directionTo(player.getPosition());
		context.spawnService().spawnEnemyProjectiles(enemy.getPosition(), direction, projectile);

		enemy.setLastShotTime(now);
	}
}