package com.lobsterchops.infinitesquareshooter.model;

public interface Damageable {
	
	void takeDamage(int damage, UpdateContext context);
	
	int getCurrentHealth();
	
	int getMaxHealth();
	
	boolean isDead();

}
