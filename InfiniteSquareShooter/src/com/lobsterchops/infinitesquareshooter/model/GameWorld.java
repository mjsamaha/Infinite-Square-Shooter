package com.lobsterchops.infinitesquareshooter.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.lobsterchops.infinitesquareshooter.collision.CollisionSystem;
import com.lobsterchops.infinitesquareshooter.combat.DamageSystem;
import com.lobsterchops.infinitesquareshooter.config.GameConfig;
import com.lobsterchops.infinitesquareshooter.config.types.EnemyType;
import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.model.entity.Enemy;
import com.lobsterchops.infinitesquareshooter.model.entity.Player;
import com.lobsterchops.infinitesquareshooter.score.RunStats;
import com.lobsterchops.infinitesquareshooter.score.ScoreManager;
import com.lobsterchops.infinitesquareshooter.state.GameState;
import com.lobsterchops.infinitesquareshooter.system.EnemyDeathSystem;
import com.lobsterchops.infinitesquareshooter.wave.WaveManager;

public class GameWorld {

	private final List<GameObject> objects = new ArrayList<>();
	private final List<GameObject> pendingObjects = new ArrayList<>();
	
	private final SpawnService spawnService = new SpawnService(this);
	private final CollisionSystem collisionSystem = new CollisionSystem(new DamageSystem());
	private final EnemyDeathSystem enemyDeathSystem = new EnemyDeathSystem();
	
	private final WaveManager waveManager = new WaveManager(this);
	private final ScoreManager scoreManager = new ScoreManager();
	private final RunStats runStats = new RunStats();

	private Player player;
	private GameState state = GameState.PLAYING;
	private int waveNumber = 1;
	
	private long tick;
	private long elapsedMillis;

	public void update() {
		if (state != GameState.PLAYING) {
			return;
		}
		
		// Process pending objects before the update to ensure they are included in the current tick.
		beginUpdate();
		
		// Update time and meta-systems before updating objects and systems to ensure they have the latest context.
		updateTime();
		updateMetaSystems();
		
		// Create a single context object to pass to all updates to ensure consistency across the update cycle.
		UpdateContext context = createContext();
		
		// Update all game objects and systems using the same context to ensure they are synchronized with the current tick and elapsed time.
		updateObjects(context);
		updateSystems(context);
		
		// Process pending objects again after the update to ensure any new objects created during the update are included in the next tick.
		endUpdate(context);


	}
	
	private void beginUpdate() {
		flushPendingObjects();
	}
	
	private void endUpdate(UpdateContext context) {
		flushPendingObjects();
		removeInactiveObjects();
	}
	
	
	private void updateTime() {
		tick++;
		elapsedMillis += Math.round(
				GameConfig.MILLIS_PER_SECOND / GameConfig.TARGET_FPS
			);
	}
	
	private void updateMetaSystems() {
		runStats.sync(this);
		scoreManager.tick(elapsedMillis);
	}
	
	private UpdateContext createContext() {
		return UpdateContext.fixed(this, tick, elapsedMillis);
	}
	
	private void updateObjects(UpdateContext context) {
		for (GameObject object : objects) {
			if (object.isActive()) {
				object.update(context);
			}
		}
	}
	
	private void updateSystems(UpdateContext context) {
		collisionSystem.update(context);
		enemyDeathSystem.update(context);
		
		if (player != null) {
			player.handleDeath(this);
		}
		
		waveManager.update(context);
	}

	

	public void addObject(GameObject object) {
		if (object != null) {
			pendingObjects.add(object);
		}
	}

	public void setPlayer(Player player) {
		this.player = player;
		addObject(player);
	}

	public Player getPlayer() {
		return player;
	}

	public SpawnService getSpawnService() {
		return spawnService;
	}

	public List<GameObject> getObjects() {
		return Collections.unmodifiableList(objects);
	}

	public List<Renderable> getRenderableObjects() {
		List<Renderable> renderables = new ArrayList<>();

		for (GameObject object : objects) {
			if (object instanceof Renderable renderable && object.isActive()) {
				renderables.add(renderable);
			}
		}

		return renderables;
	}
	
	public boolean hasActiveEnemies() {
		for (GameObject object : objects) {
			if (object instanceof Enemy && object.isActive()) {
				return true;
			}
		}

		return false;
	}

	public void clear() {
		objects.clear();
		pendingObjects.clear();
		player = null;
		waveNumber = 1;
		tick = 0;
		elapsedMillis = 0;
		state = GameState.PLAYING;
		scoreManager.reset();
		runStats.reset();
		waveManager.reset(elapsedMillis);
	}
	
	public void spawnTestEnemy() {
		spawnService.spawnEnemy(
				EnemyType.BASIC_I,
				new Vector2 (100f, 100f)
		);
	}
	


	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public int getWaveNumber() {
		return waveNumber;
	}

	public void setWaveNumber(int waveNumber) {
		this.waveNumber = waveNumber;
	}

	public int getScore() {
		return scoreManager.getScore();
	}

	public void addScore(int amount) {
		scoreManager.addBonus(amount);
	}

	public ScoreManager getScoreManager() {
		return scoreManager;
	}

	public RunStats getRunStats() {
		return runStats;
	}

	private void flushPendingObjects() {
		if (pendingObjects.isEmpty()) {
			return;
		}

		objects.addAll(pendingObjects);
		pendingObjects.clear();
	}

	private void removeInactiveObjects() {
		objects.removeIf(object -> !object.isActive());
	}
	
	public long getTick() {
		return tick;
	}

	public long getElapsedMillis() {
		return elapsedMillis;
	}
	
	public WaveManager getWaveManager() {
		return waveManager;
	}
}