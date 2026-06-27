package com.lobsterchops.infinitesquareshooter.config;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import com.lobsterchops.infinitesquareshooter.config.types.PowerUpType;

public final class PowerUpConfig {

    // Raised from 0.12 → 0.5 so pickups appear frequently enough to feel
    // present during play. Tune back down (0.10–0.15) once satisfied.
    private static final double BASE_DROP_CHANCE = 0.5;
    private static final long EFFECT_DURATION_MS = 8000L;

    private static final Map<PowerUpType, PowerUpDefinition> DEFINITIONS = createDefinitions();

    private PowerUpConfig() {
    }

    public static double baseDropChance() {
        return BASE_DROP_CHANCE;
    }

    public static Map<PowerUpType, PowerUpDefinition> definitions() {
        return DEFINITIONS;
    }

    public static PowerUpDefinition definition(PowerUpType type) {
        return DEFINITIONS.get(type);
    }

    private static Map<PowerUpType, PowerUpDefinition> createDefinitions() {
        Map<PowerUpType, PowerUpDefinition> map = new EnumMap<>(PowerUpType.class);

        map.put(PowerUpType.EXTRA_LIFE,       new PowerUpDefinition(0.8,  Integer.MAX_VALUE, 0L,             1));
        map.put(PowerUpType.WEAPON_TIER,      new PowerUpDefinition(1.4,  Integer.MAX_VALUE, 0L,             1));
        map.put(PowerUpType.FIRE_RATE,        new PowerUpDefinition(1.8,  Integer.MAX_VALUE, EFFECT_DURATION_MS, 1));
        map.put(PowerUpType.SPEED,            new PowerUpDefinition(1.5,  Integer.MAX_VALUE, EFFECT_DURATION_MS, 1));
        map.put(PowerUpType.SCORE_MULTIPLIER, new PowerUpDefinition(1.2,  Integer.MAX_VALUE, EFFECT_DURATION_MS, 1));

        return Collections.unmodifiableMap(map);
    }

    public record PowerUpDefinition(
        double dropWeight,
        int maxStacks,
        long durationMs,
        int stackIncrease
    ) {
    }
}