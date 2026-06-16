package com.lobsterchops.infinitesquareshooter.config.types;

/**
 * Flags that describe the active behaviours of any enemy or boss entity.
 *
 * <p>Each flag represents a single, discrete capability or movement pattern. Entities
 * carry an {@code EnumSet<BehaviourFlag>} inside their stats record, giving O(1) membership
 * checks with no boxing overhead. Multiple flags can be combined freely to compose
 * complex behaviour profiles.</p>
 *
 * <p>To introduce a new behaviour, add one constant here and handle it in the relevant
 * AI or rendering system. No other config files need to change.</p>
 */
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
