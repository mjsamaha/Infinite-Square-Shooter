package com.lobsterchops.infinitesquareshooter.model.entity;

import com.lobsterchops.infinitesquareshooter.config.ConfigRegistry;
import com.lobsterchops.infinitesquareshooter.config.stats.BossStats;
import com.lobsterchops.infinitesquareshooter.config.types.BossType;
import com.lobsterchops.infinitesquareshooter.math.Vector2;

/**
 * Single creation point for all Boss subclasses.
 *
 * Mirrors EnemyFactory — callers pass a BossType and spawn position; the
 * factory resolves stats from ConfigRegistry and constructs the right subclass.
 * No game logic lives here; this is pure wiring.
 */
public class BossFactory {

    public Boss createBoss(BossType type, Vector2 position) {
        BossStats stats = ConfigRegistry.boss(type);

        return switch (type) {
            case SWARM_QUEEN    -> new SwarmQueenBoss(stats, position);
            case FORTRESS       -> new FortressBoss(stats, position);
            case SPLITTER_KING  -> new SplitterKingBoss(stats, position, 0);
            case PHANTOM        -> new PhantomBoss(stats, position);
            case MIMIC          -> new MimicBoss(stats, position);
        };
    }
}