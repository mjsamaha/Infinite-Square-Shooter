package com.lobsterchops.infinitesquareshooter.model.projectile;

import java.util.ArrayList;
import java.util.List;

import com.lobsterchops.infinitesquareshooter.config.stats.ProjectileStats;
import com.lobsterchops.infinitesquareshooter.math.Vector2;

public class ProjectileFactory {

	private static final long PLAYER_PROJECTILE_LIFESPAN_MS = 1600L;
	private static final long ENEMY_PROJECTILE_LIFESPAN_MS = 3500L;

	public List<Projectile> createPlayerProjectiles(Vector2 position, Vector2 direction, ProjectileStats stats) {
		return createProjectiles(
				position,
				direction,
				stats,
				ProjectileOwner.PLAYER,
				PLAYER_PROJECTILE_LIFESPAN_MS
		);
	}

	public List<Projectile> createEnemyProjectiles(Vector2 position, Vector2 direction, ProjectileStats stats) {
		return createProjectiles(
				position,
				direction,
				stats,
				ProjectileOwner.ENEMY,
				ENEMY_PROJECTILE_LIFESPAN_MS
		);
	}

	public BombProjectile createBomb(Vector2 position, Vector2 direction) {
		Vector2 velocity = direction.normalized().multiply(2.2f);
		return new BombProjectile(position, velocity, 1);
	}

	private List<Projectile> createProjectiles(Vector2 position, Vector2 direction, ProjectileStats stats,
			ProjectileOwner owner, long lifespanMs) {
		List<Projectile> projectiles = new ArrayList<>();

		int count = Math.max(1, stats.count());
		float startOffset = -((count - 1) * stats.spreadDegrees()) / 2f;

		for (int index = 0; index < count; index++) {
			float angleDegrees = startOffset + index * stats.spreadDegrees();
			Vector2 rotatedDirection = rotate(direction.normalized(), angleDegrees);
			Vector2 velocity = rotatedDirection.multiply(stats.speed());

			projectiles.add(new Projectile(
					position,
					velocity,
					stats.damage(),
					owner,
					stats.isHoming(),
					stats.homingTurnRate(),
					lifespanMs,
					resolveProjectileSize(owner, stats)
			));
		}

		return projectiles;
	}

	private float resolveProjectileSize(ProjectileOwner owner, ProjectileStats stats) {
		if (stats.isHoming()) {
			return 9f;
		}

		if (stats.count() > 1) {
			return 6f;
		}

		return owner == ProjectileOwner.PLAYER ? 6f : 7f;
	}

	private Vector2 rotate(Vector2 direction, float degrees) {
		double radians = Math.toRadians(degrees);
		float cos = (float) Math.cos(radians);
		float sin = (float) Math.sin(radians);

		return new Vector2(
				direction.x() * cos - direction.y() * sin,
				direction.x() * sin + direction.y() * cos
		).normalized();
	}
}