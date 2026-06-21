package com.lobsterchops.infinitesquareshooter.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.lobsterchops.infinitesquareshooter.config.ColorConfig;
import com.lobsterchops.infinitesquareshooter.config.ScreenConfig;
import com.lobsterchops.infinitesquareshooter.model.GameWorld;
import com.lobsterchops.infinitesquareshooter.score.RunStats;
import com.lobsterchops.infinitesquareshooter.score.ScoreManager;
import com.lobsterchops.infinitesquareshooter.utils.VerticalLayout;

public class GameOverOverlay {



	private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 48);
	private static final Font STAT_FONT = new Font("Arial", Font.PLAIN, 20);
	private static final Font PROMPT_FONT = new Font("Arial", Font.BOLD, 18);

	public void render(Graphics2D g2, GameWorld world) {

		drawDimOverlay(g2);

		GameOverData data = buildData(world);
		
		VerticalLayout layout = new VerticalLayout(
		        g2,
		        centerX(),
		        centerY() - 100,
		        30
		    );		

		layout.text("GAME OVER", TITLE_FONT, ColorConfig.SCORE_COLOUR);

		layout.space(10);

		layout.text("Score: " + data.score(), STAT_FONT, ColorConfig.HUD_TEXT);
		layout.text("Wave Reached: " + data.wave(), STAT_FONT, ColorConfig.HUD_TEXT);
		layout.text("Enemies Killed: " + data.kills(), STAT_FONT, ColorConfig.HUD_TEXT);
		layout.text("Accuracy: " + data.accuracy() + "%", STAT_FONT, ColorConfig.HUD_TEXT);
		layout.text("Time Survived: " + data.time(), STAT_FONT, ColorConfig.HUD_TEXT);

		layout.space(20);

		layout.text("Press R to Restart", PROMPT_FONT, ColorConfig.SCORE_COLOUR);
	}
	
	private int centerX() {
	    return ScreenConfig.WIDTH / 2;
	}
	
	private int centerY() {
	    return ScreenConfig.HEIGHT / 2;
	}
	
	private record GameOverData(int score, int wave, int kills, int accuracy, String time) {
	}

	private GameOverData buildData(GameWorld world) {

		ScoreManager score = world.getScoreManager();
		RunStats stats = world.getRunStats();

		return new GameOverData(score.getScore(), stats.getHighestWaveReached(), stats.getKills(),
				Math.round(stats.getAccuracy() * 100), formatTime(stats.getElapsedMillis()));
	}

	

	private void drawDimOverlay(Graphics2D g2) {
		g2.setColor(new Color(0, 0, 0, 190));
		g2.fillRect(0, 0, ScreenConfig.WIDTH, ScreenConfig.HEIGHT);
	}


	private String formatTime(long elapsedMillis) {
		long totalSeconds = elapsedMillis / 1000;
		long minutes = totalSeconds / 60;
		long seconds = totalSeconds % 60;
		return String.format("%02d:%02d", minutes, seconds);
	}
}