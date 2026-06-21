package com.lobsterchops.infinitesquareshooter.audio;

public enum SoundType {
	
	// Player Related
	PLAYER_SHOOT, PLAYER_HIT,
	
	
	// Enemy Related
	ENEMY_SPAWN, ENEMY_SHOOT, ENEMY_HIT, ENEMY_DEATH, ENEMY_SPLIT, ENEMY_DASH,
	
	
	// Boss Related
	BOSS_SPAWN, BOSS_PHASE_CHANGE, BOSS_DEATH,
	
	
	// Gameplay Related
	COMBO_TICK,
	
	WAVE_START, WAVE_END, GAME_OVER,
	
	
	// UI Related
	UI_CLICK, PAUSE_INTERACTION, UI_TRANSITION,
	
	
	// Music
	MUSIC_SPLASH_SCREEN, MUSIC_MENU, MUSIC_GAMEPLAY, MUSIC_BOSS
	

}
