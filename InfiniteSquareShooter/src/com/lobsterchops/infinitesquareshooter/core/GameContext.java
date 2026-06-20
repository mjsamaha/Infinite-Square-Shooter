package com.lobsterchops.infinitesquareshooter.core;

import com.lobsterchops.infinitesquareshooter.config.ScreenConfig;
import com.lobsterchops.infinitesquareshooter.config.types.EnemyType;
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
		
		world.getSpawnService().spawnEnemy(
				com.lobsterchops.infinitesquareshooter.config.types.EnemyType.BASIC_I,
				new com.lobsterchops.infinitesquareshooter.math.Vector2(120f, 120f)
		);

		world.getSpawnService().spawnEnemy(
				com.lobsterchops.infinitesquareshooter.config.types.EnemyType.TANK_I,
				new com.lobsterchops.infinitesquareshooter.math.Vector2(220f, 120f)
		);

		world.getSpawnService().spawnEnemy(
				com.lobsterchops.infinitesquareshooter.config.types.EnemyType.SHOOTER_I,
				new com.lobsterchops.infinitesquareshooter.math.Vector2(920f, 120f)
		);

		world.getSpawnService().spawnEnemy(
				com.lobsterchops.infinitesquareshooter.config.types.EnemyType.SPREAD_I,
				new com.lobsterchops.infinitesquareshooter.math.Vector2(920f, 240f)
		);

		world.getSpawnService().spawnEnemy(
				com.lobsterchops.infinitesquareshooter.config.types.EnemyType.ZIGZAG_I,
				new com.lobsterchops.infinitesquareshooter.math.Vector2(120f, 650f)
		);

		world.getSpawnService().spawnEnemy(
				com.lobsterchops.infinitesquareshooter.config.types.EnemyType.DASHER_I,
				new com.lobsterchops.infinitesquareshooter.math.Vector2(220f, 650f)
		);

		world.getSpawnService().spawnEnemy(
				com.lobsterchops.infinitesquareshooter.config.types.EnemyType.ORBITER_I,
				new com.lobsterchops.infinitesquareshooter.math.Vector2(500f, 120f)
		);

		world.getSpawnService().spawnEnemy(
				com.lobsterchops.infinitesquareshooter.config.types.EnemyType.GHOST_I,
				new com.lobsterchops.infinitesquareshooter.math.Vector2(500f, 650f)
		);

		world.getSpawnService().spawnEnemy(
				com.lobsterchops.infinitesquareshooter.config.types.EnemyType.HOMING_I,
				new com.lobsterchops.infinitesquareshooter.math.Vector2(920f, 650f)
		);

		world.getSpawnService().spawnEnemy(
				com.lobsterchops.infinitesquareshooter.config.types.EnemyType.SPLITTER_I,
				new com.lobsterchops.infinitesquareshooter.math.Vector2(360f, 120f)
		);

		world.getSpawnService().spawnEnemy(
				com.lobsterchops.infinitesquareshooter.config.types.EnemyType.BOMBER_I,
				new com.lobsterchops.infinitesquareshooter.math.Vector2(650f, 120f)
		);

		world.getSpawnService().spawnEnemy(
				com.lobsterchops.infinitesquareshooter.config.types.EnemyType.SWARM_I,
				new com.lobsterchops.infinitesquareshooter.math.Vector2(360f, 650f)
		);
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