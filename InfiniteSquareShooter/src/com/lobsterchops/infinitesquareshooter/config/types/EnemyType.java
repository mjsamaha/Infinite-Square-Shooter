package com.lobsterchops.infinitesquareshooter.config.types;

import com.lobsterchops.infinitesquareshooter.config.stats.EnemyStats;
import com.lobsterchops.infinitesquareshooter.config.stats.ProjectileStats;

/**
 * Enumeration of every enemy variant in the game.
 *
 * <p>Each constant is the single source of truth for that variant's {@link EnemyStats}.
 * To add a new enemy variant, declare one constant here with its stats — no other
 * config file needs to change. The {@link com.lobsterchops.infinitesquareshooter.config.registry.ConfigRegistry}
 * automatically picks up all constants at class-load time.</p>
 *
 * <p>Constants follow the naming convention {@code TYPE_ROMANNUMERAL}, for example
 * {@code TANK_II} or {@code SHOOTER_III}. Roman numerals I–III correspond to the
 * three acts of wave progression, with higher numerals representing faster and more
 * dangerous variants of the same archetype.</p>
 */
public enum EnemyType {

	BASIC_I(EnemyStats.builder().speed(1.5f).maxHp(2).behaviour(BehaviourFlag.MOVES_STRAIGHT).scoreValue(50).build()),

	BASIC_II(EnemyStats.builder().speed(2.5f).maxHp(1).behaviour(BehaviourFlag.MOVES_STRAIGHT).scoreValue(75).build()),

	BASIC_III(
			EnemyStats.builder().speed(3.5f).maxHp(1).behaviour(BehaviourFlag.MOVES_STRAIGHT).scoreValue(100).build()),

	ZIGZAG_I(EnemyStats.builder().speed(2.0f).maxHp(2).behaviour(BehaviourFlag.MOVES_ZIGZAG).scoreValue(75).build()),

	ZIGZAG_II(EnemyStats.builder().speed(3.0f).maxHp(1).behaviour(BehaviourFlag.MOVES_ZIGZAG).scoreValue(100).build()),

	ZIGZAG_III(EnemyStats.builder().speed(4.0f).maxHp(1).behaviour(BehaviourFlag.MOVES_ZIGZAG).scoreValue(125).build()),

	SHOOTER_I(EnemyStats.builder().speed(2.0f).maxHp(2)
			.behaviour(BehaviourFlag.MOVES_STRAIGHT, BehaviourFlag.SHOOTS_SINGLE)
			.projectile(ProjectileStats.single(5f, 1, 2000L)).scoreValue(150).build()),

	SHOOTER_II(EnemyStats.builder().speed(3.0f).maxHp(1)
			.behaviour(BehaviourFlag.MOVES_STRAIGHT, BehaviourFlag.SHOOTS_SINGLE)
			.projectile(ProjectileStats.single(6f, 1, 1500L)).scoreValue(175).build()),

	SHOOTER_III(EnemyStats.builder().speed(4.0f).maxHp(1)
			.behaviour(BehaviourFlag.MOVES_STRAIGHT, BehaviourFlag.SHOOTS_SINGLE)
			.projectile(ProjectileStats.single(7f, 1, 1200L)).scoreValue(200).build()),

	DASHER_I(EnemyStats.builder().speed(4.0f).maxHp(1).behaviour(BehaviourFlag.MOVES_STRAIGHT, BehaviourFlag.CAN_DASH)
			.dashCooldownMs(2500L).scoreValue(125).build()),

	DASHER_II(EnemyStats.builder().speed(4.0f).maxHp(2).behaviour(BehaviourFlag.MOVES_STRAIGHT, BehaviourFlag.CAN_DASH)
			.dashCooldownMs(2000L).scoreValue(175).build()),

	DASHER_III(EnemyStats.builder().speed(4.0f).maxHp(1).behaviour(BehaviourFlag.MOVES_STRAIGHT, BehaviourFlag.CAN_DASH)
			.dashCooldownMs(1500L).scoreValue(150).build()),

	SPREAD_I(EnemyStats.builder().speed(2.0f).maxHp(2)
			.behaviour(BehaviourFlag.MOVES_STRAIGHT, BehaviourFlag.SHOOTS_SPREAD)
			.projectile(ProjectileStats.spread(5f, 1, 5, 15f, 2500L)).scoreValue(175).build()),

	SPREAD_II(EnemyStats.builder().speed(3.0f).maxHp(1)
			.behaviour(BehaviourFlag.MOVES_STRAIGHT, BehaviourFlag.SHOOTS_SPREAD)
			.projectile(ProjectileStats.spread(6f, 1, 5, 15f, 2000L)).scoreValue(200).build()),

	SPREAD_III(EnemyStats.builder().speed(4.0f).maxHp(1)
			.behaviour(BehaviourFlag.MOVES_STRAIGHT, BehaviourFlag.SHOOTS_SPREAD)
			.projectile(ProjectileStats.spread(7f, 1, 7, 12f, 1500L)).scoreValue(225).build()),

	TANK_I(EnemyStats.builder().speed(0.8f).maxHp(4).behaviour(BehaviourFlag.MOVES_STRAIGHT).scoreValue(200).build()),

	TANK_II(EnemyStats.builder().speed(0.8f).maxHp(6).behaviour(BehaviourFlag.MOVES_STRAIGHT).scoreValue(300).build()),

	TANK_III(EnemyStats.builder().speed(0.8f).maxHp(8).behaviour(BehaviourFlag.MOVES_STRAIGHT).scoreValue(400).build()),

	SPLITTER_I(EnemyStats.builder().speed(1.5f).maxHp(2)
			.behaviour(BehaviourFlag.MOVES_STRAIGHT, BehaviourFlag.SPLITS_ON_DEATH).splitCount(2).scoreValue(150)
			.build()),

	SPLITTER_II(EnemyStats.builder().speed(3.5f).maxHp(1)
			.behaviour(BehaviourFlag.MOVES_STRAIGHT, BehaviourFlag.SPLITS_ON_DEATH).splitCount(3).scoreValue(175)
			.build()),

	SPLITTER_III(EnemyStats.builder().speed(3.5f).maxHp(1)
			.behaviour(BehaviourFlag.MOVES_STRAIGHT, BehaviourFlag.SPLITS_ON_DEATH).splitCount(4).scoreValue(200)
			.build()),

	ORBITER_I(EnemyStats.builder().speed(2.0f).maxHp(2)
			.behaviour(BehaviourFlag.MOVES_CIRCULAR, BehaviourFlag.SHOOTS_SINGLE)
			.projectile(ProjectileStats.single(5f, 1, 2000L)).scoreValue(200).build()),

	ORBITER_II(EnemyStats.builder().speed(3.0f).maxHp(1)
			.behaviour(BehaviourFlag.MOVES_CIRCULAR, BehaviourFlag.SHOOTS_SINGLE)
			.projectile(ProjectileStats.single(6f, 1, 1800L)).scoreValue(225).build()),

	ORBITER_III(EnemyStats.builder().speed(4.0f).maxHp(1)
			.behaviour(BehaviourFlag.MOVES_CIRCULAR, BehaviourFlag.SHOOTS_SINGLE)
			.projectile(ProjectileStats.single(7f, 1, 1500L)).scoreValue(250).build()),

	BOMBER_I(EnemyStats.builder().speed(2.0f).maxHp(5)
			.behaviour(BehaviourFlag.MOVES_STRAIGHT, BehaviourFlag.DROPS_BOMBS).scoreValue(300).build()),

	BOMBER_II(EnemyStats.builder().speed(3.0f).maxHp(3)
			.behaviour(BehaviourFlag.MOVES_STRAIGHT, BehaviourFlag.DROPS_BOMBS).scoreValue(350).build()),

	BOMBER_III(EnemyStats.builder().speed(4.0f).maxHp(1)
			.behaviour(BehaviourFlag.MOVES_STRAIGHT, BehaviourFlag.DROPS_BOMBS).scoreValue(400).build()),

	GHOST_I(EnemyStats.builder().speed(2.0f).maxHp(2)
			.behaviour(BehaviourFlag.MOVES_STRAIGHT, BehaviourFlag.CAN_TURN_INVISIBLE).invisibilityMs(1500L)
			.scoreValue(200).build()),

	GHOST_II(EnemyStats.builder().speed(2.0f).maxHp(1)
			.behaviour(BehaviourFlag.MOVES_STRAIGHT, BehaviourFlag.CAN_TURN_INVISIBLE).invisibilityMs(2500L)
			.scoreValue(225).build()),

	GHOST_III(EnemyStats.builder().speed(2.0f).maxHp(1)
			.behaviour(BehaviourFlag.MOVES_STRAIGHT, BehaviourFlag.CAN_TURN_INVISIBLE).invisibilityMs(4000L)
			.scoreValue(250).build()),

	HOMING_I(EnemyStats.builder().speed(2.0f).maxHp(2)
			.behaviour(BehaviourFlag.MOVES_STRAIGHT, BehaviourFlag.SHOOTS_HOMING)
			.projectile(ProjectileStats.homing(3f, 1, 0.03f, 3000L)).scoreValue(225).build()),

	HOMING_II(EnemyStats.builder().speed(2.0f).maxHp(1)
			.behaviour(BehaviourFlag.MOVES_STRAIGHT, BehaviourFlag.SHOOTS_HOMING)
			.projectile(ProjectileStats.homing(5f, 1, 0.05f, 2500L)).scoreValue(250).build()),

	HOMING_III(EnemyStats.builder().speed(2.0f).maxHp(1)
			.behaviour(BehaviourFlag.MOVES_STRAIGHT, BehaviourFlag.SHOOTS_HOMING)
			.projectile(new ProjectileStats(6f, 1, 3, 10f, true, 0.06f, 2000L)).scoreValue(300).build()),

	SWARM_I(EnemyStats.builder().speed(4.0f).maxHp(1).behaviour(BehaviourFlag.MOVES_STRAIGHT).swarmGroupSize(7)
			.scoreValue(25).build()),

	SWARM_II(EnemyStats.builder().speed(4.5f).maxHp(1).behaviour(BehaviourFlag.MOVES_STRAIGHT).swarmGroupSize(12)// base size — WaveManager scales this per wave
			.scoreValue(30).build()),

	SWARM_III(EnemyStats.builder().speed(5.0f).maxHp(1).behaviour(BehaviourFlag.MOVES_STRAIGHT).swarmGroupSize(18)// base size — WaveManager scales this per wave
			.scoreValue(35).build());

	private final EnemyStats stats;

	EnemyType(EnemyStats stats) {
		this.stats = stats;
	}

	public EnemyStats getStats() {
		return stats;
	}

}
