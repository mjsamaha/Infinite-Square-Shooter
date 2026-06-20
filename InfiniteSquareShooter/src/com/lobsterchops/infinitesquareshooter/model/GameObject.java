package com.lobsterchops.infinitesquareshooter.model;

public interface GameObject {

	void update(UpdateContext context);

	boolean isActive();

}