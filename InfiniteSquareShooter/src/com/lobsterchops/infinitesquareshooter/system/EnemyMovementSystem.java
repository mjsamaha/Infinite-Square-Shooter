package com.lobsterchops.infinitesquareshooter.system;

import com.lobsterchops.infinitesquareshooter.config.types.BehaviourFlag;
import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.model.UpdateContext;
import com.lobsterchops.infinitesquareshooter.model.entity.Enemy;
import com.lobsterchops.infinitesquareshooter.model.entity.Player;

public class EnemyMovementSystem {

	public void update(Enemy enemy, UpdateContext context) {
		Player player = context.world().getPlayer();
		
		if (enemy.isDashing()) {
			return;
		}

		if (player == null || player.isDead()) {
			enemy.setVelocity(Vector2.ZERO);
			return;
		}

		if (enemy.hasBehaviour(BehaviourFlag.MOVES_CIRCULAR)) {
			updateCircular(enemy, player);
			return;
		}

		if (enemy.hasBehaviour(BehaviourFlag.MOVES_ZIGZAG)) {
			updateZigzag(enemy, player, context);
			return;
		}

		updateStraight(enemy, player);
	}

	private void updateStraight(Enemy enemy, Player player) {
		Vector2 direction = enemy.getPosition().directionTo(player.getPosition());
		enemy.setVelocity(direction.multiply(enemy.getStats().speed()));
	}

	private void updateZigzag(Enemy enemy, Player player, UpdateContext context) {
		Vector2 direction = enemy.getPosition().directionTo(player.getPosition());
		float wave = (float) Math.sin(context.tick() * 0.12f);

		Vector2 perpendicular = new Vector2(-direction.y(), direction.x());
		Vector2 finalDirection = direction.add(perpendicular.multiply(wave * 0.7f)).normalized();

		enemy.setVelocity(finalDirection.multiply(enemy.getStats().speed()));
	}

	private void updateCircular(Enemy enemy, Player player) {
		Vector2 toPlayerVector = player.getPosition().subtract(enemy.getPosition());
		float distance = toPlayerVector.length();

		if (distance == 0f) {
			enemy.setVelocity(Vector2.ZERO);
			return;
		}

		Vector2 towardPlayer = toPlayerVector.normalized();
		Vector2 tangent = new Vector2(-towardPlayer.y(), towardPlayer.x());

		float orbitError = distance - enemy.getOrbitRadius();
		Vector2 correction = towardPlayer.multiply(orbitError * 0.025f);

		Vector2 finalVelocity = tangent
				.multiply(enemy.getStats().speed())
				.add(correction);

		enemy.setVelocity(finalVelocity);
	}
}