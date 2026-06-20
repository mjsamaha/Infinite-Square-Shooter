package com.lobsterchops.infinitesquareshooter.wave;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

import com.lobsterchops.infinitesquareshooter.config.GameConfig;
import com.lobsterchops.infinitesquareshooter.config.ScreenConfig;
import com.lobsterchops.infinitesquareshooter.config.types.EnemyType;
import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.model.SpawnService;

public class SpawnManager {

	private final SpawnService spawnService;
	private final Random random = new Random();
	private final Queue<PendingSpawn> pendingSpawns = new ArrayDeque<>();

	private long nextSpawnAtMs;

	public SpawnManager(SpawnService spawnService) {
		this.spawnService = spawnService;
	}

	public void begin(WaveDefinition wave, long nowMs) {
		pendingSpawns.clear();

		float spawnRateScale = spawnRateScale(wave.waveNumber());

		for (SpawnEntry entry : wave.spawns()) {
			int remaining = entry.count();

			while (remaining > 0) {
				int groupSize = Math.min(resolveGroupSize(entry, wave), remaining);
				pendingSpawns.add(new PendingSpawn(entry.type(), groupSize, scaledDelay(entry.delayBetweenGroupsMs(), spawnRateScale)));
				remaining -= groupSize;
			}
		}

		nextSpawnAtMs = nowMs + scaledDelay(wave.baseSpawnDelayMs(), spawnRateScale);
	}

	public void update(long nowMs) {
		if (pendingSpawns.isEmpty() || nowMs < nextSpawnAtMs) {
			return;
		}

		PendingSpawn spawn = pendingSpawns.poll();
		spawnGroup(spawn.type(), spawn.groupSize());
		nextSpawnAtMs = nowMs + spawn.delayAfterMs();
	}

	public boolean isFinished() {
		return pendingSpawns.isEmpty();
	}

	private void spawnGroup(EnemyType type, int groupSize) {
		Vector2 anchor = randomEdgePosition();

		for (int i = 0; i < groupSize; i++) {
			Vector2 offset = randomGroupOffset(i, groupSize);
			spawnService.spawnEnemy(type, anchor.add(offset));
		}
	}

	private Vector2 randomEdgePosition() {
		int edge = random.nextInt(4);

		return switch (edge) {
			case 0 -> new Vector2(random.nextInt(ScreenConfig.WIDTH), -30f);
			case 1 -> new Vector2(ScreenConfig.WIDTH + 30f, random.nextInt(ScreenConfig.HEIGHT));
			case 2 -> new Vector2(random.nextInt(ScreenConfig.WIDTH), ScreenConfig.HEIGHT + 30f);
			default -> new Vector2(-30f, random.nextInt(ScreenConfig.HEIGHT));
		};
	}

	private Vector2 randomGroupOffset(int index, int groupSize) {
		float angle = (float) ((Math.PI * 2.0 * index) / Math.max(1, groupSize));
		float radius = 18f + random.nextFloat() * 20f;

		return new Vector2(
				(float) Math.cos(angle) * radius,
				(float) Math.sin(angle) * radius
		);
	}

	private int resolveGroupSize(SpawnEntry entry, WaveDefinition wave) {
		if (!entry.type().name().startsWith("SWARM")) {
			return entry.groupSize();
		}

		int endlessBonus = wave.isEndless() ? Math.max(0, wave.waveNumber() - 36) / 3 : 0;
		return Math.max(entry.groupSize(), entry.groupSize() + endlessBonus);
	}

	private float spawnRateScale(int waveNumber) {
		return (float) Math.pow(GameConfig.SPAWN_RATE_SCALE_PER_WAVE, Math.max(0, waveNumber - 1));
	}

	private long scaledDelay(long delayMs, float spawnRateScale) {
		return Math.max(150L, Math.round(delayMs / spawnRateScale));
	}

	private record PendingSpawn(EnemyType type, int groupSize, long delayAfterMs) {
	}
}