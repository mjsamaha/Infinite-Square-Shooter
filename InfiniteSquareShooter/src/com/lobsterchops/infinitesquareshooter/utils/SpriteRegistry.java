package com.lobsterchops.infinitesquareshooter.utils;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.lobsterchops.infinitesquareshooter.config.SpritePath;
import com.lobsterchops.infinitesquareshooter.config.types.BossType;
import com.lobsterchops.infinitesquareshooter.config.types.EnemyType;
import com.lobsterchops.infinitesquareshooter.config.types.PowerUpType;

/**
 * Loads all sprites once at startup and stores them by key. Returns null for
 * any sprite not yet replaced with real art, so render() methods can fall back
 * to color rectangles.
 *
 * Call SpriteRegistry.load() once from GameContext before the game loop starts.
 */
public final class SpriteRegistry {

	private static final Map<String, BufferedImage> CACHE = new HashMap<>();

	private SpriteRegistry() {
	}

	/** Load all known sprites. Call once during bootstrap. */
	public static void load() {
		// Player
		cache(SpritePath.PLAYER);
		cache(SpritePath.PLAYER_PROJECTILE);
		cache(SpritePath.PLAYER_WHITE);

		// Enemies
		cache(SpritePath.ENEMY_BASIC);
		cache(SpritePath.ENEMY_ZIGZAG);
		cache(SpritePath.ENEMY_SHOOTER);
		cache(SpritePath.ENEMY_SHOOTER_PROJECTILE);
		cache(SpritePath.ENEMY_SHOOTER_PROJECTILE_ONE);
		cache(SpritePath.ENEMY_DASHER);
		cache(SpritePath.ENEMY_SPREAD);
		cache(SpritePath.ENEMY_TANK);
		cache(SpritePath.ENEMY_SPLITTER);
		cache(SpritePath.ENEMY_ORBITER);
		cache(SpritePath.ENEMY_BOMBER);
		cache(SpritePath.ENEMY_GHOST);
		cache(SpritePath.ENEMY_HOMING);
		cache(SpritePath.ENEMY_SWARM);

		// Bosses
		cache(SpritePath.BOSS_SWARM_QUEEN);
		cache(SpritePath.BOSS_FORTRESS);
		cache(SpritePath.BOSS_SPLITTER_KING);
		cache(SpritePath.BOSS_PHANTOM);
		cache(SpritePath.BOSS_MIMIC);
		
		cache(SpritePath.POWERUP_EXTRA_LIFE);
		cache(SpritePath.POWERUP_WEAPON_TIER);
		cache(SpritePath.POWERUP_FIRE_RATE);
		cache(SpritePath.POWERUP_SPEED);
		cache(SpritePath.POWERUP_SCORE_MULTIPLIER);
	}

	/** Returns the sprite for a given enemy type, or null if not loaded yet. */
	public static BufferedImage forEnemy(EnemyType type) {
	    String name = type.name();

	    if (name.startsWith("BASIC"))    return get(SpritePath.ENEMY_BASIC);
	    if (name.startsWith("ZIGZAG"))   return get(SpritePath.ENEMY_ZIGZAG);
	    if (name.startsWith("SHOOTER"))  return get(SpritePath.ENEMY_SHOOTER);
	    if (name.startsWith("DASHER"))   return get(SpritePath.ENEMY_DASHER);
	    if (name.startsWith("SPREAD"))   return get(SpritePath.ENEMY_SPREAD);
	    if (name.startsWith("TANK"))     return get(SpritePath.ENEMY_TANK);
	    if (name.startsWith("SPLITTER")) return get(SpritePath.ENEMY_SPLITTER);
	    if (name.startsWith("ORBITER"))  return get(SpritePath.ENEMY_ORBITER);
	    if (name.startsWith("BOMBER"))   return get(SpritePath.ENEMY_BOMBER);
	    if (name.startsWith("GHOST"))    return get(SpritePath.ENEMY_GHOST);
	    if (name.startsWith("HOMING"))   return get(SpritePath.ENEMY_HOMING);
	    if (name.startsWith("SWARM"))    return get(SpritePath.ENEMY_SWARM);

	    return null;
	}

	/** Returns the sprite for a given boss type, or null if not loaded yet. */
	public static BufferedImage forBoss(BossType type) {
	    return switch (type) {
	        case SWARM_QUEEN   -> get(SpritePath.BOSS_SWARM_QUEEN);
	        case FORTRESS      -> get(SpritePath.BOSS_FORTRESS);
	        case SPLITTER_KING -> get(SpritePath.BOSS_SPLITTER_KING);
	        case PHANTOM       -> get(SpritePath.BOSS_PHANTOM);
	        case MIMIC         -> get(SpritePath.BOSS_MIMIC);
	    };
	}
	
	public static BufferedImage forPowerUp(PowerUpType type) {
	    return switch (type) {
	        case EXTRA_LIFE       -> get(SpritePath.POWERUP_EXTRA_LIFE);
	        case WEAPON_TIER      -> get(SpritePath.POWERUP_WEAPON_TIER);
	        case FIRE_RATE        -> get(SpritePath.POWERUP_FIRE_RATE);
	        case SPEED            -> get(SpritePath.POWERUP_SPEED);
	        case SCORE_MULTIPLIER -> get(SpritePath.POWERUP_SCORE_MULTIPLIER);
	    };
	}

	/** Returns the player sprite, or null if not loaded yet. */
	public static BufferedImage forPlayer(boolean invincible) {
	    return invincible ? get(SpritePath.PLAYER_WHITE) : get(SpritePath.PLAYER);
	}

	public static BufferedImage forPlayerProjectile() {
		return get(SpritePath.PLAYER_PROJECTILE);
	}

	public static BufferedImage forPlayerWhite() {
		return get(SpritePath.PLAYER_WHITE);
	}
	
	public static BufferedImage forEnemyProjectile() {
	    return get(SpritePath.ENEMY_SHOOTER_PROJECTILE);
	}
	


	private static void cache(String path) {
		CACHE.put(path, ResourceLoader.loadImage(path));
	}

	private static BufferedImage get(String path) {
		return CACHE.get(path); // null if file wasn't found — caller falls back to color
	}
}