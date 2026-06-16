package com.lobsterchops.infinitesquareshooter.config.stats;

import java.util.EnumSet;
import java.util.Set;

import com.lobsterchops.infinitesquareshooter.config.types.BehaviourFlag;

/**
 * Immutable configuration for a single phase of a boss fight.
 *
 * <p>Bosses are composed of an ordered list of {@code BossPhase} objects held by {@link BossStats}.
 * When a phase's HP drops to the transition threshold, the encounter advances to the next phase.
 * Single-phase bosses simply provide a list of one.</p>
 *
 * @param phaseNumber     1-indexed phase identifier; Phase 1 is always the opening phase.
 * @param hp              Hit-point pool belonging exclusively to this phase.
 * @param hpTransitionPct Fraction of this phase's HP remaining when the next phase triggers;
 *                        {@code 1.0} means the phase must be fully depleted before advancing.
 * @param behaviours      Set of {@link com.lobsterchops.infinitesquareshooter.config.types.BehaviourFlag}
 *                        values that are active while this phase is in progress.
 * @param projectile      Projectile configuration used during this phase;
 *                        {@code null} if the boss does not shoot in this phase.
 * @param speedMultiplier Multiplier applied on top of the boss's base movement speed;
 *                        {@code 1.0} leaves speed unchanged.
 */
public record BossPhase(int phaseNumber, int hp, float hpTransitionPct, Set<BehaviourFlag> behaviours,
		ProjectileStats projectile, float speedMultiplier) {

	public BossPhase {
		behaviours = behaviours.isEmpty() ? EnumSet.noneOf(BehaviourFlag.class) : EnumSet.copyOf(behaviours);
	}

}