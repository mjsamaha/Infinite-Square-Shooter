package com.lobsterchops.infinitesquareshooter.config.stats;

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