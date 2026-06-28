package com.lobsterchops.infinitesquareshooter.model.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.lobsterchops.infinitesquareshooter.audio.AudioService;
import com.lobsterchops.infinitesquareshooter.combat.Team;
import com.lobsterchops.infinitesquareshooter.combat.TeamMember;
import com.lobsterchops.infinitesquareshooter.config.ColorConfig;
import com.lobsterchops.infinitesquareshooter.config.ConfigRegistry;
import com.lobsterchops.infinitesquareshooter.config.ScreenConfig;
import com.lobsterchops.infinitesquareshooter.config.stats.PlayerStats;
import com.lobsterchops.infinitesquareshooter.config.stats.ProjectileStats;
import com.lobsterchops.infinitesquareshooter.core.ServiceLocator;
import com.lobsterchops.infinitesquareshooter.input.InputManager;
import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.model.Damageable;
import com.lobsterchops.infinitesquareshooter.model.Entity;
import com.lobsterchops.infinitesquareshooter.model.GameWorld;
import com.lobsterchops.infinitesquareshooter.model.UpdateContext;
import com.lobsterchops.infinitesquareshooter.state.GameState;
import com.lobsterchops.infinitesquareshooter.utils.SpriteRegistry;

public class Player extends Entity implements Damageable, TeamMember {

    private PlayerStats stats;
    private InputManager input;

    AudioService audioService = ServiceLocator.resolve(AudioService.class);

    public static final int MAX_WEAPON_TIER = 6;

    private int lives;
    private long lastShotTime;
    private long invincibleUntil;
    private boolean invincible;
    private int weaponTier = 1;

    public Player(Vector2 position, InputManager input) {
        super(position, 32f, 32f);
        this.stats = ConfigRegistry.player();
        this.input = input;
        this.lives = stats.startingLives();
    }

    @Override
    public void update(UpdateContext context) {
        float speedMultiplier = context.world().getPowerUpManager().speedMultiplier();
        Vector2 movement = input.movementDirection().multiply(stats.moveSpeed() * speedMultiplier);
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
        float fireRateMultiplier = context.world().getPowerUpManager().fireRateMultiplier();
        long effectiveCooldownMs = Math.max(60L, Math.round(stats.projectile().cooldownMs() / fireRateMultiplier));

        if (now - lastShotTime < effectiveCooldownMs) {
            return;
        }

        Vector2 direction = getPosition().directionTo(input.getMousePosition());

        if (direction.length() == 0f) {
            return;
        }

        context.spawnService().spawnPlayerProjectile(getPosition(), direction, resolveProjectileStats());
        context.world().getRunStats().recordShotFired();
        lastShotTime = now;
    }

    @Override
    public void render(Graphics2D g2) {
        BufferedImage sprite = invincible
            ? SpriteRegistry.forPlayer(true)
            : SpriteRegistry.forPlayer(false);

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
            g2.setColor(invincible ? ColorConfig.PLAYER_INVINCIBLE : ColorConfig.PLAYER);
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

    public void addLife() {
        lives++;
    }

    public void upgradeWeaponTier() {
        if (weaponTier < MAX_WEAPON_TIER) {
            weaponTier++;
        }
    }

    public int getWeaponTier() {
        return weaponTier;
    }

    /**
     * Resolves the projectile stats to use this frame, factoring in weapon tier.
     *
     * Weapon tier directly controls projectile count — no upper limit.
     * spreadDegrees from base stats controls the fan angle between shots.
     * All other fields (speed, damage, homing, cooldown) come straight from config.
     */
    private ProjectileStats resolveProjectileStats() {
        ProjectileStats base = stats.projectile();

        // Weapon tier directly controls projectile count, capped at MAX_WEAPON_TIER.
        int count = Math.min(weaponTier, MAX_WEAPON_TIER);

        return new ProjectileStats(
                base.speed(),
                base.damage(),
                count,
                base.spreadDegrees(),
                base.isHoming(),
                base.homingTurnRate(),
                base.cooldownMs()
        );
    }

    public void handleDeath(GameWorld world) {
        if (isDead()) {
            world.setState(GameState.GAME_OVER);
        }
    }
}