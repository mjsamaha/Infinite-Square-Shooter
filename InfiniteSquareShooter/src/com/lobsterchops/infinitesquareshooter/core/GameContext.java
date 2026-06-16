package com.lobsterchops.infinitesquareshooter.core;

import com.lobsterchops.infinitesquareshooter.manager.GameUpdater;

public class GameContext {
	
	private final GameUpdater updater = new GameUpdater();
	
	public GameUpdater getUpdater() {
		return updater;
	}

}
