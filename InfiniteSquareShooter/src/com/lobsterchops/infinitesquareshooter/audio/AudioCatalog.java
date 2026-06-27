package com.lobsterchops.infinitesquareshooter.audio;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public final class AudioCatalog {
	
	private static final Map<SoundType, SoundDefinition> DEFINITIONS;
	
	static {
		EnumMap<SoundType, SoundDefinition> defs = new EnumMap<>(SoundType.class);

		/**
		 * How to register a sound:
		 * register(defs, SoundType.SOME_SOUND_TYPE, SoundDefinition.sfx("/audio/some_sound.wav", 0.75f, 3));
		 */

		// Player Related
		register(defs, SoundType.PLAYER_SHOOT, SoundDefinition.sfx("/sfx/player_shot.wav", 0.60f, 4)); // TODO
		register(defs, SoundType.PLAYER_HIT, SoundDefinition.sfx("/audio/background.wav", 0.90f, 2)); // TODO

		// Enemy Related
		register(defs, SoundType.ENEMY_SPAWN, SoundDefinition.sfx("/audio/background.wav", 0.50f, 6)); // TODO
		register(defs, SoundType.ENEMY_SHOOT, SoundDefinition.sfx("/audio/background.wav", 0.50f, 6)); // TODO
		register(defs, SoundType.ENEMY_HIT, SoundDefinition.sfx("/audio/background.wav", 0.50f, 6)); // TODO
		register(defs, SoundType.ENEMY_DEATH, SoundDefinition.sfx("/audio/background.wav", 0.70f, 6)); // TODO
		register(defs, SoundType.ENEMY_SPLIT, SoundDefinition.sfx("/audio/background.wav", 0.60f, 4)); // TODO
		register(defs, SoundType.ENEMY_DASH, SoundDefinition.sfx("/audio/background.wav", 0.50f, 4)); // TODO

		// Boss Related
		register(defs, SoundType.BOSS_SPAWN, SoundDefinition.sfx("/audio/background.wav", 1.00f, 1)); // TODO
		register(defs, SoundType.BOSS_PHASE_CHANGE, SoundDefinition.sfx("/audio/background.wav", 1.00f, 1)); // TODO
		register(defs, SoundType.BOSS_DEATH, SoundDefinition.sfx("/audio/background.wav", 1.00f, 1)); // TODO

		// Gameplay Related
		register(defs, SoundType.COMBO_TICK, SoundDefinition.sfx("/audio/background.wav", 0.50f, 4)); // TODO
		register(defs, SoundType.WAVE_START, SoundDefinition.sfx("/audio/background.wav", 0.80f, 1)); // TODO
		register(defs, SoundType.WAVE_END, SoundDefinition.sfx("/audio/background.wav", 0.80f, 1)); // TODO
		register(defs, SoundType.GAME_OVER, SoundDefinition.sfx("/audio/background.wav", 1.00f, 1)); // TODO

		// UI Related
		register(defs, SoundType.UI_CLICK, SoundDefinition.ui("/audio/background.wav", 0.40f)); // TODO
		register(defs, SoundType.PAUSE_INTERACTION, SoundDefinition.ui("/audio/background.wav", 0.40f)); // TODO
		register(defs, SoundType.UI_TRANSITION, SoundDefinition.ui("/audio/background.wav", 0.40f)); // TODO

		// Music
		register(defs, SoundType.MUSIC_SPLASH_SCREEN, SoundDefinition.music("/audio/background.wav", 0.75f)); // TODO
		register(defs, SoundType.MUSIC_MENU, SoundDefinition.music("/audio/background.wav", 0.75f)); // TODO
		register(defs, SoundType.MUSIC_GAMEPLAY, SoundDefinition.music("/audio/gameplay_music_two.wav", 0.75f));
		register(defs, SoundType.MUSIC_BOSS, SoundDefinition.music("/audio/background.wav", 0.75f)); // TODO

		// all enum values must be registered
		for (SoundType type : SoundType.values()) {
			if (!defs.containsKey(type)) {
				throw new IllegalStateException("SoundType " + type + " is not registered in AudioCatalog");
			}
		}

		DEFINITIONS = Collections.unmodifiableMap(defs);
	}
	
	private AudioCatalog() {
		// Prevent instantiation
	}

	public static SoundDefinition get(SoundType type) {
		SoundDefinition def = DEFINITIONS.get(type);
		if (def == null) {
			throw new IllegalArgumentException("SoundType " + type + " is not registered in AudioCatalog");
		}
		return def;
	}

	public static Map<SoundType, SoundDefinition> definitions() {
		return DEFINITIONS;
	}

	public static boolean has(SoundType type) {
		return DEFINITIONS.containsKey(type);
	}

	public static void register(Map<SoundType, SoundDefinition> defs, SoundType type, SoundDefinition def) {
		if (defs.put(type, def) != null) {
			throw new IllegalStateException("SoundType " + type + " is already registered in AudioCatalog");
		}
	}
}
