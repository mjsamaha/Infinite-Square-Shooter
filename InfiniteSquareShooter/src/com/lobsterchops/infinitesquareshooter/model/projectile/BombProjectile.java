package com.lobsterchops.infinitesquareshooter.model.projectile;

import java.awt.Graphics2D;

import com.lobsterchops.infinitesquareshooter.config.ColorConfig;
import com.lobsterchops.infinitesquareshooter.math.Bounds;
import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.model.UpdateContext;
import com.lobsterchops.infinitesquareshooter.render.RenderLayer;

public class BombProjectile extends Projectile {

	private static final long FUSE_MS = 1400L;
	private static final long EXPLOSION_MS = 180L;
	private static final float BOMB_SIZE = 12f;
	private static final float EXPLOSION_SIZE = 56f;

	private long spawnedAt;
	private boolean exploding;
	private boolean damageApplied;

	public BombProjectile(Vector2 position, Vector2 velocity, int damage) {
		super(position, velocity, damage, ProjectileOwner.ENEMY, false, 0f, 0L, BOMB_SIZE);
	}

	@Override
	public void update(UpdateContext context) {
		if (spawnedAt == 0L) {
			spawnedAt = context.elapsedMillis();
		}

		long age = context.elapsedMillis() - spawnedAt;

		if (!exploding && age >= FUSE_MS) {
			exploding = true;
			setVelocity(Vector2.ZERO);
		}

		if (exploding && age >= FUSE_MS + EXPLOSION_MS) {
			markInactive();
			return;
		}

		if (!exploding) {
			super.update(context);
		}
	}

	@Override
	public void render(Graphics2D g2) {
		if (exploding) {
			g2.setColor(ColorConfig.EXPLOSION);
			g2.fillOval(
					Math.round(getPosition().x() - EXPLOSION_SIZE / 2f),
					Math.round(getPosition().y() - EXPLOSION_SIZE / 2f),
					Math.round(EXPLOSION_SIZE),
					Math.round(EXPLOSION_SIZE)
			);
			return;
		}

		g2.setColor(ColorConfig.PROJECTILE_BOMB);
		g2.fillOval(
				Math.round(getBounds().x()),
				Math.round(getBounds().y()),
				Math.round(getBounds().width()),
				Math.round(getBounds().height())
		);
	}

	@Override
	public Bounds getBounds() {
		if (exploding) {
			return Bounds.fromCenter(getPosition(), EXPLOSION_SIZE, EXPLOSION_SIZE);
		}

		return super.getBounds();
	}

	@Override
	public void onDamageApplied(com.lobsterchops.infinitesquareshooter.model.Damageable target,
			UpdateContext context) {
		if (!exploding) {
			exploding = true;
			setVelocity(Vector2.ZERO);
		}

		if (damageApplied) {
			return;
		}

		damageApplied = true;
	}

	@Override
	public RenderLayer getRenderLayer() {
		return RenderLayer.PROJECTILES;
	}
	
	@Override
	public int getDamage() {
		return damageAlreadyApplied() ? 0 : super.getDamage();
	}

	private boolean damageAlreadyApplied() {
		return damageApplied;
	}
}