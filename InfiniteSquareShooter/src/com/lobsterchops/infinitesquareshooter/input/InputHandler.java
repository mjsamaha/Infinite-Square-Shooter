package com.lobsterchops.infinitesquareshooter.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import com.lobsterchops.infinitesquareshooter.math.Vector2;

public class InputHandler implements KeyListener, MouseMotionListener {

	private boolean upPressed;
	private boolean downPressed;
	private boolean leftPressed;
	private boolean rightPressed;
	private boolean pausePressed;
	private boolean restartPressed;
	
	private boolean debugToggleRequested;

	private Vector2 mousePosition = Vector2.ZERO;

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();

		if (code == KeyEvent.VK_W) upPressed = true;
		if (code == KeyEvent.VK_S) downPressed = true;
		if (code == KeyEvent.VK_A) leftPressed = true;
		if (code == KeyEvent.VK_D) rightPressed = true;
		if (code == KeyEvent.VK_ESCAPE) pausePressed = true;
		if (code == KeyEvent.VK_R) restartPressed = true;
		if (code == KeyEvent.VK_F3) debugToggleRequested = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();

		if (code == KeyEvent.VK_W) upPressed = false;
		if (code == KeyEvent.VK_S) downPressed = false;
		if (code == KeyEvent.VK_A) leftPressed = false;
		if (code == KeyEvent.VK_D) rightPressed = false;
		if (code == KeyEvent.VK_ESCAPE) pausePressed = false;
		if (code == KeyEvent.VK_R) restartPressed = false;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mousePosition = new Vector2(e.getX(), e.getY());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public Vector2 movementDirection() {
		float x = 0f;
		float y = 0f;

		if (leftPressed) x--;
		if (rightPressed) x++;
		if (upPressed) y--;
		if (downPressed) y++;

		return new Vector2(x, y).normalized();
	}
	
	public boolean consumeDebugToggleRequest() {
		if (!debugToggleRequested) {
			return false;
		}

		debugToggleRequested = false;
		return true;
	}

	public Vector2 getMousePosition() {
		return mousePosition;
	}

	public boolean isPausePressed() {
		return pausePressed;
	}

	public boolean isRestartPressed() {
		return restartPressed;
	}
}