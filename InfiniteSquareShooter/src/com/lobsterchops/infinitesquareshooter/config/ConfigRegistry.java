package com.lobsterchops.infinitesquareshooter.config;

import java.util.EnumMap;
import java.util.Map;

import com.lobsterchops.infinitesquareshooter.config.stats.BossStats;
import com.lobsterchops.infinitesquareshooter.config.stats.EnemyStats;
import com.lobsterchops.infinitesquareshooter.config.stats.PlayerStats;
import com.lobsterchops.infinitesquareshooter.config.stats.ProjectileStats;
import com.lobsterchops.infinitesquareshooter.config.types.BossType;
import com.lobsterchops.infinitesquareshooter.config.types.EnemyType;

/**
 * Central access point for all game configuration.
 *
 * <p>
 * All game logic must retrieve stats exclusively through this class.
 * Constructing {@link EnemyStats}, {@link BossStats}, or {@link PlayerStats}
 * directly inside game code is discouraged — doing so bypasses the registry and
 * breaks the single source of truth that makes the config layer easy to
 * maintain and extend.
 * </p>
 *
 * <p>
 * The backing source is currently the
 * {@link com.lobsterchops.infinitesquareshooter.config.types.EnemyType} and
 * {@link com.lobsterchops.infinitesquareshooter.config.types.BossType}
 * enumerations. Swapping to an external source (JSON file, database, remote
 * config) requires only changes to this class — no game logic needs to be
 * touched.
 * </p>
 *
 * <p>
 * This class is thread-safe. All internal maps are populated once during class
 * initialisation via {@code static} blocks and are never mutated thereafter.
 * </p>
 *
 * <p>
 * Typical usage:
 * </p>
 * 
 * <pre>{@code
 * EnemyStats stats = ConfigRegistry.enemy(EnemyType.TANK_II);
 * BossStats boss = ConfigRegistry.boss(BossType.FORTRESS);
 * PlayerStats player = ConfigRegistry.player();
 * }</pre>
 */
public final class ConfigRegistry {

	// Enemy Map
	private static final Map<EnemyType, EnemyStats> ENEMY_MAP;

	static {
		ENEMY_MAP = new EnumMap<>(EnemyType.class);
		for (EnemyType type : EnemyType.values()) {
			ENEMY_MAP.put(type, type.getStats());
		}
	}

	/** Returns the full enemy stats map. Unmodifiable view. */
	public static Map<EnemyType, EnemyStats> enemies() {
		return java.util.Collections.unmodifiableMap(ENEMY_MAP);
	}

	/** Returns stats for a specific enemy type. */
	public static EnemyStats enemy(EnemyType type) {
		return ENEMY_MAP.get(type);
	}

	// Boss Map
	private static final Map<BossType, BossStats> BOSS_MAP;

	static {
		BOSS_MAP = new EnumMap<>(BossType.class);
		for (BossType type : BossType.values()) {
			BOSS_MAP.put(type, type.getStats());
		}
	}

	/** Returns the full boss stats map. Unmodifiable view. */
	public static Map<BossType, BossStats> bosses() {
		return java.util.Collections.unmodifiableMap(BOSS_MAP);
	}

	/** Returns stats for a specific boss type. */
	public static BossStats boss(BossType type) {
		return BOSS_MAP.get(type);
	}

	// Player Map
	private static final PlayerStats PLAYER_STATS = new PlayerStats(3, // startingLives
			4.0f, // moveSpeed
			1500L, // invincibilityMs
			ProjectileStats.single(10f, 1, 150L), // default projectile
			5 // maxLives (power-up cap)
	);

	/** Returns the player's base stats. */
	public static PlayerStats player() {
		return PLAYER_STATS;
	}

	private ConfigRegistry() {
	}

}
