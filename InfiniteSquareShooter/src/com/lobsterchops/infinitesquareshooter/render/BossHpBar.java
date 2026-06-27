package com.lobsterchops.infinitesquareshooter.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import com.lobsterchops.infinitesquareshooter.config.ColorConfig;
import com.lobsterchops.infinitesquareshooter.config.HudConfig;
import com.lobsterchops.infinitesquareshooter.config.ScreenConfig;
import com.lobsterchops.infinitesquareshooter.model.entity.Boss;

/**
 * Draws the boss HP bar at the bottom-centre of the screen.
 *
 * Layout (bottom-anchored):
 *   [boss name label]
 *   [phase pips      ]
 *   [====HP bar=====  ]
 *
 * Only rendered when a boss is active — HudRenderer gates the call.
 */
public class BossHpBar {

    private static final int   BAR_WIDTH      = 500;
    private static final int   BAR_HEIGHT     = 16;
    private static final int   BAR_BOTTOM_Y   = ScreenConfig.HEIGHT - HudConfig.PANEL_HEIGHT - 16;
    private static final int   BAR_LEFT_X     = (ScreenConfig.WIDTH - BAR_WIDTH) / 2;

    private static final int   PIP_SIZE       = 8;
    private static final int   PIP_GAP        = 6;
    private static final int   PIP_Y          = BAR_BOTTOM_Y - BAR_HEIGHT - 10;

    private static final int   LABEL_Y        = PIP_Y - 10;

    private static final Color BAR_BACKGROUND = new Color(60, 60, 60);
    private static final Color BAR_BORDER     = new Color(120, 120, 120);
    private static final Color PIP_INACTIVE   = new Color(80, 80, 80);
    private static final Color PIP_ACTIVE     = new Color(220, 220, 220);

    private static final Font  LABEL_FONT     = new Font("Arial", Font.BOLD, 14);

    public void render(Graphics2D g2, Boss boss) {
        renderLabel(g2, boss);
        renderPhasePips(g2, boss);
        renderBar(g2, boss);
    }


    private void renderLabel(Graphics2D g2, Boss boss) {
        String label = resolveBossName(boss);

        g2.setFont(LABEL_FONT);
        g2.setColor(ColorConfig.HUD_TEXT);

        FontMetrics metrics = g2.getFontMetrics();
        int textWidth = metrics.stringWidth(label);
        int centreX   = ScreenConfig.WIDTH / 2;

        g2.drawString(label, centreX - textWidth / 2, LABEL_Y);
    }

    private void renderPhasePips(Graphics2D g2, Boss boss) {
        int totalPhases   = boss.getPhaseController().getTotalPhases();
        int currentPhase  = boss.getPhaseController().getCurrentPhaseIndex();

        // Centre the pip row.
        int totalWidth = totalPhases * PIP_SIZE + (totalPhases - 1) * PIP_GAP;
        int startX     = (ScreenConfig.WIDTH - totalWidth) / 2;
        int centreY    = PIP_Y;

        for (int i = 0; i < totalPhases; i++) {
            // Phases at or before current index are considered active/done.
            Color pipColor = (i <= currentPhase) ? PIP_ACTIVE : PIP_INACTIVE;
            g2.setColor(pipColor);
            g2.fillOval(startX + i * (PIP_SIZE + PIP_GAP), centreY, PIP_SIZE, PIP_SIZE);
        }
    }

    private void renderBar(Graphics2D g2, Boss boss) {
        int currentHp = boss.getPhaseController().getHpInCurrentPhase();
        int maxHp     = boss.getPhaseController().getMaxHpInCurrentPhase();
        float fraction = maxHp > 0 ? (float) currentHp / maxHp : 0f;

        // Background.
        g2.setColor(BAR_BACKGROUND);
        g2.fillRect(BAR_LEFT_X, BAR_BOTTOM_Y - BAR_HEIGHT, BAR_WIDTH, BAR_HEIGHT);

        // Filled portion.
        int fillWidth = Math.round(BAR_WIDTH * fraction);
        if (fillWidth > 0) {
            g2.setColor(resolveBarColor(boss));
            g2.fillRect(BAR_LEFT_X, BAR_BOTTOM_Y - BAR_HEIGHT, fillWidth, BAR_HEIGHT);
        }

        // Border.
        g2.setColor(BAR_BORDER);
        g2.drawRect(BAR_LEFT_X, BAR_BOTTOM_Y - BAR_HEIGHT, BAR_WIDTH, BAR_HEIGHT);

        // Flash the bar white briefly during a phase transition.
        if (boss.getPhaseController().isTransitioning()) {
            g2.setColor(new Color(255, 255, 255, 80));
            g2.fillRect(BAR_LEFT_X, BAR_BOTTOM_Y - BAR_HEIGHT, BAR_WIDTH, BAR_HEIGHT);
        }
    }


    private String resolveBossName(Boss boss) {
        return switch (boss.getType()) {
            case SWARM_QUEEN   -> "👑 The Swarm Queen";
            case FORTRESS      -> "🏰 The Fortress";
            case SPLITTER_KING -> "💥 The Splitter King";
            case PHANTOM       -> "👻 The Phantom";
            case MIMIC         -> "🪞 The Mimic";
        };
    }

    private Color resolveBarColor(Boss boss) {
        return switch (boss.getType()) {
            case SWARM_QUEEN   -> ColorConfig.BOSS_SWARM_QUEEN;
            case FORTRESS      -> ColorConfig.BOSS_FORTRESS;
            case SPLITTER_KING -> ColorConfig.BOSS_SPLITTER_KING;
            case PHANTOM       -> ColorConfig.BOSS_PHANTOM;
            case MIMIC         -> ColorConfig.BOSS_MIMIC;
        };
    }
}