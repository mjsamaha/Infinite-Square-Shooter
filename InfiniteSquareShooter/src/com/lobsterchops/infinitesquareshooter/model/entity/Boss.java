package com.lobsterchops.infinitesquareshooter.model.entity;

import java.awt.Graphics2D;

import com.lobsterchops.infinitesquareshooter.combat.Team;
import com.lobsterchops.infinitesquareshooter.combat.TeamMember;
import com.lobsterchops.infinitesquareshooter.config.stats.BossStats;
import com.lobsterchops.infinitesquareshooter.config.types.BossType;
import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.model.Damageable;
import com.lobsterchops.infinitesquareshooter.model.Entity;
import com.lobsterchops.infinitesquareshooter.model.UpdateContext;
import com.lobsterchops.infinitesquareshooter.render.RenderLayer;

/**
 * Abstract base for all boss entities.
 *
 * Bosses sit in their own entity hierarchy alongside Enemy — they share the
 * same interfaces (Damageable, TeamMember, Collidable, Renderable) so the
 * existing CollisionSystem and DamageSystem handle them with zero changes.
 *
 * Subclasses implement updateBehaviour() for their unique mechanics and
 * renderBoss() for their visual. The base class owns the phase controller,
 * damage routing, team membership, and render-layer assignment.
 */
public abstract class Boss extends Entity implements Damageable, TeamMember {

    protected final BossType            type;
    protected final BossStats           stats;
    protected final BossPhaseController phaseController;

    private boolean deathHandled = false;

    protected Boss(BossType type, BossStats stats, Vector2 position, float width, float height) {
        super(position, width, height);
        this.type            = type;
        this.stats           = stats;
        this.phaseController = new BossPhaseController(stats);
    }


    @Override
    public final void update(UpdateContext context) {
        phaseController.tick(context.elapsedMillis());

        if (phaseController.isDead()) {
            markInactive();
            return;
        }

        updateBehaviour(context);
    }

    /**
     * Subclass-specific update logic: movement, shooting, special mechanics.
     * Called every tick while the boss is alive.
     */
    protected abstract void updateBehaviour(UpdateContext context);


    @Override
    public final void render(Graphics2D g2) {
        renderBoss(g2);
    }

    /** Subclass draws its own visual here. */
    protected abstract void renderBoss(Graphics2D g2);

    @Override
    public RenderLayer getRenderLayer() {
        return RenderLayer.ENTITIES;
    }


    @Override
    public void takeDamage(int damage, UpdateContext context) {
        phaseController.takeDamage(damage);
    }

    @Override
    public int getCurrentHp() {
        return phaseController.getHpInCurrentPhase();
    }

    @Override
    public int getMaxHp() {
        return phaseController.getMaxHpInCurrentPhase();
    }

    @Override
    public boolean isDead() {
        return phaseController.isDead();
    }



    @Override
    public Team getTeam() {
        return Team.ENEMY;
    }


    public boolean isDeathHandled() {
        return deathHandled;
    }

    public void markDeathHandled() {
        deathHandled = true;
    }


    public BossType getType() {
        return type;
    }

    public BossStats getStats() {
        return stats;
    }

    public BossPhaseController getPhaseController() {
        return phaseController;
    }
}