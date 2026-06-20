package com.lobsterchops.infinitesquareshooter.manager;

import com.lobsterchops.infinitesquareshooter.model.GameWorld;

public class GameUpdater {

	private final GameWorld world;

	public GameUpdater(GameWorld world) {
		this.world = world;
	}

	public void update() {
		world.update();
	}
}