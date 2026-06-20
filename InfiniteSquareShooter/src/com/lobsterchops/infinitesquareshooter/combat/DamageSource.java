package com.lobsterchops.infinitesquareshooter.combat;

import com.lobsterchops.infinitesquareshooter.model.Damageable;
import com.lobsterchops.infinitesquareshooter.model.UpdateContext;

public interface DamageSource extends TeamMember {

	int getDamage();

	default void onDamageApplied(Damageable target, UpdateContext context) {
	}
}