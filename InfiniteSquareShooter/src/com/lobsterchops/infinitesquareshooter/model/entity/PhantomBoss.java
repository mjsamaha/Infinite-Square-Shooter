package com.lobsterchops.infinitesquareshooter.model.entity;

import java.awt.Color;
import java.awt.Graphics2D;

import com.lobsterchops.infinitesquareshooter.config.ColorConfig;
import com.lobsterchops.infinitesquareshooter.config.ScreenConfig;
import com.lobsterchops.infinitesquareshooter.config.stats.BossStats;
import com.lobsterchops.infinitesquareshooter.config.types.BossType;
import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.model.UpdateContext;

/**
 * The Phantom — fourth boss.
 *
 * Mechanics:
 *  - Mostly invisible: rendered at low opacity normally.
 *  - Attack cycle has three states that repeat in a loop:
 *      IDLE      — drifting slowly, invisible, invulnerable.
 *      TELEGRAPH — flashes fully visible for a brief window before attacking.
 *                  Damage is accepted during this window.
 *      ATTACK    — fires a homing burst toward the player, then teleports
 *                  to a random screen-edge position and returns to IDLE.
 *  - Damage is ONLY accepted during TELEGRAPH and immediately after
 *    transitioning into ATTACK (before the teleport). Once invisible again
 *    the shield closes.
 *
 * Visual:
 *  - Deep-violet square (70×70).
 *  - IDLE:        5% opacity.
 *  - TELEGRAPH:   100% opacity + bright white border pulse.
 *  - ATTACK:      60% opacity during the brief fire+teleport window.
 */
public class PhantomBoss extends Boss {

    private static final float SIZE = 70f;

    private static final long IDLE_DURATION_MS        = 3_000L;
    private static final long TELEGRAPH_DURATION_MS   =   600L;
    private static final long ATTACK_DURATION_MS      =   400L;

    // Number of homing projectiles fired per attack.
    private static final int  SHOT_COUNT = 3;
    private static final long SHOT_SPREAD_MS = 120L; // delay between each shot in the burst

    // How far from screen edges the Phantom can teleport to.
    private static final float EDGE_MARGIN = 80f;

    private enum CycleState { IDLE, TELEGRAPH, ATTACK }

    private CycleState cycleState        = CycleState.IDLE;
    private long       cycleStartedAtMs  = 0L;

    // Tracks how many shots in the current burst have been fired.
    private int  shotsFiredThisBurst     = 0;
    private long lastBurstShotMs         = 0L;

    // Teleport flag — fires once per ATTACK phase.
    private boolean teleportedThisCycle  = false;

    // Drift velocity while idle.
    private Vector2 idleDriftVelocity    = new Vector2(0.6f, 0.4f);

    public PhantomBoss(BossStats stats, Vector2 position) {
        super(BossType.PHANTOM, stats, position, SIZE, SIZE);
    }

    @Override
    protected void updateBehaviour(UpdateContext context) {
        long nowMs = context.elapsedMillis();

        if (cycleStartedAtMs == 0L) {
            cycleStartedAtMs = nowMs;
        }

        switch (cycleState) {
            case IDLE        -> updateIdle(nowMs);
            case TELEGRAPH   -> updateTelegraph(nowMs);
            case ATTACK      -> updateAttack(nowMs, context);
        }
    }


    @Override
    public void takeDamage(int damage, UpdateContext context) {
        if (cycleState == CycleState.IDLE) {
            return; // Invisible and invulnerable.
        }
        super.takeDamage(damage, context);
    }

    private void updateIdle(long nowMs) {
        // Drift slowly around the screen, bouncing off edges.
        Vector2 pos = getPosition();
        float newX  = pos.x() + idleDriftVelocity.x();
        float newY  = pos.y() + idleDriftVelocity.y();

        boolean bounceX = newX < EDGE_MARGIN || newX > ScreenConfig.WIDTH  - EDGE_MARGIN;
        boolean bounceY = newY < EDGE_MARGIN || newY > ScreenConfig.HEIGHT - EDGE_MARGIN;

        if (bounceX) idleDriftVelocity = new Vector2(-idleDriftVelocity.x(),  idleDriftVelocity.y());
        if (bounceY) idleDriftVelocity = new Vector2( idleDriftVelocity.x(), -idleDriftVelocity.y());

        setVelocity(idleDriftVelocity);

        if (nowMs - cycleStartedAtMs >= IDLE_DURATION_MS) {
            transitionCycle(CycleState.TELEGRAPH, nowMs);
        }
    }

    private void updateTelegraph(long nowMs) {
        // Stand still during telegraph so the player can actually aim.
        setVelocity(Vector2.ZERO);

        if (nowMs - cycleStartedAtMs >= TELEGRAPH_DURATION_MS) {
            transitionCycle(CycleState.ATTACK, nowMs);
        }
    }

    private void updateAttack(long nowMs, UpdateContext context) {
        setVelocity(Vector2.ZERO);

        // Fire the burst — one shot every SHOT_SPREAD_MS up to SHOT_COUNT.
        if (shotsFiredThisBurst < SHOT_COUNT) {
            if (nowMs - lastBurstShotMs >= SHOT_SPREAD_MS) {
                fireHomingShot(context);
                shotsFiredThisBurst++;
                lastBurstShotMs = nowMs;
            }
        }

        // Teleport once the burst is complete.
        if (shotsFiredThisBurst >= SHOT_COUNT && !teleportedThisCycle) {
            teleportToRandomEdge();
            teleportedThisCycle = true;
        }

        if (nowMs - cycleStartedAtMs >= ATTACK_DURATION_MS) {
            transitionCycle(CycleState.IDLE, nowMs);
        }
    }

    private void fireHomingShot(UpdateContext context) {
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
    }

    private void teleportToRandomEdge() {
        float margin = EDGE_MARGIN;
        // Pick one of four edges at random.
        int edge = (int) (Math.random() * 4);

        Vector2 newPos = switch (edge) {
            case 0 -> new Vector2(                                   // Top edge
                    margin + (float) Math.random() * (ScreenConfig.WIDTH  - margin * 2),
                    margin);
            case 1 -> new Vector2(                                   // Right edge
                    ScreenConfig.WIDTH - margin,
                    margin + (float) Math.random() * (ScreenConfig.HEIGHT - margin * 2));
            case 2 -> new Vector2(                                   // Bottom edge
                    margin + (float) Math.random() * (ScreenConfig.WIDTH  - margin * 2),
                    ScreenConfig.HEIGHT - margin);
            default -> new Vector2(                                  // Left edge
                    margin,
                    margin + (float) Math.random() * (ScreenConfig.HEIGHT - margin * 2));
        };

        setPosition(newPos);
        setVelocity(Vector2.ZERO);

        // Recalculate idle drift from new position with a fresh random direction.
        float angle = (float) (Math.random() * Math.PI * 2);
        idleDriftVelocity = new Vector2(
                (float) Math.cos(angle) * 0.6f,
                (float) Math.sin(angle) * 0.6f
        );
    }

    private void transitionCycle(CycleState next, long nowMs) {
        cycleState           = next;
        cycleStartedAtMs     = nowMs;

        if (next == CycleState.ATTACK) {
            shotsFiredThisBurst  = 0;
            lastBurstShotMs      = nowMs;
            teleportedThisCycle  = false;
        }
    }

    @Override
    protected void renderBoss(Graphics2D g2) {
        int x = Math.round(getBounds().x());
        int y = Math.round(getBounds().y());
        int w = Math.round(getBounds().width());
        int h = Math.round(getBounds().height());

        int alpha = switch (cycleState) {
            case IDLE      -> 13;   //  ~5% opacity
            case TELEGRAPH -> 255;  // 100% opacity
            case ATTACK    -> 153;  //  60% opacity
        };

        Color body = new Color(
                ColorConfig.BOSS_PHANTOM.getRed(),
                ColorConfig.BOSS_PHANTOM.getGreen(),
                ColorConfig.BOSS_PHANTOM.getBlue(),
                alpha
        );

        g2.setColor(body);
        g2.fillRect(x, y, w, h);

        // Bright border pulse during telegraph to maximise visibility.
        if (cycleState == CycleState.TELEGRAPH) {
            g2.setColor(new Color(255, 255, 255, 220));
            g2.drawRect(x,     y,     w,     h    );
            g2.drawRect(x + 1, y + 1, w - 2, h - 2);
            g2.drawRect(x + 2, y + 2, w - 4, h - 4);
        } else {
            g2.setColor(ColorConfig.BOSS_PHANTOM.darker());
            g2.drawRect(x, y, w, h);
        }
    }
}