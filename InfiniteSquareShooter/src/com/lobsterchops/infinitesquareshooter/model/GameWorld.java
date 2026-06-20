package com.lobsterchops.infinitesquareshooter.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.lobsterchops.infinitesquareshooter.state.GameState;

public class GameWorld {

	private final List<GameObject> objects = new ArrayList<>();
	private final List<GameObject> pendingObjects = new ArrayList<>();

	private GameState state = GameState.PLAYING;
	private int waveNumber = 1;
	private int score = 0;

	public void update() {
		if (state != GameState.PLAYING) {
			return;
		}

		flushPendingObjects();

		for (GameObject object : objects) {
			if (object.isActive()) {
				object.update(this);
			}
		}

		removeInactiveObjects();
	}

	public void addObject(GameObject object) {
		if (object != null) {
			pendingObjects.add(object);
		}
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

	public void clear() {
		objects.clear();
		pendingObjects.clear();
		score = 0;
		waveNumber = 1;
		state = GameState.PLAYING;
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
		return score;
	}

	public void addScore(int amount) {
		score += Math.max(0, amount);
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
}