package com.lobsterchops.infinitesquareshooter.config.types;

public enum BehaviourFlag {

	MOVES_STRAIGHT, MOVES_ZIGZAG, MOVES_CIRCULAR, // Orbiter
	CAN_DASH, CAN_TELEPORT, // Phantom boss

	SHOOTS_SINGLE, SHOOTS_SPREAD, SHOOTS_HOMING, DROPS_BOMBS,

	CAN_TURN_INVISIBLE,

	SPLITS_ON_DEATH, // Splitter boss

	SPAWNS_MINIONS, // Sqarm Queen boss

	MIRRORS_PLAYER, // Mimic boss
	HAS_PHASES, // Multi-phase bosses
	HAS_SHIELD, // Shield must be broken first
	TURRET_PROTECTED, // Fortress — core immune until turrets gone

}
