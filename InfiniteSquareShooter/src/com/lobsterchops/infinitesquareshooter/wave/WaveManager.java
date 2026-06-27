package com.lobsterchops.infinitesquareshooter.wave;

import com.lobsterchops.infinitesquareshooter.config.GameConfig;
import com.lobsterchops.infinitesquareshooter.config.ScreenConfig;
import com.lobsterchops.infinitesquareshooter.config.types.BossType;
import com.lobsterchops.infinitesquareshooter.model.GameWorld;
import com.lobsterchops.infinitesquareshooter.model.UpdateContext;
import com.lobsterchops.infinitesquareshooter.math.Vector2;

public class WaveManager {

    private final GameWorld    world;
    private final SpawnManager spawnManager;

    private WaveState       state           = WaveState.PREPARING;
    private WaveDefinition  currentWave;
    private long            stateStartedAtMs;
    private BossType        pendingBoss;

    // Brief delay between all normal enemies dying and the boss spawning —
    // gives the player a moment to breathe and register what is coming.
    private static final long BOSS_INTRO_DELAY_MS = 2_000L;

    public WaveManager(GameWorld world) {
        this.world        = world;
        this.spawnManager = new SpawnManager(world.getSpawnService());
    }


    public void update(UpdateContext context) {
        long nowMs = context.elapsedMillis();

        switch (state) {
            case PREPARING -> updatePreparing(nowMs);
            case SPAWNING  -> updateSpawning(nowMs);
            case ACTIVE    -> updateActive(nowMs);
            case COMPLETE  -> updateComplete(nowMs);
            case BOSS      -> updateBoss(nowMs);
        }
    }


    private void updatePreparing(long nowMs) {
        if (currentWave == null) {
            currentWave      = WaveCatalog.getWave(world.getWaveNumber());
            stateStartedAtMs = nowMs;
        }

        if (nowMs - stateStartedAtMs < GameConfig.BETWEEN_WAVE_DELAY_MS) {
            return;
        }

        spawnManager.begin(currentWave, nowMs);
        transitionTo(WaveState.SPAWNING, nowMs);
    }

    private void updateSpawning(long nowMs) {
        spawnManager.update(nowMs);

        if (spawnManager.isFinished()) {
            transitionTo(WaveState.ACTIVE, nowMs);
        }
    }

    private void updateActive(long nowMs) {
        if (world.hasActiveEnemies()) {
            return;
        }

        pendingBoss = WaveCatalog.getBossAfterWave(currentWave.waveNumber());

        if (pendingBoss != null) {
            transitionTo(WaveState.BOSS, nowMs);
            return;
        }

        transitionTo(WaveState.COMPLETE, nowMs);
    }

    private void updateBoss(long nowMs) {
        // Phase 1: wait for the intro delay before spawning the boss.
        if (!world.hasActiveBoss() && pendingBoss != null) {
            if (nowMs - stateStartedAtMs < BOSS_INTRO_DELAY_MS) {
                return;
            }

            // Spawn the boss at the horizontal centre, 15% down from the top.
            Vector2 spawnPosition = new Vector2(
                    ScreenConfig.WIDTH  / 2f,
                    ScreenConfig.HEIGHT * 0.15f
            );
            world.getSpawnService().spawnBoss(pendingBoss, spawnPosition);
            pendingBoss = null;
            return;
        }

        // Phase 2: boss has been spawned — wait until it is dead.
        if (world.hasActiveBoss()) {
            return;
        }

        // Boss is gone — advance to wave complete.
        transitionTo(WaveState.COMPLETE, nowMs);
    }

    private void updateComplete(long nowMs) {
        // Award wave bonus before incrementing the counter.
        world.getScoreManager().addWaveBonus(world.getWaveNumber());

        world.setWaveNumber(world.getWaveNumber() + 1);
        currentWave = null;
        pendingBoss = null;
        transitionTo(WaveState.PREPARING, nowMs);
    }


    private void transitionTo(WaveState nextState, long nowMs) {
        state            = nextState;
        stateStartedAtMs = nowMs;
    }

    public void reset(long nowMs) {
        state       = WaveState.PREPARING;
        currentWave = null;
        pendingBoss = null;
        stateStartedAtMs = nowMs;
    }


    public WaveState getState() {
        return state;
    }

    public WaveDefinition getCurrentWave() {
        return currentWave;
    }
}