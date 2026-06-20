package com.lobsterchops.infinitesquareshooter.config.stats;

import java.util.List;

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