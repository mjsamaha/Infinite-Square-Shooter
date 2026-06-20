package com.lobsterchops.infinitesquareshooter.model;

public interface GameObject {

	void update(GameWorld world);

	boolean isActive();

}