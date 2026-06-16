package com.lobsterchops.infinitesquareshooter.config.stats;

/**
 * Immutable configuration for the player character.
 *
 * <p>This record represents the player's base stats as they stand at any given moment.
 * Power-ups must not mutate an existing instance; instead, use the {@code with}-style
 * factory helpers defined on this record to produce a modified copy, leaving the
 * original intact. The {@link com.lobsterchops.infinitesquareshooter.config.registry.ConfigRegistry}
 * holds the canonical starting values that apply at the beginning of each run.</p>
 *
 * @param startingLives   Number of lives the player begins with.
 * @param moveSpeed       Base movement speed in pixels per frame.
 * @param invincibilityMs Duration in milliseconds of the invincibility window granted after taking a hit.
 * @param projectile      Default {@link ProjectileStats} configuration for the player's weapon.
 * @param maxLives        Hard upper limit on lives; power-ups cannot push the count above this value.
 */
public record PlayerStats(int startingLives, float moveSpeed, long invincibilityMs, ProjectileStats projectile,
		int maxLives) {

	/** Returns a copy with a different move speed — useful for speed power-ups. */
	public PlayerStats withMoveSpeed(float newSpeed) {
		return new PlayerStats(startingLives, newSpeed, invincibilityMs, projectile, maxLives);
	}

	/**
	 * Returns a copy with a different projectile config — useful for weapon
	 * power-ups.
	 */
	public PlayerStats withProjectile(ProjectileStats newProjectile) {
		return new PlayerStats(startingLives, moveSpeed, invincibilityMs, newProjectile, maxLives);
	}

	/** Returns a copy with additional lives added (clamped to maxLives). */
	public PlayerStats withExtraLife() {
		int newLives = Math.min(startingLives + 1, maxLives);
		return new PlayerStats(newLives, moveSpeed, invincibilityMs, projectile, maxLives);
	}

}