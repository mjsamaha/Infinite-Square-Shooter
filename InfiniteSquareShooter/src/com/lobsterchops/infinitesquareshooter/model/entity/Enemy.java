package com.lobsterchops.infinitesquareshooter.model.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.lobsterchops.infinitesquareshooter.combat.ScoreValue;
import com.lobsterchops.infinitesquareshooter.combat.Team;
import com.lobsterchops.infinitesquareshooter.combat.TeamMember;
import com.lobsterchops.infinitesquareshooter.config.ColorConfig;
import com.lobsterchops.infinitesquareshooter.config.stats.EnemyStats;
import com.lobsterchops.infinitesquareshooter.config.types.BehaviourFlag;
import com.lobsterchops.infinitesquareshooter.config.types.EnemyType;
import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.model.Damageable;
import com.lobsterchops.infinitesquareshooter.model.Entity;
import com.lobsterchops.infinitesquareshooter.model.UpdateContext;
import com.lobsterchops.infinitesquareshooter.system.EnemyBehaviorSystem;
import com.lobsterchops.infinitesquareshooter.utils.SpriteRegistry;

public class Enemy extends Entity implements Damageable, TeamMember, ScoreValue {

	private final EnemyType type;
	private final EnemyStats stats;
	private final EnemyBehaviorSystem behaviorSystem;

	private int hp;
	private float movementAngle;
	private long lastShotTime;
	private long lastDashTime;
	private long invisibilityStartedAt;
	private boolean invisible;

	private boolean deathHandled;

	private long dashStartedAt;
	private boolean dashing;
	private float orbitRadius;
	private long lastBombTime;

	public Enemy(EnemyType type, EnemyStats stats, Vector2 position, EnemyBehaviorSystem behaviorSystem) {
		super(position, EnemySizeResolver.sizeFor(type), EnemySizeResolver.sizeFor(type));
		this.type = type;
		this.stats = stats;
		this.behaviorSystem = behaviorSystem;
		this.hp = stats.maxHp();
		this.orbitRadius = 120f;
	}

	@Override
	public void update(UpdateContext context) {
		behaviorSystem.update(this, context);
		super.update(context);
	}

	@Override
	public void render(Graphics2D g2) {
	    if (invisible) return;

	    BufferedImage sprite = SpriteRegistry.forEnemy(type);

	    if (sprite != null) {
	        g2.drawImage(
	            sprite,
	            Math.round(getBounds().x()),
	            Math.round(getBounds().y()),
	            Math.round(getBounds().width()),
	            Math.round(getBounds().height()),
	            null
	        );
	    } else {
	        g2.setColor(resolveColor());
	        g2.fillRect(
	            Math.round(getBounds().x()),
	            Math.round(getBounds().y()),
	            Math.round(getBounds().width()),
	            Math.round(getBounds().height())
	        );
	    }
	}

	@Override
	public void takeDamage(int damage, UpdateContext context) {
		if (invisible || isDead()) {
			return;
		}

		hp -= damage;

		if (hp <= 0) {
			hp = 0;
			markInactive();
		}
	}

	@Override
	public int getCurrentHp() {
		return hp;
	}

	@Override
	public int getMaxHp() {
		return stats.maxHp();
	}

	@Override
	public boolean isDead() {
		return hp <= 0;
	}

	@Override
	public Team getTeam() {
		return Team.ENEMY;
	}

	@Override
	public int getScoreValue() {
		return stats.scoreValue();
	}

	public boolean hasBehaviour(BehaviourFlag flag) {
		return stats.behaviours().contains(flag);
	}

	private java.awt.Color resolveColor() {
		String name = type.name();

		if (name.startsWith("BASIC"))
			return ColorConfig.ENEMY_BASIC;
		if (name.startsWith("ZIGZAG"))
			return ColorConfig.ENEMY_ZIGZAG;
		if (name.startsWith("SHOOTER"))
			return ColorConfig.ENEMY_SHOOTER;
		if (name.startsWith("DASHER"))
			return ColorConfig.ENEMY_DASHER;
		if (name.startsWith("SPREAD"))
			return ColorConfig.ENEMY_SPREAD;
		if (name.startsWith("TANK"))
			return ColorConfig.ENEMY_TANK;
		if (name.startsWith("SPLITTER"))
			return ColorConfig.ENEMY_SPLITTER;
		if (name.startsWith("ORBITER"))
			return ColorConfig.ENEMY_ORBITER;
		if (name.startsWith("BOMBER"))
			return ColorConfig.ENEMY_BOMBER;
		if (name.startsWith("GHOST"))
			return ColorConfig.ENEMY_GHOST;
		if (name.startsWith("HOMING"))
			return ColorConfig.ENEMY_HOMING;
		if (name.startsWith("SWARM"))
			return ColorConfig.ENEMY_SWARM;

		return ColorConfig.WHITE;
	}

	public boolean isDeathHandled() {
		return deathHandled;
	}

	public void markDeathHandled() {
		deathHandled = true;
	}

	public EnemyType getType() {
		return type;
	}

	public EnemyStats getStats() {
		return stats;
	}

	public float getMovementAngle() {
		return movementAngle;
	}

	public void setMovementAngle(float movementAngle) {
		this.movementAngle = movementAngle;
	}

	public long getLastShotTime() {
		return lastShotTime;
	}

	public void setLastShotTime(long lastShotTime) {
		this.lastShotTime = lastShotTime;
	}

	public long getLastDashTime() {
		return lastDashTime;
	}

	public void setLastDashTime(long lastDashTime) {
		this.lastDashTime = lastDashTime;
	}

	public long getInvisibilityStartedAt() {
		return invisibilityStartedAt;
	}

	public void setInvisibilityStartedAt(long invisibilityStartedAt) {
		this.invisibilityStartedAt = invisibilityStartedAt;
	}

	public boolean isInvisible() {
		return invisible;
	}

	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

	public long getDashStartedAt() {
		return dashStartedAt;
	}

	public void setDashStartedAt(long dashStartedAt) {
		this.dashStartedAt = dashStartedAt;
	}

	public boolean isDashing() {
		return dashing;
	}

	public void setDashing(boolean dashing) {
		this.dashing = dashing;
	}

	public float getOrbitRadius() {
		return orbitRadius;
	}

	public void setOrbitRadius(float orbitRadius) {
		this.orbitRadius = orbitRadius;
	}

	public long getLastBombTime() {
		return lastBombTime;
	}

	public void setLastBombTime(long lastBombTime) {
		this.lastBombTime = lastBombTime;
	}
}