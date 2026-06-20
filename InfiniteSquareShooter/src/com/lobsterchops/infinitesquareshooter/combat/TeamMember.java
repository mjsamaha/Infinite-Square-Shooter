package com.lobsterchops.infinitesquareshooter.combat;

public interface TeamMember {

	Team getTeam();

	default boolean isHostileTo(TeamMember other) {
		return other != null && getTeam().isHostileTo(other.getTeam());
	}
}