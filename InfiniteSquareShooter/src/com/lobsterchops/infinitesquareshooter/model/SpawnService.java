package com.lobsterchops.infinitesquareshooter.model;

import com.lobsterchops.infinitesquareshooter.config.stats.ProjectileStats;
import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.model.projectile.Projectile;
import com.lobsterchops.infinitesquareshooter.model.projectile.ProjectileFactory;

public class SpawnService {

	private final GameWorld world;
	private final ProjectileFactory projectileFactory = new ProjectileFactory();

	public SpawnService(GameWorld world) {
		this.world = world;
	}

	public void spawnPlayerProjectile(Vector2 position, Vector2 direction, ProjectileStats stats) {
		Projectile projectile = projectileFactory.createPlayerProjectile(position, direction, stats);
		world.addObject(projectile);
	}
}