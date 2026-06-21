package com.lobsterchops.infinitesquareshooter.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import com.lobsterchops.infinitesquareshooter.config.ColorConfig;
import com.lobsterchops.infinitesquareshooter.config.ScreenConfig;
import com.lobsterchops.infinitesquareshooter.model.GameWorld;
import com.lobsterchops.infinitesquareshooter.score.RunStats;
import com.lobsterchops.infinitesquareshooter.score.ScoreManager;

public class GameOverOverlay {

	private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 48);
	private static final Font STAT_FONT = new Font("Arial", Font.PLAIN, 20);
	private static final Font PROMPT_FONT = new Font("Arial", Font.BOLD, 18);

	public void render(Graphics2D g2, GameWorld world) {
		drawDimOverlay(g2);

		ScoreManager scoreManager = world.getScoreManager();
		RunStats runStats = world.getRunStats();

		int centerX = ScreenConfig.WIDTH / 2;
		int y = ScreenConfig.HEIGHT / 2 - 100;

		drawCentered(g2, "GAME OVER", TITLE_FONT, ColorConfig.SCORE_COLOUR, centerX, y);

		y += 60;
		drawCentered(g2, "Score: " + scoreManager.getScore(), STAT_FONT, ColorConfig.HUD_TEXT, centerX, y);

		y += 30;
		drawCentered(g2, "Wave Reached: " + runStats.getHighestWaveReached(), STAT_FONT, ColorConfig.HUD_TEXT, centerX, y);

		y += 30;
		drawCentered(g2, "Enemies Killed: " + runStats.getKills(), STAT_FONT, ColorConfig.HUD_TEXT, centerX, y);

		y += 30;
		drawCentered(g2, "Accuracy: " + Math.round(runStats.getAccuracy() * 100) + "%", STAT_FONT, ColorConfig.HUD_TEXT, centerX, y);

		y += 30;
		drawCentered(g2, "Time Survived: " + formatTime(runStats.getElapsedMillis()), STAT_FONT, ColorConfig.HUD_TEXT, centerX, y);

		y += 60;
		drawCentered(g2, "Press R to Restart", PROMPT_FONT, ColorConfig.SCORE_COLOUR, centerX, y);
	}

	private void drawDimOverlay(Graphics2D g2) {
		g2.setColor(new Color(0, 0, 0, 190));
		g2.fillRect(0, 0, ScreenConfig.WIDTH, ScreenConfig.HEIGHT);
	}

	private void drawCentered(Graphics2D g2, String text, Font font, Color color, int centerX, int y) {
		g2.setFont(font);
		g2.setColor(color);
		FontMetrics metrics = g2.getFontMetrics();
		int textWidth = metrics.stringWidth(text);
		g2.drawString(text, centerX - textWidth / 2, y);
	}

	private String formatTime(long elapsedMillis) {
		long totalSeconds = elapsedMillis / 1000;
		long minutes = totalSeconds / 60;
		long seconds = totalSeconds % 60;
		return String.format("%02d:%02d", minutes, seconds);
	}
}