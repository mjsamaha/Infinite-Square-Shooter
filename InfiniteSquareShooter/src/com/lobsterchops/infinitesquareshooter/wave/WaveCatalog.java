package com.lobsterchops.infinitesquareshooter.wave;

import java.util.List;

import com.lobsterchops.infinitesquareshooter.config.types.BossType;
import static com.lobsterchops.infinitesquareshooter.config.types.EnemyType.*;

public final class WaveCatalog {

	private static final int FINAL_STORY_WAVE = 36;

	private static final List<WaveDefinition> STORY_WAVES = List.of(
			wave(1, "Act 1-1: First Contact", "Tutorial wave - ease the player in",
					SpawnEntry.single(BASIC_I, 8, 900L)),

			wave(2, "Act 1-2: New Angles", "Introduce movement variety",
					SpawnEntry.single(BASIC_I, 8, 850L),
					SpawnEntry.single(ZIGZAG_I, 5, 950L)),

			wave(3, "Act 1-3: Speed Pressure", "Speed pressure begins",
					SpawnEntry.single(ZIGZAG_I, 7, 850L),
					SpawnEntry.single(DASHER_I, 4, 1100L)),

			wave(4, "Act 1-4: First Fire", "Introduce projectile threats",
					SpawnEntry.single(SHOOTER_I, 8, 1000L)),

			wave(5, "Act 1-5: Mixed Formation", "First mixed wave",
					SpawnEntry.single(BASIC_I, 8, 750L),
					SpawnEntry.single(SHOOTER_I, 5, 1000L),
					SpawnEntry.single(ZIGZAG_I, 5, 850L)),

			wave(6, "Act 1-6: Swarm Test", "Overwhelming numbers - tests movement",
					SpawnEntry.group(SWARM_I, 24, 6, 1200L)),

			wave(7, "Act 1-7: Heavy Hazards", "Slow but dangerous combo",
					SpawnEntry.single(BOMBER_I, 5, 1100L),
					SpawnEntry.single(TANK_I, 4, 1300L)),

			wave(8, "Act 1-8: Cross Pressure", "Positional pressure from all angles",
					SpawnEntry.single(ORBITER_I, 5, 1200L),
					SpawnEntry.single(HOMING_I, 5, 1200L)),

			wave(9, "Act 1-9: Hard To Track", "Hard to track plus fast closers",
					SpawnEntry.single(GHOST_I, 6, 1100L),
					SpawnEntry.single(DASHER_I, 6, 900L)),

			wave(10, "Act 1-10: Projectile Wall", "Heavy projectile wave",
					SpawnEntry.single(SPREAD_I, 6, 1150L),
					SpawnEntry.single(SHOOTER_I, 7, 950L)),

			wave(11, "Act 1-11: Split Swarm", "Chaotic split plus swarm combo",
					SpawnEntry.single(SPLITTER_I, 5, 1200L),
					SpawnEntry.group(SWARM_I, 18, 6, 1300L)),

			wave(12, "Act 1-12: Level I Gauntlet", "All Level I types before the boss",
					SpawnEntry.single(BASIC_I, 4, 700L),
					SpawnEntry.single(ZIGZAG_I, 4, 800L),
					SpawnEntry.single(SHOOTER_I, 4, 950L),
					SpawnEntry.single(DASHER_I, 4, 850L),
					SpawnEntry.single(SPREAD_I, 3, 1100L),
					SpawnEntry.single(TANK_I, 3, 1300L),
					SpawnEntry.single(SPLITTER_I, 3, 1250L),
					SpawnEntry.single(ORBITER_I, 3, 1200L),
					SpawnEntry.single(BOMBER_I, 3, 1300L),
					SpawnEntry.single(GHOST_I, 3, 1150L),
					SpawnEntry.single(HOMING_I, 3, 1200L),
					SpawnEntry.group(SWARM_I, 12, 6, 1400L)),

			wave(13, "Act 2-1: Faster Basics", "Reintroduction at higher speed",
					SpawnEntry.single(BASIC_II, 12, 750L)),

			wave(14, "Act 2-2: Faster Patterns", "Faster movement pressure",
					SpawnEntry.single(BASIC_II, 8, 700L),
					SpawnEntry.single(ZIGZAG_II, 8, 800L)),

			wave(15, "Act 2-3: Closers", "Aggressive closers",
					SpawnEntry.single(DASHER_II, 7, 850L),
					SpawnEntry.single(ZIGZAG_II, 7, 800L)),

			wave(16, "Act 2-4: Smarter Shots", "Smarter projectiles",
					SpawnEntry.single(SHOOTER_II, 7, 900L),
					SpawnEntry.single(HOMING_II, 6, 1100L)),

			wave(17, "Act 2-5: Dense Fire", "Dense projectile wave",
					SpawnEntry.single(BASIC_II, 8, 650L),
					SpawnEntry.single(SHOOTER_II, 6, 850L),
					SpawnEntry.single(SPREAD_II, 6, 1050L)),

			wave(18, "Act 2-6: Larger Swarm", "Larger swarm groups than Act 1",
					SpawnEntry.group(SWARM_II, 36, 9, 1100L)),

			wave(19, "Act 2-7: Durable Explosions", "High durability plus explosion hazards",
					SpawnEntry.single(TANK_II, 5, 1300L),
					SpawnEntry.single(BOMBER_II, 6, 1050L)),

			wave(20, "Act 2-8: Disorientation", "Orbiters and ghosts disrupt tracking",
					SpawnEntry.single(ORBITER_II, 6, 1050L),
					SpawnEntry.single(GHOST_II, 6, 1050L)),

			wave(21, "Act 2-9: Flood And Dash", "Splits flood screen plus dashers close in",
					SpawnEntry.single(SPLITTER_II, 6, 1100L),
					SpawnEntry.single(DASHER_II, 8, 800L)),

			wave(22, "Act 2-10: Projectile Trap", "Near-unavoidable projectile wave",
					SpawnEntry.single(HOMING_II, 7, 1000L),
					SpawnEntry.single(SPREAD_II, 7, 1000L)),

			wave(23, "Act 2-11: Hidden Swarm", "Invisible threats amid chaos",
					SpawnEntry.single(GHOST_II, 7, 950L),
					SpawnEntry.group(SWARM_II, 27, 9, 1200L)),

			wave(24, "Act 2-12: Level II Gauntlet", "All Level II types before the boss",
					SpawnEntry.single(BASIC_II, 5, 650L),
					SpawnEntry.single(ZIGZAG_II, 5, 750L),
					SpawnEntry.single(SHOOTER_II, 4, 850L),
					SpawnEntry.single(DASHER_II, 5, 750L),
					SpawnEntry.single(SPREAD_II, 4, 950L),
					SpawnEntry.single(TANK_II, 3, 1250L),
					SpawnEntry.single(SPLITTER_II, 4, 1050L),
					SpawnEntry.single(ORBITER_II, 4, 1050L),
					SpawnEntry.single(BOMBER_II, 4, 1100L),
					SpawnEntry.single(GHOST_II, 4, 1000L),
					SpawnEntry.single(HOMING_II, 4, 1000L),
					SpawnEntry.group(SWARM_II, 18, 9, 1200L)),

			wave(25, "Act 3-1: Maximum Speed", "Everything is faster now",
					SpawnEntry.single(BASIC_III, 10, 600L),
					SpawnEntry.single(ZIGZAG_III, 10, 700L)),

			wave(26, "Act 3-2: Fast Aggression", "Fast and aggressive",
					SpawnEntry.single(DASHER_III, 10, 700L),
					SpawnEntry.single(SHOOTER_III, 8, 800L)),

			wave(27, "Act 3-3: Brutal Density", "Brutal projectile density",
					SpawnEntry.single(SPREAD_III, 8, 850L),
					SpawnEntry.single(HOMING_III, 8, 900L)),

			wave(28, "Act 3-4: Wall Of Health", "Extremely tanky - tests sustained damage",
					SpawnEntry.single(TANK_III, 8, 1150L)),

			wave(29, "Act 3-5: Filled Screen", "Screen completely filled",
					SpawnEntry.group(SWARM_III, 48, 12, 1000L),
					SpawnEntry.single(ORBITER_III, 6, 950L)),

			wave(30, "Act 3-6: Invisible Rush", "Fast invisible threats",
					SpawnEntry.single(GHOST_III, 8, 900L),
					SpawnEntry.single(DASHER_III, 9, 700L)),

			wave(31, "Act 3-7: Blast Armor", "Explosion spam plus durability",
					SpawnEntry.single(BOMBER_III, 8, 850L),
					SpawnEntry.single(TANK_III, 5, 1100L)),

			wave(32, "Act 3-8: Peak Chaos", "Most chaotic wave yet",
					SpawnEntry.single(SPLITTER_III, 7, 900L),
					SpawnEntry.group(SWARM_III, 36, 12, 1000L)),

			wave(33, "Act 3-9: Projectile Hell", "Pure projectile hell",
					SpawnEntry.single(SHOOTER_III, 7, 750L),
					SpawnEntry.single(SPREAD_III, 7, 800L),
					SpawnEntry.single(HOMING_III, 7, 850L)),

			wave(34, "Act 3-10: Movement Nightmare", "Ghosts, orbiters, and zigzags",
					SpawnEntry.single(GHOST_III, 7, 850L),
					SpawnEntry.single(ORBITER_III, 7, 900L),
					SpawnEntry.single(ZIGZAG_III, 8, 650L)),

			wave(35, "Act 3-11: Half Gauntlet", "All Level III types at half spawn",
					SpawnEntry.single(BASIC_III, 3, 550L),
					SpawnEntry.single(ZIGZAG_III, 3, 600L),
					SpawnEntry.single(SHOOTER_III, 3, 700L),
					SpawnEntry.single(DASHER_III, 3, 650L),
					SpawnEntry.single(SPREAD_III, 2, 800L),
					SpawnEntry.single(TANK_III, 2, 1000L),
					SpawnEntry.single(SPLITTER_III, 2, 900L),
					SpawnEntry.single(ORBITER_III, 2, 850L),
					SpawnEntry.single(BOMBER_III, 2, 900L),
					SpawnEntry.single(GHOST_III, 2, 850L),
					SpawnEntry.single(HOMING_III, 2, 850L),
					SpawnEntry.group(SWARM_III, 12, 12, 1000L)),

			wave(36, "Act 3-12: Final Gauntlet", "All Level III types at full spawn",
					SpawnEntry.single(BASIC_III, 5, 500L),
					SpawnEntry.single(ZIGZAG_III, 5, 575L),
					SpawnEntry.single(SHOOTER_III, 4, 675L),
					SpawnEntry.single(DASHER_III, 5, 600L),
					SpawnEntry.single(SPREAD_III, 4, 750L),
					SpawnEntry.single(TANK_III, 3, 950L),
					SpawnEntry.single(SPLITTER_III, 4, 850L),
					SpawnEntry.single(ORBITER_III, 4, 800L),
					SpawnEntry.single(BOMBER_III, 4, 850L),
					SpawnEntry.single(GHOST_III, 4, 800L),
					SpawnEntry.single(HOMING_III, 4, 800L),
					SpawnEntry.group(SWARM_III, 24, 12, 950L))
	);

	private WaveCatalog() {
	}

	public static WaveDefinition getWave(int waveNumber) {
		if (waveNumber <= FINAL_STORY_WAVE) {
			return STORY_WAVES.get(waveNumber - 1);
		}

		return endlessWave(waveNumber);
	}

	public static BossType getBossAfterWave(int waveNumber) {
		return switch (waveNumber) {
			case 12 -> BossType.SWARM_QUEEN;
			case 24 -> BossType.FORTRESS;
			case 36 -> BossType.SPLITTER_KING;
			default -> null;
		};
	}

	public static boolean shouldTriggerBossAfter(int waveNumber) {
		return waveNumber > 0 && waveNumber % 12 == 0 && waveNumber <= FINAL_STORY_WAVE;
	}

	public static boolean isFinalStoryWave(int waveNumber) {
		return waveNumber == FINAL_STORY_WAVE;
	}

	private static WaveDefinition wave(int number, String label, String notes, SpawnEntry... spawns) {
		return new WaveDefinition(number, label, List.of(spawns), 900L, notes, null, false);
	}

	private static WaveDefinition endlessWave(int waveNumber) {
		int cycle = ((waveNumber - FINAL_STORY_WAVE - 1) / 12) + 1;
		int slot = ((waveNumber - FINAL_STORY_WAVE - 1) % 12) + 1;

		return new WaveDefinition(
				waveNumber,
				"Endless " + cycle + "-" + slot,
				List.of(
						SpawnEntry.single(BASIC_III, 6 + cycle, 550L),
						SpawnEntry.single(ZIGZAG_III, 5 + cycle, 650L),
						SpawnEntry.single(SHOOTER_III, 4 + cycle, 750L),
						SpawnEntry.single(DASHER_III, 4 + cycle, 650L),
						SpawnEntry.single(SPREAD_III, 3 + cycle, 800L),
						SpawnEntry.single(HOMING_III, 3 + cycle, 850L),
						SpawnEntry.group(SWARM_III, 12 + cycle * 6, 12 + cycle, 1000L)
				),
				800L,
				"Endless mixed Level III wave - scaling cycle " + cycle,
				null,
				true
		);
	}
}