package com.lobsterchops.infinitesquareshooter.model;

import java.awt.Graphics2D;

import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.model.entity.Player;

public abstract class Pickup extends Entity implements Collectible {

	private boolean collected;

	protected Pickup(Vector2 position, float width, float height) {
		super(position, width, height);
	}

	@Override
	public void update(UpdateContext context) {
		super.update(context);
	}

	@Override
	public boolean canBeCollectedBy(Player player) {
		return player != null && !player.isDead() && !collected;
	}

	@Override
	public final void collect(Player player, GameWorld world) {
		if (!canBeCollectedBy(player)) {
			return;
		}

		collected = true;
		applyEffect(player, world);
		markInactive();
	}

	@Override
	public boolean isCollected() {
		return collected;
	}

	protected abstract void applyEffect(Player player, GameWorld world);

	@Override
	public abstract void render(Graphics2D g2);
}