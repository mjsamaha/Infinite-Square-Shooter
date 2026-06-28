package com.lobsterchops.infinitesquareshooter.model.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.lobsterchops.infinitesquareshooter.combat.DamageSource;
import com.lobsterchops.infinitesquareshooter.combat.Team;
import com.lobsterchops.infinitesquareshooter.combat.TeamMember;
import com.lobsterchops.infinitesquareshooter.config.ColorConfig;
import com.lobsterchops.infinitesquareshooter.config.ScreenConfig;
import com.lobsterchops.infinitesquareshooter.config.stats.BossStats;
import com.lobsterchops.infinitesquareshooter.config.stats.ProjectileStats;
import com.lobsterchops.infinitesquareshooter.config.types.BossType;
import com.lobsterchops.infinitesquareshooter.math.Bounds;
import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.model.Collidable;
import com.lobsterchops.infinitesquareshooter.model.Damageable;
import com.lobsterchops.infinitesquareshooter.model.GameObject;
import com.lobsterchops.infinitesquareshooter.model.Renderable;
import com.lobsterchops.infinitesquareshooter.model.UpdateContext;
import com.lobsterchops.infinitesquareshooter.render.RenderLayer;
import com.lobsterchops.infinitesquareshooter.utils.SpriteRegistry;

/**
 * The Fortress — second boss, end of Act 2 (after wave 24).
 *
 * Mechanics: - Large slow-moving core that drifts to the upper-centre of the
 * screen. - Four FortressTurret sub-entities are spawned with the boss,
 * positioned at N / S / E / W offsets from the core centre. - The core is
 * INVULNERABLE while any turret remains alive. - Each turret shoots
 * independently on its own cooldown. - When all turrets are destroyed the core
 * becomes vulnerable and the player can damage it directly via normal
 * projectile collision. - On core death all surviving turrets (edge case) are
 * marked inactive.
 *
 * Visual: - Core: large military-green square (100×100). - Invulnerable tint:
 * semi-transparent dark overlay on the core. - Turrets: smaller green squares
 * (24×24) connected to the core by a thin line so the spatial relationship is
 * clear.
 */
public class FortressBoss extends Boss {

	private static final float CORE_SIZE = 100f;
	private static final float TURRET_SIZE = 24f;
	private static final float TURRET_REACH = 70f; // distance from core centre to turret centre

	// Target resting position.
	private static final float TARGET_X = ScreenConfig.WIDTH / 2f;
	private static final float TARGET_Y = ScreenConfig.HEIGHT * 0.20f;

	// Turret shoot cooldown — each turret uses this independently.
	private static final long TURRET_SHOOT_MS = 1_800L;

	private final List<FortressTurret> turrets = new ArrayList<>();
	private boolean turretsSpawned = false;

	public FortressBoss(BossStats stats, Vector2 position) {
		super(BossType.FORTRESS, stats, position, CORE_SIZE, CORE_SIZE);
	}

	@Override
	protected void updateBehaviour(UpdateContext context) {
		if (!turretsSpawned) {
			spawnTurrets(context);
			turretsSpawned = true;
		}

		updateMovement();
		updateTurretPositions();

		// Kill all remaining turrets when the core dies — handled in the
		// overridden takeDamage path once HP reaches zero, but we also check
		// here for safety in case death was processed by BossDeathSystem.
		if (phaseController.isDead()) {
			deactivateAllTurrets();
		}
	}

	@Override
	public void takeDamage(int damage, UpdateContext context) {
		if (hasLivingTurrets()) {
			return; // Core absorbs nothing — turrets must die first.
		}
		super.takeDamage(damage, context);
	}

	private void updateMovement() {
		Vector2 pos = getPosition();
		float dx = TARGET_X - pos.x();
		float dy = TARGET_Y - pos.y();
		float dist = (float) Math.sqrt(dx * dx + dy * dy);

		if (dist < 2f) {
			setVelocity(Vector2.ZERO);
			return;
		}

		Vector2 direction = new Vector2(dx / dist, dy / dist);
		setVelocity(direction.multiply(stats.baseSpeed()));
	}

	private void spawnTurrets(UpdateContext context) {
		// N / E / S / W offsets relative to core centre.
		float[][] offsets = { { 0f, -TURRET_REACH }, // North
				{ TURRET_REACH, 0f }, // East
				{ 0f, TURRET_REACH }, // South
				{ -TURRET_REACH, 0f } // West
		};

		for (float[] offset : offsets) {
			Vector2 turretPos = getPosition().add(new Vector2(offset[0], offset[1]));
			FortressTurret turret = new FortressTurret(turretPos, stats.turretHp(), this);
			turrets.add(turret);
			context.world().addObject(turret);
		}
	}

	/**
	 * Called every tick to keep turret positions locked to the core as it moves.
	 */
	private void updateTurretPositions() {
		float[][] offsets = { { 0f, -TURRET_REACH }, { TURRET_REACH, 0f }, { 0f, TURRET_REACH },
				{ -TURRET_REACH, 0f } };

		for (int i = 0; i < turrets.size(); i++) {
			FortressTurret turret = turrets.get(i);
			if (turret.isActive()) {
				Vector2 newPos = getPosition().add(new Vector2(offsets[i][0], offsets[i][1]));
				turret.setPosition(newPos);
			}
		}
	}

	private boolean hasLivingTurrets() {
		for (FortressTurret turret : turrets) {
			if (turret.isActive()) {
				return true;
			}
		}
		return false;
	}

	private void deactivateAllTurrets() {
		for (FortressTurret turret : turrets) {
			if (turret.isActive()) {
				turret.markInactive();
			}
		}
	}

	@Override
	protected void renderBoss(Graphics2D g2) {
	    int x = Math.round(getBounds().x());
	    int y = Math.round(getBounds().y());
	    int w = Math.round(getBounds().width());
	    int h = Math.round(getBounds().height());

	    // Connector lines first — behind the core body.
	    g2.setColor(ColorConfig.BOSS_FORTRESS.darker());
	    int cx = Math.round(getPosition().x());
	    int cy = Math.round(getPosition().y());
	    for (FortressTurret turret : turrets) {
	        if (turret.isActive()) {
	            g2.drawLine(cx, cy, Math.round(turret.getPosition().x()), Math.round(turret.getPosition().y()));
	        }
	    }

	    BufferedImage sprite = SpriteRegistry.forBoss(BossType.FORTRESS);

	    if (sprite != null) {
	        g2.drawImage(sprite, x, y, w, h, null);
	    } else {
	        g2.setColor(ColorConfig.BOSS_FORTRESS);
	        g2.fillRect(x, y, w, h);
	        g2.setColor(ColorConfig.BOSS_FORTRESS.darker());
	        g2.drawRect(x, y, w, h);
	    }

	    // Invulnerable overlay on top of sprite while turrets are alive.
	    if (hasLivingTurrets()) {
	        g2.setColor(new Color(0, 0, 0, 120));
	        g2.fillRect(x, y, w, h);
	    }
	}

	/**
	 * A self-contained turret sub-entity. Each turret: - Has its own HP pool (from
	 * BossStats.turretHp()). - Shoots independently toward the player on its own
	 * cooldown. - Is a full GameObject / Damageable / TeamMember / Collidable so
	 * the existing CollisionSystem handles player-projectile hits on turrets with
	 * zero special casing. - Does NOT extend Entity — it manages its own position
	 * directly, since its position is driven by the parent core each tick.
	 */
	public static class FortressTurret
			implements GameObject, Damageable, TeamMember, Collidable, Renderable, DamageSource {

		private static final ProjectileStats TURRET_PROJECTILE = ProjectileStats.single(5f, 1, TURRET_SHOOT_MS);

		private final FortressBoss parent;

		private Vector2 position;
		private int hp;
		private boolean active = true;
		private long lastShotMs = 0L;

		public FortressTurret(Vector2 position, int hp, FortressBoss parent) {
			this.position = position;
			this.hp = hp;
			this.parent = parent;
		}

		@Override
		public void update(UpdateContext context) {
			if (!active)
				return;
			shoot(context);
		}

		@Override
		public boolean isActive() {
			return active;
		}

		public void markInactive() {
			active = false;
		}

		private void shoot(UpdateContext context) {
			long nowMs = context.elapsedMillis();
			if (nowMs - lastShotMs < TURRET_SHOOT_MS) {
				return;
			}

			Player player = context.world().getPlayer();
			if (player == null || player.isDead()) {
				return;
			}

			Vector2 direction = position.directionTo(player.getPosition());
			context.spawnService().spawnEnemyProjectiles(position, direction, TURRET_PROJECTILE);
			lastShotMs = nowMs;
		}

		@Override
		public void takeDamage(int damage, UpdateContext context) {
			if (!active)
				return;
			hp -= damage;
			if (hp <= 0) {
				hp = 0;
				active = false;
			}
		}

		@Override
		public int getCurrentHp() {
			return hp;
		}

		@Override
		public int getMaxHp() {
			return parent.stats.turretHp();
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
		public int getDamage() {
			return 1;
		}

		@Override
		public Bounds getBounds() {
			return Bounds.fromCenter(position, TURRET_SIZE, TURRET_SIZE);
		}

		@Override
		public void render(Graphics2D g2) {
			if (!active)
				return;

			int x = Math.round(position.x() - TURRET_SIZE / 2f);
			int y = Math.round(position.y() - TURRET_SIZE / 2f);
			int s = Math.round(TURRET_SIZE);

			// Body.
			g2.setColor(ColorConfig.BOSS_FORTRESS);
			g2.fillRect(x, y, s, s);

			// HP indicator — thin bar above the turret.
			int barW = s;
			int barH = 4;
			int barX = x;
			int barY = y - barH - 2;
			g2.setColor(new Color(60, 60, 60));
			g2.fillRect(barX, barY, barW, barH);
			float fraction = (float) hp / parent.stats.turretHp();
			g2.setColor(ColorConfig.BOSS_FORTRESS.brighter());
			g2.fillRect(barX, barY, Math.round(barW * fraction), barH);

			// Border.
			g2.setColor(ColorConfig.BOSS_FORTRESS.darker());
			g2.drawRect(x, y, s, s);
		}

		@Override
		public RenderLayer getRenderLayer() {
			return RenderLayer.ENTITIES;
		}

		public void setPosition(Vector2 position) {
			this.position = position;
		}

		public Vector2 getPosition() {
			return position;
		}
	}
}