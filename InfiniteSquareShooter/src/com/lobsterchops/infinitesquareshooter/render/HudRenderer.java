package com.lobsterchops.infinitesquareshooter.render;

import java.awt.Graphics2D;

import com.lobsterchops.infinitesquareshooter.config.ColorConfig;
import com.lobsterchops.infinitesquareshooter.config.HudConfig;
import com.lobsterchops.infinitesquareshooter.model.GameWorld;
import com.lobsterchops.infinitesquareshooter.model.entity.Player;
import com.lobsterchops.infinitesquareshooter.score.ScoreManager;

public class HudRenderer {
	
	public static final int COMBO_DISPLAY_THRESHOLD = 2;
	
	public void render(Graphics2D g2, GameWorld world) {
		
		Player player = world.getPlayer();
		int lives = player == null ? 0 : player.getLives();
		
		ScoreManager scoreManager = world.getScoreManager();
		
		g2.setFont(HudConfig.HUD_FONT);
		g2.setColor(ColorConfig.HUD_TEXT);
		
		int y = HudConfig.LINE_START_Y;
		g2.drawString("Lives: " + lives, HudConfig.MARGIN_X, y);
		y += HudConfig.LINE_SPACING;
		g2.drawString("Score: " + scoreManager.getScore(), HudConfig.MARGIN_X, y);
		y += HudConfig.LINE_SPACING;
		g2.drawString("Wave: " + world.getWaveNumber(), HudConfig.MARGIN_X, y);
		
		if (scoreManager.getComboCount() > COMBO_DISPLAY_THRESHOLD) {
			y += HudConfig.COMBO_GAP_Y;
			g2.setFont(HudConfig.COMBO_FONT);
			g2.setColor(ColorConfig.SCORE_COLOUR);
			g2.drawString(String.format("Combo x%.1f", scoreManager.getComboMultiplier()), HudConfig.MARGIN_X, y);
		}
		
	}
}