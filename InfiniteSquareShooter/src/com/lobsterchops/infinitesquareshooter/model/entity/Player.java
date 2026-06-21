package com.lobsterchops.infinitesquareshooter.model.entity;

import java.awt.Graphics2D;

import com.lobsterchops.infinitesquareshooter.combat.Team;
import com.lobsterchops.infinitesquareshooter.combat.TeamMember;
import com.lobsterchops.infinitesquareshooter.config.ColorConfig;
import com.lobsterchops.infinitesquareshooter.config.ConfigRegistry;
import com.lobsterchops.infinitesquareshooter.config.ScreenConfig;
import com.lobsterchops.infinitesquareshooter.config.stats.PlayerStats;
import com.lobsterchops.infinitesquareshooter.input.InputHandler;
import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.model.Damageable;
import com.lobsterchops.infinitesquareshooter.model.Entity;
import com.lobsterchops.infinitesquareshooter.model.GameWorld;
import com.lobsterchops.infinitesquareshooter.model.UpdateContext;
import com.lobsterchops.infinitesquareshooter.state.GameState;

public class Player extends Entity implements Damageable, TeamMember {

	private PlayerStats stats;
	private final InputHandler input;

	private int lives;
	private long lastShotTime;
	private long invincibleUntil;
	private boolean invincible;

	public Player(Vector2 position, InputHandler input) {
		super(position, 32f, 32f);
		this.stats = ConfigRegistry.player();
		this.input = input;
		this.lives = stats.startingLives();
	}

	@Override
	public void update(UpdateContext context) {
		Vector2 movement = input.movementDirection().multiply(stats.moveSpeed());
		setVelocity(movement);

		super.update(context);

		float halfWidth = getWidth() / 2f;
		float halfHeight = getHeight() / 2f;

		setPosition(getPosition().clamp(
				halfWidth,
				halfHeight,
				ScreenConfig.WIDTH - halfWidth,
				ScreenConfig.HEIGHT - halfHeight
		));

		fireIfReady(context);

		invincible = context.elapsedMillis() < invincibleUntil;
	}

	private void fireIfReady(UpdateContext context) {
		long now = context.elapsedMillis();

		if (now - lastShotTime < stats.projectile().cooldownMs()) {
			return;
		}

		Vector2 direction = getPosition().directionTo(input.getMousePosition());

		if (direction.length() == 0f) {
			return;
		}

		context.spawnService().spawnPlayerProjectile(getPosition(), direction, stats.projectile());
		context.world().getRunStats().recordShotFired();
		lastShotTime = now;
	}

	@Override
	public void render(Graphics2D g2) {
		g2.setColor(invincible ? ColorConfig.PLAYER_INVINCIBLE : ColorConfig.PLAYER);
		g2.fillRect(
				Math.round(getBounds().x()),
				Math.round(getBounds().y()),
				Math.round(getBounds().width()),
				Math.round(getBounds().height())
		);
	}

	@Override
	public void takeDamage(int damage, UpdateContext context) {
		long now = context.elapsedMillis();

		if (now < invincibleUntil || isDead()) {
			return;
		}

		lives -= damage;
		invincibleUntil = now + stats.invincibilityMs();

		if (lives <= 0) {
			lives = 0;
			markInactive();
		}
	}
	
	@Override
	public int getCurrentHp() {
		return lives;
	}

	@Override
	public int getMaxHp() {
		return stats.maxLives();
	}

	@Override
	public boolean isDead() {
		return lives <= 0;
	}
	
	@Override
	public Team getTeam() {
		return Team.PLAYER;
	}

	public void applyStats(PlayerStats newStats) {
		this.stats = newStats;
	}

	public int getLives() {
		return lives;
	}

	public void handleDeath(GameWorld world) {
		if (isDead()) {
			world.setState(GameState.GAME_OVER);
		}
	}
}