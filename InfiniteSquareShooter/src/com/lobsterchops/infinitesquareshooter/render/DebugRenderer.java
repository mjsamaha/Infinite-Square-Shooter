package com.lobsterchops.infinitesquareshooter.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;

import com.lobsterchops.infinitesquareshooter.math.Bounds;
import com.lobsterchops.infinitesquareshooter.model.Collidable;
import com.lobsterchops.infinitesquareshooter.model.GameObject;
import com.lobsterchops.infinitesquareshooter.model.GameWorld;

public class DebugRenderer {

	private static final Font DEBUG_FONT = new Font("Consolas", Font.PLAIN, 14);
	
	private static final int DEBUG_X = 850;
    private static final int START_Y = 24;
    private static final int LINE_HEIGHT = 18;


	public void render(Graphics2D g2, GameWorld world, DebugMetrics metrics) {
		renderDebugText(g2, world, metrics);
		renderHitboxes(g2, world);
	}

	private void renderDebugText(Graphics2D g2, GameWorld world, DebugMetrics metrics) {

        g2.setFont(DEBUG_FONT);
        g2.setColor(Color.GREEN);

        int y = START_Y;

        g2.drawString("DEBUG", DEBUG_X, y);
        y += LINE_HEIGHT;

        for (DebugLine line : buildDebugLines(world, metrics)) {
            g2.drawString(format(line), DEBUG_X, y);
            y += LINE_HEIGHT;
        }
    }

    private List<DebugLine> buildDebugLines(GameWorld world, DebugMetrics metrics) {
        return List.of(
            new DebugLine("FPS", metrics.getFps()),
            new DebugLine("Objects", world.getObjects().size()),
            new DebugLine("State", world.getState()),
            new DebugLine("Tick", world.getTick()),
            new DebugLine("Time", world.getElapsedMillis() + " ms")
        );
    }

    private String format(DebugLine line) {
        return line.label() + ": " + line.value();
    }

    private record DebugLine(String label, Object value) {}

    private void renderHitboxes(Graphics2D g2, GameWorld world) {

        g2.setColor(Color.GREEN);

        for (GameObject object : world.getObjects()) {
            renderHitboxIfNeeded(g2, object);
        }
    }

    private void renderHitboxIfNeeded(Graphics2D g2, GameObject object) {

        if (!object.isActive() || !(object instanceof Collidable collidable)) {
            return;
        }

        Bounds b = collidable.getBounds();

        g2.drawRect(
            Math.round(b.x()),
            Math.round(b.y()),
            Math.round(b.width()),
            Math.round(b.height())
        );
    }
}