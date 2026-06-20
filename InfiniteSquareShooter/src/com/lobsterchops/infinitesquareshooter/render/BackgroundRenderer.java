package com.lobsterchops.infinitesquareshooter.render;

import java.awt.Graphics2D;

import com.lobsterchops.infinitesquareshooter.config.ColorConfig;
import com.lobsterchops.infinitesquareshooter.config.ScreenConfig;

public class BackgroundRenderer {

	public void render(Graphics2D g2) {
		g2.setColor(ColorConfig.BLACK);
		g2.fillRect(0, 0, ScreenConfig.WIDTH, ScreenConfig.HEIGHT);
	}

}
