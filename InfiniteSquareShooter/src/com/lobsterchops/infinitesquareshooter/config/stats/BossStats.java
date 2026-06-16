package com.lobsterchops.infinitesquareshooter.config.stats;

import java.util.List;

/**
 * Immutable configuration for a boss encounter.
 *
 * <p>A boss is defined as an ordered list of {@link BossPhase} objects. Single-phase bosses
 * (e.g. The Phantom) provide a list of exactly one phase. Multi-phase bosses (e.g. The Splitter King)
 * transition through phases as HP thresholds are crossed.</p>
 *
 * <p>Fields that are not relevant to a particular boss should be set to {@code 0}. Game logic
 * must check the boss's {@link com.lobsterchops.infinitesquareshooter.config.types.BehaviourFlag}
 * set before reading mechanic-specific fields such as {@code turretCount} or {@code shellOpenMs}.</p>
 *
 * @param baseSpeed       Base movement speed in pixels per frame, before any per-phase multiplier is applied.
 * @param phases          Ordered list of phases; index {@code 0} is always the opening phase.
 * @param scoreValue      Points awarded to the player upon defeating this boss.
 * @param shellOpenMs     Swarm Queen only — duration in milliseconds that the shell remains open per cycle;
 *                        {@code 0} for all other bosses.
 * @param shellIntervalMs Swarm Queen only — milliseconds between consecutive shell openings;
 *                        {@code 0} for all other bosses.
 * @param turretCount     Fortress only — number of turrets that must be destroyed before the core
 *                        becomes vulnerable; {@code 0} for all other bosses.
 * @param turretHp        Fortress only — hit points per individual turret; {@code 0} for all other bosses.
 * @param spawnIntervalMs Swarm Queen only — milliseconds between each minion spawn wave;
 *                        {@code 0} for all other bosses.
 */
public record BossStats(float baseSpeed, List<BossPhase> phases, int scoreValue, long shellOpenMs, long shellIntervalMs,
		int turretCount, int turretHp, long spawnIntervalMs) {

	/** Canonical constructor — defensively copies phase list. */
	public BossStats {
		phases = List.copyOf(phases);
	}

	/** Total HP across all phases. */
	public int totalHp() {
		return phases.stream().mapToInt(BossPhase::hp).sum();
	}

	/** True if this boss has more than one phase. */
	public boolean isMultiPhase() {
		return phases.size() > 1;
	}


	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {

		private float baseSpeed = 1f;
		private List<BossPhase> phases = List.of();
		private int scoreValue = 5000;
		private long shellOpenMs = 0L;
		private long shellIntervalMs = 0L;
		private int turretCount = 0;
		private int turretHp = 0;
		private long spawnIntervalMs = 0L;

		private Builder() {
		}

		public Builder baseSpeed(float baseSpeed) {
			this.baseSpeed = baseSpeed;
			return this;
		}

		public Builder phases(List<BossPhase> phases) {
			this.phases = phases;
			return this;
		}

		public Builder scoreValue(int scoreValue) {
			this.scoreValue = scoreValue;
			return this;
		}

		public Builder shellOpenMs(long shellOpenMs) {
			this.shellOpenMs = shellOpenMs;
			return this;
		}

		public Builder shellIntervalMs(long shellIntervalMs) {
			this.shellIntervalMs = shellIntervalMs;
			return this;
		}

		public Builder turretCount(int turretCount) {
			this.turretCount = turretCount;
			return this;
		}

		public Builder turretHp(int turretHp) {
			this.turretHp = turretHp;
			return this;
		}

		public Builder spawnIntervalMs(long spawnIntervalMs) {
			this.spawnIntervalMs = spawnIntervalMs;
			return this;
		}

		public BossStats build() {
			return new BossStats(baseSpeed, phases, scoreValue, shellOpenMs, shellIntervalMs, turretCount, turretHp,
					spawnIntervalMs);
		}
	}

}