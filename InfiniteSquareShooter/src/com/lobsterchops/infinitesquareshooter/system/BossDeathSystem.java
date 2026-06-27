package com.lobsterchops.infinitesquareshooter.system;

import java.util.ArrayList;
import java.util.List;

import com.lobsterchops.infinitesquareshooter.model.GameObject;
import com.lobsterchops.infinitesquareshooter.model.UpdateContext;
import com.lobsterchops.infinitesquareshooter.model.entity.Boss;

/**
 * Parallel to EnemyDeathSystem — scans the object list each tick for Boss
 * instances whose phase controller has reached the dead state, handles the
 * one-shot death consequences (score, power-up drop), and marks death handled
 * so the logic never fires twice.
 *
 * BossDeathSystem intentionally does NOT advance the wave — WaveManager polls
 * world.hasActiveBoss() independently and progresses when it returns false.
 * This keeps the two concerns cleanly separated.
 */
public class BossDeathSystem {

    public void update(UpdateContext context) {
        List<GameObject> objects = new ArrayList<>(context.world().getObjects());

        for (GameObject object : objects) {
            if (!(object instanceof Boss boss)) {
                continue;
            }

            if (!boss.isDead() || boss.isDeathHandled()) {
                continue;
            }

            handleBossDeath(boss, context);
            boss.markDeathHandled();
        }
    }

    private void handleBossDeath(Boss boss, UpdateContext context) {
        // Award the boss score value via the score manager.
        context.world().getScoreManager().addBossBonus(boss.getStats().scoreValue());

        // Record as a kill in run stats — counts toward accuracy proxy.
        context.world().getRunStats().recordKill();

        // Guaranteed power-up drop on boss death — always drops a random
        // timed effect so the player enters the next wave buffed.
        rollPowerUpDrop(boss, context);
    }

    private void rollPowerUpDrop(Boss boss, UpdateContext context) {
        // Bosses always drop something — no base-chance roll needed.
        // Pick a random timed power-up from the three temporary types.
        com.lobsterchops.infinitesquareshooter.config.types.PowerUpType[] timedTypes = {
            com.lobsterchops.infinitesquareshooter.config.types.PowerUpType.FIRE_RATE,
            com.lobsterchops.infinitesquareshooter.config.types.PowerUpType.SPEED,
            com.lobsterchops.infinitesquareshooter.config.types.PowerUpType.SCORE_MULTIPLIER
        };

        int index = (int) (Math.random() * timedTypes.length);
        context.spawnService().spawnPowerUp(timedTypes[index], boss.getPosition());
    }
}