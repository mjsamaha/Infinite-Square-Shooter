package com.lobsterchops.infinitesquareshooter.model.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.Deque;

import com.lobsterchops.infinitesquareshooter.config.ColorConfig;
import com.lobsterchops.infinitesquareshooter.config.stats.BossStats;
import com.lobsterchops.infinitesquareshooter.config.stats.ProjectileStats;
import com.lobsterchops.infinitesquareshooter.config.types.BossType;
import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.model.UpdateContext;

/**
 * The Mimic — fifth boss, end of Act 5.
 *
 * Mechanics:
 *  - Records the player's position in a rolling history buffer (~90 entries,
 *    ~1.5 s at 60 fps). Each tick the Mimic warps to the oldest buffered
 *    entry, creating a shadow that always occupies where the player JUST was.
 *  - Does NOT use velocity-based movement; velocity is always ZERO. Position
 *    is set directly to the delayed player position each tick once the buffer
 *    has warmed up.
 *  - Fires toward the player's current position on a per-phase cooldown.
 *  - Phase 1 (HP=60): single shot every 800 ms.
 *  - Phase 2 (HP=60): spread shot (3 proj, 20° arc) every 600 ms PLUS the
 *    phase-1 single shot on its own 800 ms track — "adds on top" of phase-1
 *    pressure with a wider arc and a precise centre shot simultaneously.
 *
 * Visual:
 *  - Teal square (72×72).
 *  - Phase 1: solid BOSS_MIMIC fill with a single thin darker border.
 *  - Phase 2: brighter fill + two inner glow rings to signal the escalation.
 */
public class MimicBoss extends Boss {

    private static final float SIZE          = 72f;

    // Number of history entries kept ≈ 1.5 s at 60 fps.
    private static final int   HISTORY_DEPTH = 90;

    // Rolling position buffer: oldest entry at head, newest at tail.
    private final Deque<Vector2> positionHistory = new ArrayDeque<>(HISTORY_DEPTH + 1);

    // Primary shoot cooldown — refreshed from current phase each tick.
    private long lastShotMs   = 0L;

    // Phase-2 secondary single-shot track (phase-1 stats reused at their own cadence).
    private long lastSingleMs = 0L;

    // Phase-1 single-shot stats preserved before the controller can advance
    // to phase 2, so they remain available for the dual-fire track.
    private ProjectileStats phase1Stats = null;

    public MimicBoss(BossStats stats, Vector2 position) {
        super(BossType.MIMIC, stats, position, SIZE, SIZE);
    }

    // -------------------------------------------------------------------------
    // Update
    // -------------------------------------------------------------------------

    @Override
    protected void updateBehaviour(UpdateContext context) {
        Player player = context.world().getPlayer();
        if (player == null || player.isDead()) {
            return;
        }

        // Capture phase-1 projectile stats exactly once, before any phase
        // transition can occur.  The check is cheap; it only fires once.
        if (phase1Stats == null) {
            phase1Stats = phaseController.getCurrentPhase().projectile();
        }

        long nowMs = context.elapsedMillis();

        updateMirroring(player);
        updateShooting(player, nowMs, context);
    }

    // -------------------------------------------------------------------------
    // Position mirroring
    // -------------------------------------------------------------------------

    private void updateMirroring(Player player) {
        // Append the player's current position to the tail of the buffer.
        positionHistory.addLast(player.getPosition());

        // Once the buffer holds more than HISTORY_DEPTH entries the boss warps
        // to the oldest (most-delayed) position by polling from the head.
        if (positionHistory.size() > HISTORY_DEPTH) {
            setPosition(positionHistory.pollFirst());
        }

        // The Mimic never drifts under its own velocity; position is set above.
        setVelocity(Vector2.ZERO);
    }

    // -------------------------------------------------------------------------
    // Shooting
    // -------------------------------------------------------------------------

    private void updateShooting(Player player, long nowMs, UpdateContext context) {
        var phase = phaseController.getCurrentPhase();
        if (phase.projectile() == null) {
            return;
        }

        // Fire from the Mimic's current (delayed) position toward the player's
        // current position — the shot "catches up" with the player.
        Vector2 direction = getPosition().directionTo(player.getPosition());
        if (direction.length() == 0f) {
            return; // Boss and player occupy the same point — edge case guard.
        }

        // Primary track: uses the current phase's projectile stats.
        // Phase 1 fires a single shot; phase 2 fires the spread shot.
        long cooldown = phase.projectile().cooldownMs();
        if (nowMs - lastShotMs >= cooldown) {
            context.spawnService().spawnEnemyProjectiles(getPosition(), direction, phase.projectile());
            lastShotMs = nowMs;
        }

        // Phase-2 bonus track: the phase-1 single shot continues independently,
        // layering a precise centre shot on top of the spread arc.
        if (phaseController.getCurrentPhaseIndex() >= 1 && phase1Stats != null) {
            if (nowMs - lastSingleMs >= phase1Stats.cooldownMs()) {
                context.spawnService().spawnEnemyProjectiles(getPosition(), direction, phase1Stats);
                lastSingleMs = nowMs;
            }
        }
    }

    // -------------------------------------------------------------------------
    // Rendering
    // -------------------------------------------------------------------------

    @Override
    protected void renderBoss(Graphics2D g2) {
        int x = Math.round(getBounds().x());
        int y = Math.round(getBounds().y());
        int w = Math.round(getBounds().width());
        int h = Math.round(getBounds().height());

        boolean phase2 = phaseController.getCurrentPhaseIndex() >= 1;

        // Body fill — brighter tint in phase 2 to mark the escalation clearly.
        Color body = phase2 ? ColorConfig.BOSS_MIMIC.brighter() : ColorConfig.BOSS_MIMIC;
        g2.setColor(body);
        g2.fillRect(x, y, w, h);

        // Phase-2 inner glow rings.
        if (phase2) {
            Color glow = new Color(
                    Math.min(255, ColorConfig.BOSS_MIMIC.getRed()   + 60),
                    Math.min(255, ColorConfig.BOSS_MIMIC.getGreen() + 60),
                    Math.min(255, ColorConfig.BOSS_MIMIC.getBlue()  + 60),
                    180);
            g2.setColor(glow);
            g2.drawRect(x + 3,  y + 3,  w - 6,  h - 6);
            g2.drawRect(x + 6,  y + 6,  w - 12, h - 12);
        }

        // Outer border.
        g2.setColor(ColorConfig.BOSS_MIMIC.darker());
        g2.drawRect(x, y, w, h);
    }
}
