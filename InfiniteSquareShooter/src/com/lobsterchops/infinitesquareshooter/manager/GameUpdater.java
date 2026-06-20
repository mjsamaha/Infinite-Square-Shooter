package com.lobsterchops.infinitesquareshooter.manager;

import com.lobsterchops.infinitesquareshooter.input.InputHandler;
import com.lobsterchops.infinitesquareshooter.model.GameWorld;
import com.lobsterchops.infinitesquareshooter.render.RenderPipeline;

public class GameUpdater {

	private final GameWorld world;
	private final InputHandler inputHandler;
	private final RenderPipeline renderPipeline;

	public GameUpdater(GameWorld world, InputHandler inputHandler, RenderPipeline renderPipeline) {
		this.world = world;
		this.inputHandler = inputHandler;
		this.renderPipeline = renderPipeline;
	}

	public void update() {
		if (inputHandler.consumeDebugToggleRequest()) {
			renderPipeline.toggleDebug();
		}

		world.update();
	}
}