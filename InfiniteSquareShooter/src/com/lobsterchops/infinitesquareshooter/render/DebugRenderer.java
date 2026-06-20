package com.lobsterchops.infinitesquareshooter.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.lobsterchops.infinitesquareshooter.math.Bounds;
import com.lobsterchops.infinitesquareshooter.model.Collidable;
import com.lobsterchops.infinitesquareshooter.model.GameObject;
import com.lobsterchops.infinitesquareshooter.model.GameWorld;

public class DebugRenderer {

	private static final Font DEBUG_FONT = new Font("Consolas", Font.PLAIN, 14);

	public void render(Graphics2D g2, GameWorld world, DebugMetrics metrics) {
		renderDebugText(g2, world, metrics);
		renderHitboxes(g2, world);
	}

	private void renderDebugText(Graphics2D g2, GameWorld world, DebugMetrics metrics) {
		g2.setFont(DEBUG_FONT);
		g2.setColor(Color.GREEN);

		int y = 24;
		g2.drawString("DEBUG", 850, y);
		y += 18;
		g2.drawString("FPS: " + metrics.getFps(), 850, y);
		y += 18;
		g2.drawString("Objects: " + world.getObjects().size(), 850, y);
		y += 18;
		g2.drawString("State: " + world.getState(), 850, y);
		y += 18;
		g2.drawString("Tick: " + world.getTick(), 850, y);
		y += 18;
		g2.drawString("Time: " + world.getElapsedMillis() + " ms", 850, y);
	}

	private void renderHitboxes(Graphics2D g2, GameWorld world) {
		g2.setColor(Color.GREEN);

		for (GameObject object : world.getObjects()) {
			if (object instanceof Collidable collidable && object.isActive()) {
				Bounds bounds = collidable.getBounds();
				
				// Draw hitbox outline
				g2.drawRect(
						Math.round(bounds.x()),
						Math.round(bounds.y()),
						Math.round(bounds.width()),
						Math.round(bounds.height())
				);
			}
		}
	}
}