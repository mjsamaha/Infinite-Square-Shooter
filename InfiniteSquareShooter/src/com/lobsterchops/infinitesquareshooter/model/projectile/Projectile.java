package com.lobsterchops.infinitesquareshooter.model.projectile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.lobsterchops.infinitesquareshooter.combat.DamageSource;
import com.lobsterchops.infinitesquareshooter.combat.Team;
import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.model.Damageable;
import com.lobsterchops.infinitesquareshooter.model.Entity;
import com.lobsterchops.infinitesquareshooter.model.UpdateContext;
import com.lobsterchops.infinitesquareshooter.render.RenderLayer;
import com.lobsterchops.infinitesquareshooter.utils.SpriteRegistry;

public class Projectile extends Entity implements DamageSource {

	private static final long DEFAULT_LIFESPAN_MS = 3000L;

	private final int damage;
	private final ProjectileOwner owner;
	private final boolean homing;
	private final float homingTurnRate;
	private final long lifespanMs;

	private long spawnedAt;

	public Projectile(Vector2 position, Vector2 velocity, int damage, ProjectileOwner owner) {
		this(position, velocity, damage, owner, false, 0f, DEFAULT_LIFESPAN_MS, 6f);
	}

	public Projectile(Vector2 position, Vector2 velocity, int damage, ProjectileOwner owner,
			boolean homing, float homingTurnRate) {
		this(position, velocity, damage, owner, homing, homingTurnRate, DEFAULT_LIFESPAN_MS, 6f);
	}

	public Projectile(Vector2 position, Vector2 velocity, int damage, ProjectileOwner owner,
			boolean homing, float homingTurnRate, long lifespanMs, float size) {
		super(position, size, size);
		this.damage = damage;
		this.owner = owner;
		this.homing = homing;
		this.homingTurnRate = homingTurnRate;
		this.lifespanMs = lifespanMs;
		setVelocity(velocity);
	}

	@Override
	public void update(UpdateContext context) {
		if (spawnedAt == 0L) {
			spawnedAt = context.elapsedMillis();
		}

		if (isExpired(context)) {
			markInactive();
			return;
		}

		if (homing && owner == ProjectileOwner.ENEMY && context.world().getPlayer() != null) {
			steerTowardPlayer(context);
		}

		super.update(context);

		if (isOffscreen()) {
			markInactive();
		}
	}

	@Override
	public void render(Graphics2D g2) {
	    if (getOwner() == ProjectileOwner.PLAYER) {
	        BufferedImage sprite = SpriteRegistry.forPlayerProjectile();

	        if (sprite != null) {
	            g2.drawImage(
	                sprite,
	                Math.round(getBounds().x()),
	                Math.round(getBounds().y()),
	                Math.round(getBounds().width()),
	                Math.round(getBounds().height()),
	                null
	            );
	            return;
	        }
	    }

	    if (getOwner() == ProjectileOwner.ENEMY && !isHoming()) {
	        BufferedImage sprite = SpriteRegistry.forEnemyProjectile();

	        if (sprite != null) {
	            g2.drawImage(
	                sprite,
	                Math.round(getBounds().x()),
	                Math.round(getBounds().y()),
	                Math.round(getBounds().width()),
	                Math.round(getBounds().height()),
	                null
	            );
	            return;
	        }
	    }

	    // Fallback for homing, neutral, or any missing sprite
	    g2.setColor(ProjectileVisualResolver.colorFor(this));
	    g2.fillOval(
	        Math.round(getBounds().x()),
	        Math.round(getBounds().y()),
	        Math.round(getBounds().width()),
	        Math.round(getBounds().height())
	    );
	}

	@Override
	public Team getTeam() {
		return owner.getTeam();
	}

	@Override
	public int getDamage() {
		return damage;
	}

	@Override
	public void onDamageApplied(Damageable target, UpdateContext context) {
		markInactive();
	}

	@Override
	public RenderLayer getRenderLayer() {
		return RenderLayer.PROJECTILES;
	}

	private void steerTowardPlayer(UpdateContext context) {
		Vector2 targetDirection = getPosition()
				.directionTo(context.world().getPlayer().getPosition());

		Vector2 currentDirection = getVelocity().normalized();
		Vector2 blendedDirection = currentDirection
				.multiply(1f - homingTurnRate)
				.add(targetDirection.multiply(homingTurnRate))
				.normalized();

		setVelocity(blendedDirection.multiply(getVelocity().length()));
	}

	private boolean isExpired(UpdateContext context) {
		return lifespanMs > 0 && context.elapsedMillis() - spawnedAt >= lifespanMs;
	}

	private boolean isOffscreen() {
		Vector2 position = getPosition();

		return position.x() < -40
				|| position.x() > com.lobsterchops.infinitesquareshooter.config.ScreenConfig.WIDTH + 40
				|| position.y() < -40
				|| position.y() > com.lobsterchops.infinitesquareshooter.config.ScreenConfig.HEIGHT + 40;
	}

	public ProjectileOwner getOwner() {
		return owner;
	}

	public boolean isHoming() {
		return homing;
	}

	public float getHomingTurnRate() {
		return homingTurnRate;
	}

	public long getLifespanMs() {
		return lifespanMs;
	}
}