package com.lobsterchops.infinitesquareshooter.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import com.lobsterchops.infinitesquareshooter.config.ColorConfig;
import com.lobsterchops.infinitesquareshooter.config.HudConfig;
import com.lobsterchops.infinitesquareshooter.config.PowerUpConfig;
import com.lobsterchops.infinitesquareshooter.config.ScreenConfig;
import com.lobsterchops.infinitesquareshooter.config.types.PowerUpType;
import com.lobsterchops.infinitesquareshooter.model.GameWorld;
import com.lobsterchops.infinitesquareshooter.model.entity.Player;
import com.lobsterchops.infinitesquareshooter.score.ScoreManager;
import com.lobsterchops.infinitesquareshooter.system.PowerUpManager;

public class HudRenderer {

	public static final int COMBO_DISPLAY_THRESHOLD = 2;

	private final BossHpBar bossHpBar = new BossHpBar();

	// ── Panel geometry ────────────────────────────────────────────────────────
	private static final int PH = HudConfig.PANEL_HEIGHT; // 72
	private static final int PY = ScreenConfig.HEIGHT - PH; // 696

	// Section X starts and widths
	private static final int LIVES_X = 0, LIVES_W = 178;
	private static final int SCORE_X = LIVES_X + LIVES_W + 1, SCORE_W = 205;
	private static final int WAVE_X = SCORE_X + SCORE_W + 1, WAVE_W = 132;
	private static final int WEAPON_X = WAVE_X + WAVE_W + 1, WEAPON_W = 140;
	private static final int POWER_X = WEAPON_X + WEAPON_W + 1;
	private static final int POWER_W = ScreenConfig.WIDTH - POWER_X; // ~366

	// Vertical anchor rows inside the panel
	private static final int LABEL_Y = PY + 14; // small-label baseline
	private static final int VALUE_Y = PY + 50; // main-value baseline
	private static final int HEART_Y = PY + 22; // top of heart row

	// Typography
	private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 10);
	private static final Font VALUE_FONT = new Font("Arial", Font.BOLD, 20);
	private static final Font COMBO_FONT = new Font("Arial", Font.BOLD, 20);
	private static final Font SLOT_LABEL_FONT = new Font("Arial", Font.PLAIN, 9);
	private static final Font SLOT_VAL_FONT = new Font("Arial", Font.BOLD, 13);

	// Panel colours
	private static final Color PANEL_BG = new Color(10, 10, 20, 220);
	private static final Color PANEL_BORDER = new Color(60, 60, 95, 255);
	private static final Color PANEL_HEADER_STRIP = new Color(30, 30, 55, 255);
	private static final Color DIVIDER_COL = new Color(45, 45, 72, 255);
	private static final Color LABEL_COL = new Color(135, 135, 165);

	// Heart colours
	private static final Color HEART_FULL = new Color(215, 50, 50);
	private static final Color HEART_EMPTY = new Color(65, 20, 20);

	// Heart dimensions (pixel-art, 7 units × 7 units)
	private static final int HEART_UNIT = 2; // 1 unit = 2 px → heart = 14 × 14
	private static final int HEART_PX = 7 * HEART_UNIT; // 14
	private static final int HEART_GAP = 3;

	// Power-up slot dimensions
	private static final int SLOT_W = 108;
	private static final int SLOT_H = 50;
	private static final int SLOT_GAP = 6;
	private static final int TIMER_H = 4;

	// Power-up accent colours
	private static final Color COL_FIRE = new Color(255, 115, 35);
	private static final Color COL_SPEED = new Color(55, 195, 255);
	private static final Color COL_SCORE = new Color(255, 215, 50);

	private static final Color SLOT_BG = new Color(22, 22, 38, 255);
	private static final Color SLOT_BORDER_IDLE = new Color(50, 50, 75, 255);
	private static final Color SLOT_BORDER_ACTIVE = new Color(255, 215, 60, 210);

	// Timed power-up descriptor arrays (order matches rendered slots)
	private static final PowerUpType[] TIMED_TYPES = { PowerUpType.FIRE_RATE, PowerUpType.SPEED,
			PowerUpType.SCORE_MULTIPLIER };
	private static final String[] TIMED_LABELS = { "FIRE RATE", "SPEED", "SCORE MULT" };
	private static final Color[] TIMED_COLORS = { COL_FIRE, COL_SPEED, COL_SCORE };

	// ── Entry point ───────────────────────────────────────────────────────────

	public void render(Graphics2D g2, GameWorld world) {
		drawPanel(g2, world);
		if (world.hasActiveBoss()) {
			bossHpBar.render(g2, world.getActiveBoss());
		}
	}

	// ── Panel shell ───────────────────────────────────────────────────────────

	private void drawPanel(Graphics2D g2, GameWorld world) {
		// Outer background
		g2.setColor(PANEL_BG);
		g2.fillRect(0, PY, ScreenConfig.WIDTH, PH);

		// Thin lighter strip at top for depth
		g2.setColor(PANEL_HEADER_STRIP);
		g2.fillRect(0, PY, ScreenConfig.WIDTH, 3);

		// Top border accent line
		g2.setColor(PANEL_BORDER);
		g2.fillRect(0, PY, ScreenConfig.WIDTH, 2);

		// Render each section
		renderLives(g2, LIVES_X, LIVES_W, world);
		drawDivider(g2, SCORE_X - 1);
		renderScore(g2, SCORE_X, SCORE_W, world);
		drawDivider(g2, WAVE_X - 1);
		renderWave(g2, WAVE_X, WAVE_W, world);
		drawDivider(g2, WEAPON_X - 1);
		renderWeapon(g2, WEAPON_X, WEAPON_W, world);
		drawDivider(g2, POWER_X - 1);
		renderPowerUps(g2, POWER_X, POWER_W, world);

		// Combo overlay floats just above the panel
		renderCombo(g2, world);
	}

	private void drawDivider(Graphics2D g2, int x) {
		g2.setColor(DIVIDER_COL);
		g2.fillRect(x, PY + 8, 1, PH - 16);
	}

	// ── Section: Lives ────────────────────────────────────────────────────────

	private void renderLives(Graphics2D g2, int secX, int secW, GameWorld world) {
		int cx = secX + secW / 2;
		drawLabel(g2, "LIVES", cx);

		Player player = world.getPlayer();
		int lives = player == null ? 0 : player.getLives();
		int maxLives = player == null ? 3 : player.getMaxHp();
		int displayed = Math.min(maxLives, 10); // cap visual hearts at 10

		int totalPx = displayed * (HEART_PX + HEART_GAP) - HEART_GAP;
		int startX = cx - totalPx / 2;

		for (int i = 0; i < displayed; i++) {
			g2.setColor(i < lives ? HEART_FULL : HEART_EMPTY);
			drawPixelHeart(g2, startX + i * (HEART_PX + HEART_GAP), HEART_Y);
		}

		// If lives exceed the 10-heart display, append a count badge
		if (lives > 10) {
			g2.setFont(SLOT_VAL_FONT);
			g2.setColor(HEART_FULL);
			g2.drawString("+" + (lives - 10), startX + totalPx + 4, HEART_Y + HEART_PX - 2);
		}
	}

	/**
	 * 7×7 pixel-art heart drawn with filled rectangles (no antialiasing needed).
	 * Grid (each cell = HEART_UNIT pixels):
	 * 
	 * <pre>
	 *  . # # . # # .
	 *  # # # # # # #
	 *  # # # # # # #
	 *  # # # # # # #
	 *  . # # # # # .
	 *  . . # # # . .
	 *  . . . # . . .
	 * </pre>
	 */
	private void drawPixelHeart(Graphics2D g2, int x, int y) {
		int u = HEART_UNIT;
		// Row 0 – two top bumps
		g2.fillRect(x + u, y, 2 * u, u);
		g2.fillRect(x + 4 * u, y, 2 * u, u);
		// Rows 1-3 – full-width body
		g2.fillRect(x, y + u, 7 * u, 3 * u);
		// Row 4 – first taper
		g2.fillRect(x + u, y + 4 * u, 5 * u, u);
		// Row 5 – second taper
		g2.fillRect(x + 2 * u, y + 5 * u, 3 * u, u);
		// Row 6 – point
		g2.fillRect(x + 3 * u, y + 6 * u, u, u);
	}

	// ── Section: Score ────────────────────────────────────────────────────────

	private void renderScore(Graphics2D g2, int secX, int secW, GameWorld world) {
		int cx = secX + secW / 2;
		drawLabel(g2, "SCORE", cx);
		ScoreManager sm = world.getScoreManager();
		drawValue(g2, String.format("%,d", sm.getScore()), cx, ColorConfig.HUD_TEXT);
	}

	// ── Section: Wave ─────────────────────────────────────────────────────────

	private void renderWave(Graphics2D g2, int secX, int secW, GameWorld world) {
		int cx = secX + secW / 2;
		drawLabel(g2, "WAVE", cx);
		drawValue(g2, String.valueOf(world.getWaveNumber()), cx, ColorConfig.WAVE_ACCENT);
	}

	// ── Section: Weapon ───────────────────────────────────────────────────────

	private void renderWeapon(Graphics2D g2, int secX, int secW, GameWorld world) {
		int cx = secX + secW / 2;
		drawLabel(g2, "WEAPON", cx);
		Player player = world.getPlayer();
		int tier = player == null ? 1 : player.getWeaponTier();
		Color tierColor = switch (tier) {
		case 1 -> new Color(170, 170, 170); // grey – base
		case 2 -> new Color(80, 215, 80); // green
		case 3 -> new Color(80, 155, 255); // blue
		default -> new Color(255, 215, 50); // gold – T4+
		};
		drawValue(g2, "TIER " + tier, cx, tierColor);
	}

	// ── Section: Power-ups ───────────────────────────────────────────────────

	private void renderPowerUps(Graphics2D g2, int secX, int secW, GameWorld world) {
		PowerUpManager pm = world.getPowerUpManager();
		long nowMs = world.getElapsedMillis();

		int totalW = 3 * SLOT_W + 2 * SLOT_GAP;
		int startX = secX + (secW - totalW) / 2;
		int slotY = PY + (PH - SLOT_H) / 2;

		for (int i = 0; i < 3; i++) {
			drawPowerUpSlot(g2, startX + i * (SLOT_W + SLOT_GAP), slotY, TIMED_TYPES[i], TIMED_LABELS[i],
					TIMED_COLORS[i], pm, nowMs);
		}
	}

	private void drawPowerUpSlot(Graphics2D g2, int x, int y, PowerUpType type, String label, Color color,
			PowerUpManager pm, long nowMs) {
		int stacks = pm.getStacks(type);
		boolean active = stacks > 0;
		long remMs = active ? pm.getRemainingMs(type, nowMs) : 0L;
		long durMs = PowerUpConfig.definition(type).durationMs();

		// ── Slot background
		g2.setColor(SLOT_BG);
		g2.fillRoundRect(x, y, SLOT_W, SLOT_H, 8, 8);

		// ── Border (glows when active)
		g2.setColor(active ? SLOT_BORDER_ACTIVE : SLOT_BORDER_IDLE);
		g2.drawRoundRect(x, y, SLOT_W - 1, SLOT_H - 1, 8, 8);

		// ── Colour dot
		int dotSize = 9;
		int dotX = x + 8;
		int dotY = y + 9;
		g2.setColor(active ? color : dimColor(color));
		g2.fillRoundRect(dotX, dotY, dotSize, dotSize, 3, 3);

		// ── Label (next to dot)
		g2.setFont(SLOT_LABEL_FONT);
		g2.setColor(active ? ColorConfig.HUD_TEXT : LABEL_COL);
		FontMetrics lm = g2.getFontMetrics();
		g2.drawString(label, dotX + dotSize + 4, dotY + lm.getAscent() - 1);

		// ── Value text (centred)
		String valText;
		if (active) {
			long secs = Math.max(1L, remMs / 1000L);
			valText = stacks > 1 ? "x" + stacks + "  " + secs + "s" : secs + "s";
		} else {
			valText = "READY";
		}
		g2.setFont(SLOT_VAL_FONT);
		g2.setColor(active ? color : dimColor(color));
		FontMetrics vm = g2.getFontMetrics();
		int valX = x + SLOT_W / 2 - vm.stringWidth(valText) / 2;
		int valY = y + SLOT_H - TIMER_H - 10;
		g2.drawString(valText, valX, valY);

		// ── Timer bar track
		int barX = x + 5;
		int barW = SLOT_W - 10;
		int barY = y + SLOT_H - TIMER_H - 3;
		g2.setColor(new Color(20, 20, 30));
		g2.fillRoundRect(barX, barY, barW, TIMER_H, 2, 2);

		// ── Timer bar fill
		if (active && durMs > 0) {
			float frac = Math.min(1f, (float) remMs / durMs);
			int fillW = Math.max(2, Math.round(frac * barW));
			g2.setColor(color);
			g2.fillRoundRect(barX, barY, fillW, TIMER_H, 2, 2);
		}
	}

	// ── Combo (floats above panel) ────────────────────────────────────────────

	private void renderCombo(Graphics2D g2, GameWorld world) {
		ScoreManager sm = world.getScoreManager();
		if (sm.getComboCount() <= COMBO_DISPLAY_THRESHOLD)
			return;

		String text = String.format("COMBO  x%.1f", sm.getComboMultiplier());
		g2.setFont(COMBO_FONT);
		FontMetrics fm = g2.getFontMetrics();
		int x = ScreenConfig.WIDTH / 2 - fm.stringWidth(text) / 2;
		int y = PY - 10;

		// Drop shadow
		g2.setColor(new Color(0, 0, 0, 160));
		g2.drawString(text, x + 1, y + 1);
		// Bright text
		g2.setColor(ColorConfig.SCORE_COLOUR);
		g2.drawString(text, x, y);
	}

	// ── Shared drawing helpers ────────────────────────────────────────────────

	/** Draws a small muted section label centred at cx, at LABEL_Y. */
	private void drawLabel(Graphics2D g2, String text, int cx) {
		g2.setFont(LABEL_FONT);
		g2.setColor(LABEL_COL);
		FontMetrics fm = g2.getFontMetrics();
		g2.drawString(text, cx - fm.stringWidth(text) / 2, LABEL_Y);
	}

	/** Draws the main value text centred at cx, at VALUE_Y. */
	private void drawValue(Graphics2D g2, String text, int cx, Color color) {
		g2.setFont(VALUE_FONT);
		g2.setColor(color);
		FontMetrics fm = g2.getFontMetrics();
		g2.drawString(text, cx - fm.stringWidth(text) / 2, VALUE_Y);
	}

	/** Returns a noticeably dimmed version of a colour for inactive states. */
	private Color dimColor(Color c) {
		return new Color(c.getRed() / 4, c.getGreen() / 4, c.getBlue() / 4);
	}
}
