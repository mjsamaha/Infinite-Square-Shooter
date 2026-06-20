package com.lobsterchops.infinitesquareshooter.core;

import com.lobsterchops.infinitesquareshooter.config.ScreenConfig;
import com.lobsterchops.infinitesquareshooter.input.InputHandler;
import com.lobsterchops.infinitesquareshooter.manager.GameUpdater;
import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.model.GameWorld;
import com.lobsterchops.infinitesquareshooter.model.entity.Player;
import com.lobsterchops.infinitesquareshooter.render.DebugMetrics;
import com.lobsterchops.infinitesquareshooter.render.RenderPipeline;

public class GameContext {

	private final InputHandler inputHandler;
	private final GameWorld world;
	private final GameUpdater updater;
	
	private final DebugMetrics debugMetrics;
	private final RenderPipeline renderPipeline;

	public GameContext() {
		this.inputHandler = new InputHandler();
		this.world = new GameWorld();
		this.debugMetrics = new DebugMetrics();
		this.renderPipeline = new RenderPipeline(world, debugMetrics);
		this.updater = new GameUpdater(world, inputHandler, renderPipeline);
	}

	public void setupNewRun() {
		Vector2 startPosition = new Vector2(ScreenConfig.WIDTH / 2f, ScreenConfig.HEIGHT / 2f);
		Player player = new Player(startPosition, inputHandler);
		world.setPlayer(player);
	}

	public InputHandler getInputHandler() {
		return inputHandler;
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