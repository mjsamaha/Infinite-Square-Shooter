package com.lobsterchops.infinitesquareshooter.config.types;

import java.util.EnumSet;
import java.util.List;

import com.lobsterchops.infinitesquareshooter.config.stats.BossPhase;
import com.lobsterchops.infinitesquareshooter.config.stats.BossStats;
import com.lobsterchops.infinitesquareshooter.config.stats.ProjectileStats;

public enum BossType {

	SWARM_QUEEN(
			BossStats.builder().baseSpeed(1.0f)
					.phases(List.of(new BossPhase(1, 40, 1.0f,
							EnumSet.of(BehaviourFlag.SPAWNS_MINIONS, BehaviourFlag.HAS_SHIELD),
							ProjectileStats.single(5f, 1, 1000L), 1.0f)))
					.scoreValue(10_000).shellOpenMs(2000L).shellIntervalMs(17_500L).spawnIntervalMs(3000L).build()),

	FORTRESS(BossStats.builder().baseSpeed(0.5f)
			.phases(List.of(new BossPhase(1, 60, 1.0f, EnumSet.of(BehaviourFlag.TURRET_PROTECTED), null, // core doesn't shoot; turrets handle it
					1.0f)))
			.scoreValue(12_000).turretCount(4).turretHp(5).build()),

	SPLITTER_KING(
			BossStats.builder().baseSpeed(2.0f)
					.phases(List.of(new BossPhase(1, 30, 0.5f,
							EnumSet.of(BehaviourFlag.MOVES_STRAIGHT, BehaviourFlag.SPLITS_ON_DEATH), null, 1.0f),
							new BossPhase(2, 20, 0.5f,
									EnumSet.of(BehaviourFlag.MOVES_STRAIGHT, BehaviourFlag.SPLITS_ON_DEATH), null, 1.3f // gets
																														// faster
																														// in
																														// tier
																														// 2
							), new BossPhase(3, 10, 1.0f,
									EnumSet.of(BehaviourFlag.MOVES_STRAIGHT, BehaviourFlag.SPLITS_ON_DEATH), null, 1.6f// even faster in tier 3
							))).scoreValue(15_000).build()),

	PHANTOM(BossStats.builder().baseSpeed(3.0f)
			.phases(List.of(new BossPhase(1, 50, 1.0f,
					EnumSet.of(BehaviourFlag.CAN_TURN_INVISIBLE, BehaviourFlag.CAN_TELEPORT,
							BehaviourFlag.SHOOTS_HOMING),
					ProjectileStats.homing(6f, 1, 0.06f, 2000L), 1.0f)))
			.scoreValue(20_000).build()),

	MIMIC(BossStats.builder().baseSpeed(0f) // doesn't move independently — mirrors player position
			.phases(List.of(
					new BossPhase(1, 60, 0.5f, EnumSet.of(BehaviourFlag.MIRRORS_PLAYER, BehaviourFlag.SHOOTS_SINGLE),
							ProjectileStats.single(8f, 1, 800L), 1.0f),
					new BossPhase(2, 60, 1.0f, EnumSet.of(BehaviourFlag.MIRRORS_PLAYER, BehaviourFlag.SHOOTS_SINGLE,
							BehaviourFlag.SHOOTS_SPREAD// adds spread in phase 2
					), ProjectileStats.spread(8f, 1, 3, 20f, 600L), 1.0f))).scoreValue(25_000).build());

	private final BossStats stats;

	BossType(BossStats stats) {
		this.stats = stats;
	}

	public BossStats getStats() {
		return stats;
	}

}
