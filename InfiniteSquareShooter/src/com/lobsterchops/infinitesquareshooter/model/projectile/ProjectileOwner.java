package com.lobsterchops.infinitesquareshooter.model.projectile;

import com.lobsterchops.infinitesquareshooter.combat.Team;

public enum ProjectileOwner {

	PLAYER(Team.PLAYER),
	ENEMY(Team.ENEMY),
	NEUTRAL(Team.NEUTRAL);

	private final Team team;

	ProjectileOwner(Team team) {
		this.team = team;
	}

	public Team getTeam() {
		return team;
	}
}