package com.lobsterchops.infinitesquareshooter.render;

import java.awt.Font;
import java.awt.Graphics2D;

import com.lobsterchops.infinitesquareshooter.config.ColorConfig;
import com.lobsterchops.infinitesquareshooter.model.GameWorld;
import com.lobsterchops.infinitesquareshooter.model.entity.Player;
import com.lobsterchops.infinitesquareshooter.score.ScoreManager;

public class HudRenderer {
	
	private static final Font HUD_FONT = new Font("Arial", Font.BOLD, 16);
	private static final Font COMBO_FONT = new Font("Arial", Font.BOLD, 14);

	public void render(Graphics2D g2, GameWorld world) {
		g2.setFont(HUD_FONT);
		g2.setColor(ColorConfig.HUD_TEXT);

		Player player = world.getPlayer();
		int lives = player == null ? 0 : player.getLives();

		ScoreManager scoreManager = world.getScoreManager();

		g2.drawString("Lives: " + lives, 20, 30);
		g2.drawString("Score: " + scoreManager.getScore(), 20, 55);
		g2.drawString("Wave: " + world.getWaveNumber(), 20, 80);

		if (scoreManager.getComboCount() > 1) {
			g2.setFont(COMBO_FONT);
			g2.setColor(ColorConfig.SCORE_COLOUR);
			g2.drawString(String.format("Combo x%.1f", scoreManager.getComboMultiplier()), 20, 102);
		}
	}

}