package com.lobsterchops.infinitesquareshooter.system;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import com.lobsterchops.infinitesquareshooter.config.PowerUpConfig;
import com.lobsterchops.infinitesquareshooter.config.PowerUpConfig.PowerUpDefinition;
import com.lobsterchops.infinitesquareshooter.config.types.PowerUpType;

public class PowerUpManager {

	private final Map<PowerUpType, ActiveEffect> activeEffects = new EnumMap<>(PowerUpType.class);

	public void apply(PowerUpType type, long nowMs) {
		PowerUpDefinition definition = PowerUpConfig.definition(type);
		if (definition == null || definition.durationMs() <= 0L) {
			return;
		}

		ActiveEffect current = activeEffects.get(type);
		int nextStacks = (current == null ? 0 : current.stacks()) + definition.stackIncrease();
		activeEffects.put(type, new ActiveEffect(nextStacks, nowMs + definition.durationMs()));
	}

	public void update(long nowMs) {
		activeEffects.entrySet().removeIf(entry -> entry.getValue().expiresAtMs() <= nowMs);
	}

	public void reset() {
		activeEffects.clear();
	}

	public int getStacks(PowerUpType type) {
		ActiveEffect effect = activeEffects.get(type);
		return effect == null ? 0 : effect.stacks();
	}

	public long getRemainingMs(PowerUpType type, long nowMs) {
		ActiveEffect effect = activeEffects.get(type);
		if (effect == null) {
			return 0L;
		}
		return Math.max(0L, effect.expiresAtMs() - nowMs);
	}

	public float fireRateMultiplier() {
		return 1f + (getStacks(PowerUpType.FIRE_RATE) * 0.2f);
	}

	public float speedMultiplier() {
		return 1f + (getStacks(PowerUpType.SPEED) * 0.15f);
	}

	public float scoreMultiplier() {
		return 1f + (getStacks(PowerUpType.SCORE_MULTIPLIER) * 0.25f);
	}

	public Map<PowerUpType, ActiveEffect> snapshot() {
		return Collections.unmodifiableMap(new EnumMap<>(activeEffects));
	}

	public record ActiveEffect(int stacks, long expiresAtMs) {
	}
}
