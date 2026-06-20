package com.lobsterchops.infinitesquareshooter.render;

import java.awt.Graphics2D;

import com.lobsterchops.infinitesquareshooter.model.GameWorld;
import com.lobsterchops.infinitesquareshooter.model.Renderable;

public class RenderPipeline {

	private final BackgroundRenderer backgroundRenderer = new BackgroundRenderer();
	private final HudRenderer hudRenderer = new HudRenderer();
	private final DebugRenderer debugRenderer = new DebugRenderer();

	private final GameWorld world;
	private final DebugMetrics debugMetrics;

	private boolean debugEnabled;

	public RenderPipeline(GameWorld world, DebugMetrics debugMetrics) {
		this.world = world;
		this.debugMetrics = debugMetrics;
	}

	public void render(Graphics2D g2) {
		backgroundRenderer.render(g2);

		renderLayer(g2, RenderLayer.ENTITIES);
		renderLayer(g2, RenderLayer.PROJECTILES);
		renderLayer(g2, RenderLayer.PICKUPS);
		renderLayer(g2, RenderLayer.EFFECTS);

		hudRenderer.render(g2, world);

		if (debugEnabled) {
			debugRenderer.render(g2, world, debugMetrics);
		}
	}

	private void renderLayer(Graphics2D g2, RenderLayer layer) {
		for (Renderable renderable : world.getRenderableObjects()) {
			if (renderable.getRenderLayer() == layer) {
				renderable.render(g2);
			}
		}
	}

	public boolean isDebugEnabled() {
		return debugEnabled;
	}

	public void toggleDebug() {
		debugEnabled = !debugEnabled;
	}
}