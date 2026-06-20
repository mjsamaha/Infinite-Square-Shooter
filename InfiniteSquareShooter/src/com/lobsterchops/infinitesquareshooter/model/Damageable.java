package com.lobsterchops.infinitesquareshooter.model;

public interface Damageable {
	
	void takeDamage(int damage);
	
	int getCurrentHealth();
	
	int getMaxHealth();
	
	boolean isDead();

}
