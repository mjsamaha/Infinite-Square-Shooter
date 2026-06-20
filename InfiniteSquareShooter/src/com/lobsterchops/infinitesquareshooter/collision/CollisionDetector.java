package com.lobsterchops.infinitesquareshooter.collision;

import com.lobsterchops.infinitesquareshooter.model.Collidable;

public final class CollisionDetector {

	public static boolean intersects(Collidable first, Collidable second) {
		if (first == null || second == null) {
			return false;
		}

		return first.getBounds().intersects(second.getBounds());
	}

	private CollisionDetector() {
	}
}