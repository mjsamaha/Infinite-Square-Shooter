package com.lobsterchops.infinitesquareshooter.config.stats;

import java.util.EnumSet;
import java.util.Set;

import com.lobsterchops.infinitesquareshooter.config.types.BehaviourFlag;

public record BossPhase(int phaseNumber, int hp, float hpTransitionPct, Set<BehaviourFlag> behaviours,
		ProjectileStats projectile, float speedMultiplier) {

	public BossPhase {
		behaviours = behaviours.isEmpty() ? EnumSet.noneOf(BehaviourFlag.class) : EnumSet.copyOf(behaviours);
	}

}