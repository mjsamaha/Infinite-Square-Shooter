package com.lobsterchops.infinitesquareshooter.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import com.lobsterchops.infinitesquareshooter.config.ColorConfig;
import com.lobsterchops.infinitesquareshooter.config.ScreenConfig;

public class PauseOverlay {

	private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 42);
	private static final Font PROMPT_FONT = new Font("Arial", Font.PLAIN, 18);

	public void render(Graphics2D g2) {
		g2.setColor(new Color(0, 0, 0, 160));
		g2.fillRect(0, 0, ScreenConfig.WIDTH, ScreenConfig.HEIGHT);

		int centerX = ScreenConfig.WIDTH / 2;

		drawCentered(g2, "PAUSED", TITLE_FONT, ColorConfig.HUD_TEXT, centerX, ScreenConfig.HEIGHT / 2 - 20);
		drawCentered(g2, "Press ESC to Resume", PROMPT_FONT, ColorConfig.HUD_TEXT, centerX, ScreenConfig.HEIGHT / 2 + 20);
	}

	private void drawCentered(Graphics2D g2, String text, Font font, Color color, int centerX, int y) {
		g2.setFont(font);
		g2.setColor(color);
		FontMetrics metrics = g2.getFontMetrics();
		int textWidth = metrics.stringWidth(text);
		g2.drawString(text, centerX - textWidth / 2, y);
	}
}