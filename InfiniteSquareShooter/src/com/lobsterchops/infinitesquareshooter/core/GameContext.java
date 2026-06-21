package com.lobsterchops.infinitesquareshooter.core;

import com.lobsterchops.infinitesquareshooter.config.ScreenConfig;
import com.lobsterchops.infinitesquareshooter.input.InputHandler;
import com.lobsterchops.infinitesquareshooter.input.InputManager;
import com.lobsterchops.infinitesquareshooter.manager.GameUpdater;
import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.model.GameWorld;
import com.lobsterchops.infinitesquareshooter.model.entity.Player;
import com.lobsterchops.infinitesquareshooter.render.DebugMetrics;
import com.lobsterchops.infinitesquareshooter.render.RenderPipeline;

public class GameContext {

	private final InputManager inputManager;
	private final GameWorld world;
	private final GameUpdater updater;
	
	private final DebugMetrics debugMetrics;
	private final RenderPipeline renderPipeline;

	public GameContext() {
		this.inputManager = new InputManager();
		this.world = new GameWorld();
		this.debugMetrics = new DebugMetrics();
		this.renderPipeline = new RenderPipeline(world, debugMetrics);
		this.updater = new GameUpdater(world, inputManager, renderPipeline, this::restartRun);
	}
	
	public void setupNewRun() {
		Vector2 startPosition = new Vector2(ScreenConfig.WIDTH / 2f, ScreenConfig.HEIGHT / 2f);
		Player player = new Player(startPosition, inputManager);
		world.setPlayer(player);
	}

	public void restartRun() {
		world.clear();
		setupNewRun();
	}
	
	public void registerInput(GamePanel panel) {

	    inputManager.register(panel);
	}

	public InputManager getInputManager() {

        return inputManager;
    }

	public GameWorld getWorld() {
		return world;
	}

	public GameUpdater getUpdater() {
		return updater;
	}

	public RenderPipeline getRenderPipeline() {
		return renderPipeline;
	}

	public DebugMetrics getDebugMetrics() {
		return debugMetrics;
	}
}