package com.lobsterchops.infinitesquareshooter.config.stats;

import com.lobsterchops.infinitesquareshooter.config.types.BehaviourFlag;

import java.util.EnumSet;
import java.util.Set;

/**
 * Immutable configuration snapshot for a single enemy variant.
 *
 * <p>Every enemy in the game is fully described by one instance of this record.
 * Fields that do not apply to a given variant should be set to their sentinel value
 * ({@code 0} for numerics, {@code null} for {@code projectile}). Game logic must
 * always check the relevant {@link com.lobsterchops.infinitesquareshooter.config.types.BehaviourFlag}
 * before reading mechanic-specific fields such as {@code splitCount} or {@code invisibilityMs}.</p>
 *
 * <p>Use {@link Builder} to construct instances; it provides defaults for every optional
 * field so only the fields that matter for a given enemy need to be specified.</p>
 *
 * @param speed          Base movement speed in pixels per frame.
 * @param maxHp          Hit points the enemy starts with before dying.
 * @param behaviours     Set of {@link com.lobsterchops.infinitesquareshooter.config.types.BehaviourFlag}
 *                       values that govern this enemy's AI decisions.
 * @param projectile     Projectile configuration used when this enemy shoots;
 *                       {@code null} if the enemy does not shoot.
 * @param splitCount     Number of child enemies spawned on death; {@code 0} if this enemy does not split.
 * @param invisibilityMs Duration in milliseconds of each invisibility phase; {@code 0} if not a Ghost variant.
 * @param dashCooldownMs Milliseconds between consecutive dashes; {@code 0} if the enemy cannot dash.
 * @param swarmGroupSize Base number of enemies in a swarm spawn group; {@code 0} if not a Swarm variant.
 * @param scoreValue     Points awarded to the player for destroying this enemy.
 */
public record EnemyStats(float speed, int maxHp, Set<BehaviourFlag> behaviours, ProjectileStats projectile,
		int splitCount, long invisibilityMs, long dashCooldownMs, int swarmGroupSize, int scoreValue) {

	/**
	 * Canonical constructor — defensively copies the behaviour set to guarantee
	 * immutability.
	 */
	public EnemyStats {
		behaviours = behaviours.isEmpty() ? EnumSet.noneOf(BehaviourFlag.class) : EnumSet.copyOf(behaviours);
	}

	/**
	 * Convenience check — avoids null-checking projectile everywhere in game logic.
	 */
	public boolean isShooting() {
		return projectile != null;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {

		private float speed = 1f;
		private int maxHp = 1;
		private final EnumSet<BehaviourFlag> behaviours = EnumSet.noneOf(BehaviourFlag.class);
		private ProjectileStats projectile = null;
		private int splitCount = 0;
		private long invisibilityMs = 0L;
		private long dashCooldownMs = 0L;
		private int swarmGroupSize = 0;
		private int scoreValue = 100;

		private Builder() {
		}

		public Builder speed(float speed) {
			this.speed = speed;
			return this;
		}

		public Builder maxHp(int maxHp) {
			this.maxHp = maxHp;
			return this;
		}

		public Builder behaviour(BehaviourFlag... flags) {
			behaviours.addAll(java.util.Arrays.asList(flags));
			return this;
		}

		public Builder projectile(ProjectileStats projectile) {
			this.projectile = projectile;
			return this;
		}

		public Builder splitCount(int splitCount) {
			this.splitCount = splitCount;
			return this;
		}

		public Builder invisibilityMs(long invisibilityMs) {
			this.invisibilityMs = invisibilityMs;
			return this;
		}

		public Builder dashCooldownMs(long dashCooldownMs) {
			this.dashCooldownMs = dashCooldownMs;
			return this;
		}

		public Builder swarmGroupSize(int swarmGroupSize) {
			this.swarmGroupSize = swarmGroupSize;
			return this;
		}

		public Builder scoreValue(int scoreValue) {
			this.scoreValue = scoreValue;
			return this;
		}

		public EnemyStats build() {
			return new EnemyStats(speed, maxHp, behaviours, projectile, splitCount, invisibilityMs, dashCooldownMs,
					swarmGroupSize, scoreValue);
		}
	}

}