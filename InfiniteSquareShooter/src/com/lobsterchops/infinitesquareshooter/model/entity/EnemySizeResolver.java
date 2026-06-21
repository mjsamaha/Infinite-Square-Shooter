package com.lobsterchops.infinitesquareshooter.model.entity;

import com.lobsterchops.infinitesquareshooter.config.types.EnemyType;

public final class EnemySizeResolver {

	public static float sizeFor(EnemyType type) {
		String name = type.name();

		if (name.startsWith("SWARM"))
			return 16f;
		if (name.startsWith("TANK"))
			return 42f;
		if (name.startsWith("BOMBER"))
			return 36f;
		if (name.startsWith("SPLITTER"))
			return 34f;
		if (name.startsWith("DASHER"))
			return 26f;

		return 28f;
	}

	private EnemySizeResolver() {
	}
}