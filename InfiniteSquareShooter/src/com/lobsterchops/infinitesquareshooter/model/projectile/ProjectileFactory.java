package com.lobsterchops.infinitesquareshooter.model.projectile;

import com.lobsterchops.infinitesquareshooter.config.stats.ProjectileStats;
import com.lobsterchops.infinitesquareshooter.math.Vector2;

public class ProjectileFactory {

	public Projectile createPlayerProjectile(Vector2 position, Vector2 direction, ProjectileStats stats) {
		Vector2 velocity = direction.normalized().multiply(stats.speed());
		return new Projectile(position, velocity, stats.damage(), ProjectileOwner.PLAYER);
	}

}
