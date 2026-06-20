package com.lobsterchops.infinitesquareshooter.config.stats;

public record ProjectileStats(float speed, int damage, int count, float spreadDegrees, boolean isHoming,
		float homingTurnRate, long cooldownMs) {

	/** Convenience factory — single non-homing projectile. */
	public static ProjectileStats single(float speed, int damage, long cooldownMs) {
		return new ProjectileStats(speed, damage, 1, 0f, false, 0f, cooldownMs);
	}

	/** Convenience factory — spread shot, non-homing. */
	public static ProjectileStats spread(float speed, int damage, int count, float spreadDegrees, long cooldownMs) {
		return new ProjectileStats(speed, damage, count, spreadDegrees, false, 0f, cooldownMs);
	}

	/** Convenience factory — homing projectile. */
	public static ProjectileStats homing(float speed, int damage, float homingTurnRate, long cooldownMs) {
		return new ProjectileStats(speed, damage, 1, 0f, true, homingTurnRate, cooldownMs);
	}

}
