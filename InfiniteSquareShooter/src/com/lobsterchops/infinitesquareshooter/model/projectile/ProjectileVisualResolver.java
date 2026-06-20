package com.lobsterchops.infinitesquareshooter.model.projectile;

import java.awt.Color;

import com.lobsterchops.infinitesquareshooter.config.ColorConfig;

public final class ProjectileVisualResolver {

	public static Color colorFor(Projectile projectile) {
		if (projectile.isHoming()) {
			return ColorConfig.PROJECTILE_HOMING;
		}

		if (projectile.getOwner() == ProjectileOwner.PLAYER) {
			return ColorConfig.PLAYER_PROJECTILE;
		}

		if (projectile.getOwner() == ProjectileOwner.ENEMY) {
			return ColorConfig.PROJECTILE_ENEMY;
		}

		return ColorConfig.WHITE;
	}

	private ProjectileVisualResolver() {
	}
}