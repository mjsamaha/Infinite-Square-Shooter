package com.lobsterchops.infinitesquareshooter.manager;

import com.lobsterchops.infinitesquareshooter.input.InputHandler;
import com.lobsterchops.infinitesquareshooter.model.GameWorld;
import com.lobsterchops.infinitesquareshooter.render.RenderPipeline;
import com.lobsterchops.infinitesquareshooter.state.GameState;

public class GameUpdater {

	private final GameWorld world;
	private final InputHandler inputHandler;
	private final RenderPipeline renderPipeline;
	private final Runnable restartCallback;

	public GameUpdater(GameWorld world, InputHandler inputHandler, RenderPipeline renderPipeline,
			Runnable restartCallback) {
		this.world = world;
		this.inputHandler = inputHandler;
		this.renderPipeline = renderPipeline;
		this.restartCallback = restartCallback;
	}

	public void update() {
		if (inputHandler.consumeDebugToggleRequest()) {
			renderPipeline.toggleDebug();
		}

		if (inputHandler.consumePauseToggleRequest()) {
			togglePause();
		}

		boolean restartRequested = inputHandler.consumeRestartRequest();
		if (restartRequested && world.getState() == GameState.GAME_OVER) {
			restartCallback.run();
		}

		world.update();
	}

	private void togglePause() {
		if (world.getState() == GameState.PLAYING) {
			world.setState(GameState.PAUSED);
		} else if (world.getState() == GameState.PAUSED) {
			world.setState(GameState.PLAYING);
		}
	}
}