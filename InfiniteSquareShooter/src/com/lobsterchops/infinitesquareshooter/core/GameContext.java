package com.lobsterchops.infinitesquareshooter.core;

import com.lobsterchops.infinitesquareshooter.manager.GameUpdater;
import com.lobsterchops.infinitesquareshooter.model.GameWorld;

public class GameContext {

	private final GameWorld world;
	private final GameUpdater updater;

	public GameContext() {
		this.world = new GameWorld();
		this.updater = new GameUpdater(world);
	}

	public GameWorld getWorld() {
		return world;
	}

	public GameUpdater getUpdater() {
		return updater;
	}
}