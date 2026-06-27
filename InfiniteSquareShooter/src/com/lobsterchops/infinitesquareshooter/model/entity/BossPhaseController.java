package com.lobsterchops.infinitesquareshooter.model.entity;

import java.util.List;

import com.lobsterchops.infinitesquareshooter.config.stats.BossPhase;
import com.lobsterchops.infinitesquareshooter.config.stats.BossStats;

/**
 * Owns all phase-progression logic for a boss.
 *
 * Phases are defined in BossStats and consumed in order. When HP in the
 * current phase reaches zero the controller advances to the next phase (or
 * marks the boss dead if it was the last one). One tick after a transition
 * phaseJustChanged() returns false again — callers use that single-tick flag
 * to trigger spawn/visual events exactly once.
 *
 * The controller is intentionally ignorant of rendering and world state;
 * it is pure HP/phase arithmetic.
 */
public class BossPhaseController {

    private final List<BossPhase> phases;

    private int   currentPhaseIndex = 0;
    private int   hpInCurrentPhase;
    private boolean dead             = false;
    private boolean transitioning    = false;
    private boolean phaseJustChanged = false;

    // Brief window (ms) during which the boss is invulnerable after a phase
    // transition — gives the player a visual cue before pressure resumes.
    private static final long TRANSITION_INVULN_MS = 600L;
    private long transitionStartedAtMs = 0L;

    public BossPhaseController(BossStats stats) {
        this.phases = stats.phases();
        this.hpInCurrentPhase = phases.get(0).hp();
    }


    public void tick(long nowMs) {
        phaseJustChanged = false;

        if (transitioning && nowMs - transitionStartedAtMs >= TRANSITION_INVULN_MS) {
            transitioning = false;
        }
    }


    /**
     * Applies damage to the current phase. While transitioning between phases
     * the boss is invulnerable — damage is silently ignored.
     */
    public void takeDamage(int amount) {
        if (dead || transitioning) {
            return;
        }

        hpInCurrentPhase -= amount;

        if (hpInCurrentPhase <= 0) {
            advancePhase();
        }
    }

    private void advancePhase() {
        if (currentPhaseIndex + 1 >= phases.size()) {
            // Final phase exhausted — boss is dead.
            hpInCurrentPhase = 0;
            dead = true;
            phaseJustChanged = true;
        } else {
            // Move to the next phase.
            currentPhaseIndex++;
            hpInCurrentPhase = phases.get(currentPhaseIndex).hp();
            transitioning    = true;
            phaseJustChanged = true;
        }
    }


    public BossPhase getCurrentPhase() {
        return phases.get(currentPhaseIndex);
    }

    public int getCurrentPhaseIndex() {
        return currentPhaseIndex;
    }

    /** HP remaining in the current phase only. */
    public int getHpInCurrentPhase() {
        return hpInCurrentPhase;
    }

    /** Max HP of the current phase (from config). */
    public int getMaxHpInCurrentPhase() {
        return phases.get(currentPhaseIndex).hp();
    }

    /** Total phases this boss has. */
    public int getTotalPhases() {
        return phases.size();
    }

    public boolean isDead() {
        return dead;
    }

    /** True during the brief invulnerability window after a phase change. */
    public boolean isTransitioning() {
        return transitioning;
    }

    /**
     * True for exactly one tick immediately after a phase change or death.
     * Use this to trigger one-shot events (spawn children, play sound, etc.).
     */
    public boolean phaseJustChanged() {
        return phaseJustChanged;
    }
}