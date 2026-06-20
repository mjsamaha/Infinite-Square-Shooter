package com.lobsterchops.infinitesquareshooter.model;

import com.lobsterchops.infinitesquareshooter.model.entity.Player;

public interface Collectible extends Collidable {

	boolean canBeCollectedBy(Player player);

	void collect(Player player, GameWorld world);

	boolean isCollected();

}