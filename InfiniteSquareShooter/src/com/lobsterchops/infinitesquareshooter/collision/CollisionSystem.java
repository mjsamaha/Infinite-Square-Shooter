package com.lobsterchops.infinitesquareshooter.collision;

import java.util.ArrayList;
import java.util.List;

import com.lobsterchops.infinitesquareshooter.combat.DamageSource;
import com.lobsterchops.infinitesquareshooter.combat.DamageSystem;
import com.lobsterchops.infinitesquareshooter.combat.Team;
import com.lobsterchops.infinitesquareshooter.combat.TeamMember;
import com.lobsterchops.infinitesquareshooter.model.Collectible;
import com.lobsterchops.infinitesquareshooter.model.Collidable;
import com.lobsterchops.infinitesquareshooter.model.Damageable;
import com.lobsterchops.infinitesquareshooter.model.GameObject;
import com.lobsterchops.infinitesquareshooter.model.GameWorld;
import com.lobsterchops.infinitesquareshooter.model.UpdateContext;
import com.lobsterchops.infinitesquareshooter.model.entity.Player;

public class CollisionSystem {

	private final DamageSystem damageSystem;

	public CollisionSystem(DamageSystem damageSystem) {
		this.damageSystem = damageSystem;
	}

	public void update(UpdateContext context) {
		GameWorld world = context.world();
		Player player = world.getPlayer();

		if (player == null || player.isDead()) {
			return;
		}

		List<GameObject> objects = new ArrayList<>(world.getObjects());

		resolveProjectileHits(objects, context);
		resolveEnemyContact(objects, player, context);
		resolvePickupCollection(objects, player, world);
	}

	private void resolveProjectileHits(List<GameObject> objects, UpdateContext context) {
		for (GameObject sourceObject : objects) {
			if (!sourceObject.isActive()) {
				continue;
			}

			if (!(sourceObject instanceof DamageSource source)) {
				continue;
			}

			if (!(sourceObject instanceof Collidable sourceCollider)) {
				continue;
			}

			for (GameObject targetObject : objects) {
				if (sourceObject == targetObject || !targetObject.isActive()) {
					continue;
				}

				if (!(targetObject instanceof Damageable target)) {
					continue;
				}

				if (!(targetObject instanceof Collidable targetCollider)) {
					continue;
				}

				if (!canDamage(source, targetObject)) {
					continue;
				}

				if (CollisionDetector.intersects(sourceCollider, targetCollider)) {
					damageSystem.applyDamage(target, source, context);
					break;
				}
			}
		}
	}

	private void resolveEnemyContact(List<GameObject> objects, Player player, UpdateContext context) {
		for (GameObject object : objects) {
			if (!object.isActive() || object == player) {
				continue;
			}

			if (!(object instanceof TeamMember teamMember) || teamMember.getTeam() != Team.ENEMY) {
				continue;
			}

			if (!(object instanceof Collidable collidable)) {
				continue;
			}

			if (object instanceof DamageSource) {
				continue;
			}

			if (CollisionDetector.intersects(player, collidable)) {
				damageSystem.applyDamage(player, 1, context);
			}
		}
	}

	private void resolvePickupCollection(List<GameObject> objects, Player player, GameWorld world) {
		for (GameObject object : objects) {
			if (!object.isActive()) {
				continue;
			}

			if (!(object instanceof Collectible collectible)) {
				continue;
			}

			if (CollisionDetector.intersects(player, collectible) && collectible.canBeCollectedBy(player)) {
				collectible.collect(player, world);
			}
		}
	}

	private boolean canDamage(DamageSource source, GameObject targetObject) {
		if (!(targetObject instanceof TeamMember targetTeamMember)) {
			return false;
		}

		return source.isHostileTo(targetTeamMember);
	}
}