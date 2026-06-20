package com.lobsterchops.infinitesquareshooter.combat;

import com.lobsterchops.infinitesquareshooter.model.Damageable;
import com.lobsterchops.infinitesquareshooter.model.GameObject;
import com.lobsterchops.infinitesquareshooter.model.UpdateContext;

public class DamageSystem {

	public void applyDamage(Damageable target, DamageSource source, UpdateContext context) {
		if (target == null || source == null || target.isDead()) {
			return;
		}

		target.takeDamage(source.getDamage(), context);
		source.onDamageApplied(target, context);

		awardScoreIfKilled(target, context);
	}

	public void applyDamage(Damageable target, int damage, UpdateContext context) {
		if (target == null || target.isDead()) {
			return;
		}

		target.takeDamage(damage, context);

		awardScoreIfKilled(target, context);
	}

	private void awardScoreIfKilled(Damageable target, UpdateContext context) {
		if (!target.isDead()) {
			return;
		}

		if (target instanceof ScoreValue scoreValue) {
			context.world().addScore(scoreValue.getScoreValue());
		}

		if (target instanceof GameObject gameObject) {
			gameObject.isActive();
		}
	}
}