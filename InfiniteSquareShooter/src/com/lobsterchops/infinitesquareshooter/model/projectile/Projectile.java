package com.lobsterchops.infinitesquareshooter.model.projectile;

import java.awt.Color;
import java.awt.Graphics2D;

import com.lobsterchops.infinitesquareshooter.config.ColorConfig;
import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.model.Entity;
import com.lobsterchops.infinitesquareshooter.model.GameWorld;

public class Projectile extends Entity {

	private final int damage;
	private final ProjectileOwner owner;

	public Projectile(Vector2 position, Vector2 velocity, int damage, ProjectileOwner owner) {
		super(position, 6f, 6f);
		this.damage = damage;
		this.owner = owner;
		setVelocity(velocity);
	}

	@Override
	public void update(GameWorld world) {
		super.update(world);

		Vector2 position = getPosition();

		if (position.x() < -20 || position.x() > com.lobsterchops.infinitesquareshooter.config.ScreenConfig.WIDTH + 20
				|| position.y() < -20
				|| position.y() > com.lobsterchops.infinitesquareshooter.config.ScreenConfig.HEIGHT + 20) {
			markInactive();
		}
	}

	@Override
	public void render(Graphics2D g2) {
		Color color = owner == ProjectileOwner.PLAYER ? ColorConfig.PLAYER_PROJECTILE : ColorConfig.PROJECTILE_ENEMY;

		g2.setColor(color);
		g2.fillOval(Math.round(getBounds().x()), Math.round(getBounds().y()), Math.round(getBounds().width()),
				Math.round(getBounds().height()));
	}

	public int getDamage() {
		return damage;
	}

	public ProjectileOwner getOwner() {
		return owner;
	}
}