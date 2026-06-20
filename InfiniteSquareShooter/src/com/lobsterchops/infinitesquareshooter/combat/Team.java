package com.lobsterchops.infinitesquareshooter.combat;

public enum Team {
	
	PLAYER,
	ENEMY,
	NEUTRAL;

	public boolean isHostileTo(Team other) {
		if (this == NEUTRAL || other == NEUTRAL) {
			return false;
		}

		return this != other;
	}

}
