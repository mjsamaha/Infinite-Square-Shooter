package com.lobsterchops.infinitesquareshooter.config.stats;

/**
 * Immutable configuration for a projectile fired by any entity.
 *
 * <p>Both player and enemy shooters hold a reference to one of these. Any entity that fires
 * a projectile — regardless of type — is fully described by a single {@code ProjectileStats}
 * instance paired with the appropriate {@link com.lobsterchops.infinitesquareshooter.config.types.BehaviourFlag}
 * values on the owning entity.</p>
 *
 * <p>To introduce a new projectile property, add a record component here. The compiler
 * will immediately flag every construction site that needs to be updated, making omissions
 * impossible to overlook.</p>
 *
 * <p>Prefer the static factory methods ({@link #single}, {@link #spread}, {@link #homing})
 * over the canonical constructor for common configurations.</p>
 *
 * @param speed          Travel speed of the projectile in pixels per frame.
 * @param damage         Hit points deducted from the target on contact.
 * @param count          Number of projectiles fired per shot; {@code 1} for single-shot,
 *                       greater than {@code 1} for spread configurations.
 * @param spreadDegrees  Angle in degrees between adjacent projectiles in a spread shot;
 *                       {@code 0} when {@code count} is {@code 1}.
 * @param isHoming       {@code true} if the projectile actively steers toward the player.
 * @param homingTurnRate Maximum turning rate of a homing projectile in radians per frame;
 *                       {@code 0} when {@code isHoming} is {@code false}.
 * @param cooldownMs     Milliseconds the owning entity must wait between consecutive shots.
 */
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
