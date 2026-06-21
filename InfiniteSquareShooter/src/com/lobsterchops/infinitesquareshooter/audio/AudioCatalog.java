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
		 * register(defs, SoundType.SOME_SOUND_TYPE, SoundDefinition.sfx("audio/some_sound.wav", 0.75f, 3));
		 */
		
		// Music
		
		// UI SFX
		
		
		// Player SFX
		
		
		// Enemy SFX
		
		
		// Boss SFX
		
		
		// Score/Combo SFX
		
		
		// Gameflow SFX
		
		
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
	
	public static Map<SoundType, SoundDefinition> definitions(){
		return DEFINITIONS;
	}
	
	public static boolean has(SoundType type) {
		return DEFINITIONS.containsKey(type);
	}
	
	public static void register (Map<SoundType, SoundDefinition> defs, SoundType type, SoundDefinition def){
		if (defs.put(type,  def) != null) {
			throw new IllegalStateException("SoundType " + type + " is already registered in AudioCatalog");
		}
	}

}
