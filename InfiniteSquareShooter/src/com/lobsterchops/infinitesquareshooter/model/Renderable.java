package com.lobsterchops.infinitesquareshooter.model;

import java.awt.Graphics2D;

import com.lobsterchops.infinitesquareshooter.render.RenderLayer;

public interface Renderable {

	void render(Graphics2D g2);

	default RenderLayer getRenderLayer() {
		return RenderLayer.ENTITIES;
	}
}