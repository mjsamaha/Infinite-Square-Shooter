package com.lobsterchops.infinitesquareshooter.config;

import java.awt.Font;

public final class HudConfig {

	public static final Font HUD_FONT = new Font("Arial", Font.BOLD, 16);
	public static final Font COMBO_FONT = new Font("Arial", Font.BOLD, 14);

	// reduce magic numbers
	public static final int MARGIN_X = 20;
	public static final int LINE_START_Y = 30;
	public static final int LINE_SPACING = 25;
	public static final int COMBO_GAP_Y = 22;
	
	private HudConfig() {
		
	}

}
