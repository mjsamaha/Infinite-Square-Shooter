package com.lobsterchops.infinitesquareshooter.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import com.lobsterchops.infinitesquareshooter.config.ColorConfig;
import com.lobsterchops.infinitesquareshooter.config.PauseOverlayConfig;
import com.lobsterchops.infinitesquareshooter.config.ScreenConfig;

public class PauseOverlay {

	public void render(Graphics2D g2) {
		g2.setColor(new Color(0, 0, 0, 160));
		g2.fillRect(0, 0, ScreenConfig.WIDTH, ScreenConfig.HEIGHT);

		int centerX = (int) (ScreenConfig.WIDTH / ScreenConfig.CENTER_DIVISOR);
		int centerY = (int) (ScreenConfig.HEIGHT / ScreenConfig.CENTER_DIVISOR);

		drawCentered(g2, "PAUSED", PauseOverlayConfig.TITLE_FONT, ColorConfig.HUD_TEXT, centerX,
				centerY + PauseOverlayConfig.TITLE_OFFSET_Y);
		drawCentered(g2, "Press ESC to Resume", PauseOverlayConfig.PROMPT_FONT, ColorConfig.HUD_TEXT, centerX,
				centerY + PauseOverlayConfig.PROMPT_OFFSET_Y);
	}

	private void drawCentered(Graphics2D g2, String text, Font font, Color color, int centerX, int y) {
		g2.setFont(font);
		g2.setColor(color);
		FontMetrics metrics = g2.getFontMetrics();
		int textWidth = metrics.stringWidth(text);
		g2.drawString(text, centerX - textWidth / 2, y);
	}
}