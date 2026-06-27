package com.lobsterchops.infinitesquareshooter.model.entity;

import java.awt.Color;
import java.awt.Graphics2D;

import com.lobsterchops.infinitesquareshooter.config.ColorConfig;
import com.lobsterchops.infinitesquareshooter.config.ScreenConfig;
import com.lobsterchops.infinitesquareshooter.config.stats.BossStats;
import com.lobsterchops.infinitesquareshooter.config.types.BossType;
import com.lobsterchops.infinitesquareshooter.config.types.EnemyType;
import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.model.UpdateContext;

/**
 * The Swarm Queen — first boss, end of Act 1 (after wave 12).
 *
 * Mechanics:
 *  - Drifts slowly toward horizontal centre of the screen, staying in the
 *    upper third vertically.
 *  - Has a shell that opens and closes on a fixed timer
 *    (shellIntervalMs from BossStats, open for shellOpenMs).
 *  - While the shell is CLOSED all incoming damage is blocked.
 *  - While the shell is OPEN damage passes through to BossPhaseController.
 *  - Spawns a small cluster of SWARM_I enemies every spawnIntervalMs
 *    regardless of shell state.
 *  - Fires a single projectile toward the player every second while the
 *    shell is open.
 *
 * Visual:
 *  - Large amber square (80×80) for the body.
 *  - Darker amber border drawn thicker when shell is closed; a gap is
 *    cut from the bottom edge when shell is open to signal the weak point.
 *  - Small amber circle pulses at the centre when shell is open.
 */
public class SwarmQueenBoss extends Boss {

    private static final float SIZE            = 80f;
    private static final int   BORDER_CLOSED   = 6;
    private static final int   BORDER_OPEN     = 2;
    private static final int   WEAK_POINT_SIZE = 18;

    // How many swarm enemies spawn per burst.
    private static final int   SWARM_BURST     = 4;
    // Offset radius from queen centre when spawning minions.
    private static final float SPAWN_RADIUS    = 70f;

    private static final long  SHOOT_COOLDOWN_MS = 1_000L;

    private boolean shellOpen          = false;
    private long    shellCycleStartMs  = 0L;   // when the current open/closed phase began
    private long    lastMinionSpawnMs  = 0L;
    private long    lastShotMs         = 0L;

    // Target X to drift toward (horizontal centre).
    private static final float TARGET_X = ScreenConfig.WIDTH / 2f;
    // Vertical position — stays in the upper quarter.
    private static final float TARGET_Y = ScreenConfig.HEIGHT * 0.18f;

    public SwarmQueenBoss(BossStats stats, Vector2 position) {
        super(BossType.SWARM_QUEEN, stats, position, SIZE, SIZE);
    }


    @Override
    protected void updateBehaviour(UpdateContext context) {
        long nowMs = context.elapsedMillis();

        updateShellCycle(nowMs);
        updateMovement(context);
        updateMinionSpawn(nowMs, context);
        updateShooting(nowMs, context);
    }


    private void updateShellCycle(long nowMs) {
        if (shellCycleStartMs == 0L) {
            // First tick — start closed.
            shellCycleStartMs = nowMs;
            shellOpen = false;
            return;
        }

        long elapsed = nowMs - shellCycleStartMs;

        if (!shellOpen) {
            // Closed phase: wait shellIntervalMs then open.
            if (elapsed >= stats.shellIntervalMs()) {
                shellOpen = true;
                shellCycleStartMs = nowMs;
            }
        } else {
            // Open phase: stay open for shellOpenMs then close again.
            if (elapsed >= stats.shellOpenMs()) {
                shellOpen = false;
                shellCycleStartMs = nowMs;
            }
        }
    }


    @Override
    public void takeDamage(int damage, UpdateContext context) {
        if (!shellOpen) {
            return; // Shell absorbs the hit.
        }
        super.takeDamage(damage, context);
    }


    private void updateMovement(UpdateContext context) {
        Vector2 pos = getPosition();

        float dx = TARGET_X - pos.x();
        float dy = TARGET_Y - pos.y();
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        if (dist < 2f) {
            setVelocity(Vector2.ZERO);
            return;
        }

        float speed = stats.baseSpeed();
        Vector2 direction = new Vector2(dx / dist, dy / dist);
        setVelocity(direction.multiply(speed));
    }


    private void updateMinionSpawn(long nowMs, UpdateContext context) {
        if (nowMs - lastMinionSpawnMs < stats.spawnIntervalMs()) {
            return;
        }

        for (int i = 0; i < SWARM_BURST; i++) {
            float angle  = (float) ((Math.PI * 2.0 / SWARM_BURST) * i);
            Vector2 offset = new Vector2(
                    (float) Math.cos(angle) * SPAWN_RADIUS,
                    (float) Math.sin(angle) * SPAWN_RADIUS
            );
            context.spawnService().spawnEnemy(EnemyType.SWARM_I, getPosition().add(offset));
        }

        lastMinionSpawnMs = nowMs;
    }


    private void updateShooting(long nowMs, UpdateContext context) {
        if (!shellOpen) {
            return;
        }

        if (nowMs - lastShotMs < SHOOT_COOLDOWN_MS) {
            return;
        }

        Player player = context.world().getPlayer();
        if (player == null || player.isDead()) {
            return;
        }

        var projectile = phaseController.getCurrentPhase().projectile();
        if (projectile == null) {
            return;
        }

        Vector2 direction = getPosition().directionTo(player.getPosition());
        context.spawnService().spawnEnemyProjectiles(getPosition(), direction, projectile);
        lastShotMs = nowMs;
    }


    @Override
    protected void renderBoss(Graphics2D g2) {
        int x = Math.round(getBounds().x());
        int y = Math.round(getBounds().y());
        int w = Math.round(getBounds().width());
        int h = Math.round(getBounds().height());

        // Body.
        g2.setColor(ColorConfig.BOSS_SWARM_QUEEN);
        g2.fillRect(x, y, w, h);

        // Shell border.
        int borderThickness = shellOpen ? BORDER_OPEN : BORDER_CLOSED;
        Color shellColor = shellOpen
                ? ColorConfig.BOSS_SWARM_QUEEN.darker()
                : ColorConfig.BOSS_SWARM_QUEEN.darker().darker();

        g2.setColor(shellColor);
        // Top
        g2.fillRect(x, y, w, borderThickness);
        // Left
        g2.fillRect(x, y, borderThickness, h);
        // Right
        g2.fillRect(x + w - borderThickness, y, borderThickness, h);
        // Bottom — omit centre section when open to show the gap.
        if (shellOpen) {
            int gapWidth  = w / 3;
            int gapStartX = x + (w - gapWidth) / 2;
            g2.fillRect(x, y + h - borderThickness, gapStartX - x, borderThickness);
            g2.fillRect(gapStartX + gapWidth, y + h - borderThickness,
                    x + w - (gapStartX + gapWidth), borderThickness);
        } else {
            g2.fillRect(x, y + h - borderThickness, w, borderThickness);
        }

        // Weak-point pulse when open.
        if (shellOpen) {
            int cx = x + w / 2 - WEAK_POINT_SIZE / 2;
            int cy = y + h / 2 - WEAK_POINT_SIZE / 2;
            g2.setColor(new Color(255, 220, 80, 200));
            g2.fillOval(cx, cy, WEAK_POINT_SIZE, WEAK_POINT_SIZE);
        }
    }
}