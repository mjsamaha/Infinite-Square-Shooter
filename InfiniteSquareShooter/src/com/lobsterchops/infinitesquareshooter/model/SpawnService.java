package com.lobsterchops.infinitesquareshooter.model;

import java.util.List;

import com.lobsterchops.infinitesquareshooter.config.stats.ProjectileStats;
import com.lobsterchops.infinitesquareshooter.config.types.BossType;
import com.lobsterchops.infinitesquareshooter.config.types.EnemyType;
import com.lobsterchops.infinitesquareshooter.config.types.PowerUpType;
import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.model.entity.Boss;
import com.lobsterchops.infinitesquareshooter.model.entity.BossFactory;
import com.lobsterchops.infinitesquareshooter.model.entity.Enemy;
import com.lobsterchops.infinitesquareshooter.model.entity.EnemyFactory;
import com.lobsterchops.infinitesquareshooter.model.pickup.PowerUpPickup;
import com.lobsterchops.infinitesquareshooter.model.projectile.BombProjectile;
import com.lobsterchops.infinitesquareshooter.model.projectile.Projectile;
import com.lobsterchops.infinitesquareshooter.model.projectile.ProjectileFactory;
import com.lobsterchops.infinitesquareshooter.system.EnemyBehaviorSystem;

public class SpawnService {

    private final GameWorld world;
    private final ProjectileFactory projectileFactory = new ProjectileFactory();
    private final EnemyFactory enemyFactory = new EnemyFactory(new EnemyBehaviorSystem());
    private final BossFactory bossFactory = new BossFactory();

    public SpawnService(GameWorld world) {
        this.world = world;
    }

    public void spawnPlayerProjectile(Vector2 position, Vector2 direction, ProjectileStats stats) {
        List<Projectile> projectiles = projectileFactory.createPlayerProjectiles(position, direction, stats);
        for (Projectile projectile : projectiles) {
            world.addObject(projectile);
        }
    }

    public void spawnEnemyProjectiles(Vector2 position, Vector2 direction, ProjectileStats stats) {
        List<Projectile> projectiles = projectileFactory.createEnemyProjectiles(position, direction, stats);
        for (Projectile projectile : projectiles) {
            world.addObject(projectile);
        }
    }

    public void spawnEnemy(EnemyType type, Vector2 position) {
        Enemy enemy = enemyFactory.createEnemy(type, position);
        world.addObject(enemy);
    }

    public void spawnEnemyBomb(Vector2 position, Vector2 direction) {
        BombProjectile bomb = projectileFactory.createBomb(position, direction);
        world.addObject(bomb);
    }

    public void spawnPowerUp(PowerUpType type, Vector2 position) {
        world.addObject(new PowerUpPickup(type, position));
    }

    /**
     * Creates the correct Boss subclass via BossFactory, registers it with
     * the world as both a GameObject (for update/collision) and as the active
     * boss reference (for WaveManager polling and HUD rendering).
     */
    public void spawnBoss(BossType type, Vector2 position) {
        Boss boss = bossFactory.createBoss(type, position);
        world.setActiveBoss(boss);
        world.addObject(boss);
    }
}