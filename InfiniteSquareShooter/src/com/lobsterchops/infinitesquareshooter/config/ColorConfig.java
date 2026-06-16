package com.lobsterchops.infinitesquareshooter.config;

import java.awt.Color;

public final class ColorConfig {

	// Base
	public static final Color BLACK = new Color(0, 0, 0);
	public static final Color WHITE = new Color(255, 255, 255);
	public static final Color DARK_GREY = new Color(30, 30, 30);

	// UI
	public static final Color HUD_TEXT = WHITE;
	public static final Color HUD_BACKGROUND = new Color(0, 0, 0, 180); // semi-transparent
	public static final Color SCORE_COLOUR = new Color(255, 220, 50); // gold

	// Player
	public static final Color PLAYER = new Color(80, 160, 255); // blue
	public static final Color PLAYER_PROJECTILE = new Color(180, 230, 255); // light blue
	public static final Color PLAYER_INVINCIBLE = new Color(255, 255, 255); // flash white

	// Enemy
	public static final Color ENEMY_BASIC = new Color(220, 80, 80); // red
	public static final Color ENEMY_ZIGZAG = new Color(240, 140, 40); // orange
	public static final Color ENEMY_SHOOTER = new Color(200, 60, 200); // purple
	public static final Color ENEMY_DASHER = new Color(60, 200, 220); // cyan
	public static final Color ENEMY_SPREAD = new Color(230, 200, 40); // yellow
	public static final Color ENEMY_TANK = new Color(100, 180, 80); // green
	public static final Color ENEMY_SPLITTER = new Color(200, 100, 40); // burnt orange
	public static final Color ENEMY_ORBITER = new Color(160, 80, 220); // violet
	public static final Color ENEMY_BOMBER = new Color(240, 60, 60); // bright red
	public static final Color ENEMY_GHOST = new Color(180, 180, 255); // pale blue (visible phase)
	public static final Color ENEMY_HOMING = new Color(240, 100, 160); // pink
	public static final Color ENEMY_SWARM = new Color(180, 220, 80); // lime

	// Boss Accent Colors
	public static final Color BOSS_FORTRESS = new Color(120, 160, 120); // military green
	public static final Color BOSS_SWARM_QUEEN = new Color(200, 160, 40); // amber
	public static final Color BOSS_PHANTOM = new Color(140, 100, 220); // deep violet
	public static final Color BOSS_SPLITTER_KING = new Color(220, 80, 60); // deep red
	public static final Color BOSS_MIMIC = new Color(80, 200, 200); // teal

	// Projectiles
	public static final Color PROJECTILE_ENEMY = new Color(255, 80, 80);
	public static final Color PROJECTILE_HOMING = new Color(255, 120, 200);
	public static final Color PROJECTILE_BOMB = new Color(255, 180, 40);
	public static final Color EXPLOSION = new Color(255, 140, 0);

	public ColorConfig() {

	}
}
