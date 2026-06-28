package com.lobsterchops.infinitesquareshooter.model.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.lobsterchops.infinitesquareshooter.config.ColorConfig;
import com.lobsterchops.infinitesquareshooter.config.ConfigRegistry;
import com.lobsterchops.infinitesquareshooter.config.stats.BossStats;
import com.lobsterchops.infinitesquareshooter.config.types.BossType;
import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.model.UpdateContext;
import com.lobsterchops.infinitesquareshooter.utils.SpriteRegistry;

/**
 * The Splitter King — third boss, end of Act 3 (after wave 36).
 *
 * Mechanics:
 *  - Three-tier cascade. The top-level boss (tier 0) is a large square.
 *    When it dies it spawns two tier-1 instances. Each tier-1 death spawns
 *    two tier-2 instances. Tier-2 deaths spawn nothing — they are the leaves.
 *  - Each tier is faster than the previous (speedMultiplier from BossPhase).
 *  - Moves straight toward the player at all times.
 *  - No projectiles — pure contact pressure.
 *  - Score is awarded by BossDeathSystem on each individual instance death,
 *    so the player earns points progressively as the cascade unfolds.
 *
 * Visual:
 *  - Tier 0: deep-red square, 90×90.
 *  - Tier 1: deep-red square, 58×58.
 *  - Tier 2: deep-red square, 36×36.
 *  - A small white Roman numeral label (I / II / III) in the centre
 *    identifies the tier at a glance.
 *
 * Implementation note:
 *  - Each instance owns its own BossStats pulled from ConfigRegistry. All
 *    tiers share the same BossType (SPLITTER_KING) and therefore the same
 *    three-phase BossStats. The `tier` field selects which phase index to
 *    treat as the "current" phase by fast-forwarding the phase controller
 *    past earlier phases on construction.
 *  - Rather than fast-forwarding the controller (which would complicate
 *    BossPhaseController), each tier instance is given a fresh BossStats
 *    whose phases list contains only the single phase relevant to that tier.
 *    This keeps BossPhaseController simple and each instance self-contained.
 */
public class SplitterKingBoss extends Boss {

    private static final float[] SIZES = { 90f, 58f, 36f };

    // Spawn offset radius when splitting.
    private static final float[] SPLIT_RADIUS = { 0f, 55f, 35f };

    // Maximum number of tiers (0-indexed).
    private static final int MAX_TIER = 2;


    /** 0 = top-level, 1 = medium, 2 = small (leaf). */
    private final int tier;

    /** True for one tick after the phase controller signals death. */
    private boolean splitSpawned = false;

    public SplitterKingBoss(BossStats stats, Vector2 position, int tier) {
        super(
            BossType.SPLITTER_KING,
            buildTierStats(stats, tier),
            position,
            SIZES[Math.min(tier, MAX_TIER)],
            SIZES[Math.min(tier, MAX_TIER)]
        );
        this.tier = Math.min(tier, MAX_TIER);
    }


    /**
     * Builds a single-phase BossStats for the given tier by extracting the
     * relevant BossPhase from the full three-phase config. This keeps each
     * instance self-contained with exactly one phase so BossPhaseController
     * needs no special tier awareness.
     *
     * Score value is divided across tiers:
     *   tier 0 → 40% of total, tier 1 → 35% (×2 instances), tier 2 → 25% (×4).
     */
    private static BossStats buildTierStats(BossStats source, int tier) {
        int clampedTier = Math.min(tier, MAX_TIER);
        var phase = source.phases().get(clampedTier);

        int tierScore = switch (clampedTier) {
            case 0  -> (int) (source.scoreValue() * 0.40);
            case 1  -> (int) (source.scoreValue() * 0.175); // ×2 instances
            default -> (int) (source.scoreValue() * 0.0625); // ×4 instances
        };

        return BossStats.builder()
                .baseSpeed(source.baseSpeed() * phase.speedMultiplier())
                .phases(java.util.List.of(phase))
                .scoreValue(tierScore)
                .build();
    }


    @Override
    protected void updateBehaviour(UpdateContext context) {
        updateMovement(context);
        checkSplit(context);
    }


    private void updateMovement(UpdateContext context) {
        Player player = context.world().getPlayer();

        if (player == null || player.isDead()) {
            setVelocity(Vector2.ZERO);
            return;
        }

        Vector2 direction = getPosition().directionTo(player.getPosition());
        setVelocity(direction.multiply(stats.baseSpeed()));
    }


    /**
     * When the phase controller signals death (phaseJustChanged fires on the
     * death tick) and this is not a leaf tier, spawn two child instances at
     * offset positions. splitSpawned guards against firing more than once.
     */
    private void checkSplit(UpdateContext context) {
        if (splitSpawned) {
            return;
        }

        if (!phaseController.isDead()) {
            return;
        }

        if (tier >= MAX_TIER) {
            // Leaf tier — no children.
            splitSpawned = true;
            return;
        }

        spawnChildren(context);
        splitSpawned = true;
    }

    private void spawnChildren(UpdateContext context) {
        int childTier = tier + 1;
        float radius  = SPLIT_RADIUS[childTier];

        // Spawn two children offset left and right of the death position.
        Vector2 leftPos  = getPosition().add(new Vector2(-radius, 0f));
        Vector2 rightPos = getPosition().add(new Vector2( radius, 0f));

        BossStats fullStats = ConfigRegistry.boss(BossType.SPLITTER_KING);

        SplitterKingBoss leftChild  = new SplitterKingBoss(fullStats, leftPos,  childTier);
        SplitterKingBoss rightChild = new SplitterKingBoss(fullStats, rightPos, childTier);

        context.world().addObject(leftChild);
        context.world().addObject(rightChild);

    }


    @Override
    protected void renderBoss(Graphics2D g2) {
        int x = Math.round(getBounds().x());
        int y = Math.round(getBounds().y());
        int w = Math.round(getBounds().width());
        int h = Math.round(getBounds().height());

        BufferedImage sprite = SpriteRegistry.forBoss(BossType.SPLITTER_KING);

        if (sprite != null) {
            g2.drawImage(sprite, x, y, w, h, null);
        } else {
            Color body = switch (tier) {
                case 0  -> ColorConfig.BOSS_SPLITTER_KING;
                case 1  -> ColorConfig.BOSS_SPLITTER_KING.brighter();
                default -> ColorConfig.BOSS_SPLITTER_KING.brighter().brighter();
            };
            g2.setColor(body);
            g2.fillRect(x, y, w, h);
            g2.setColor(body.darker());
            g2.drawRect(x, y, w, h);
        }

        // Tier label always drawn on top.
        String label = switch (tier) {
            case 0  -> "I";
            case 1  -> "II";
            default -> "III";
        };

        g2.setColor(new Color(255, 255, 255, 200));
        g2.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        java.awt.FontMetrics fm = g2.getFontMetrics();
        int labelX = x + (w - fm.stringWidth(label)) / 2;
        int labelY = y + (h + fm.getAscent() - fm.getDescent()) / 2;
        g2.drawString(label, labelX, labelY);
    }
}