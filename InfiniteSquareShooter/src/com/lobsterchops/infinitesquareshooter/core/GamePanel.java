package com.lobsterchops.infinitesquareshooter.core;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.lobsterchops.infinitesquareshooter.config.ColorConfig;
import com.lobsterchops.infinitesquareshooter.config.WindowConfig;

public class GamePanel extends JPanel implements Runnable {
	
	private static final long serialVersionUID = 1L;
	
	private Thread gameThread;
	
	private final GameContext context;
	private final GameLoop gameLoop;
	
	public GamePanel() {
		initializePanel();
		
		this.context = new GameContext();
		this.gameLoop = new GameLoop(
			context.getUpdater()::update,
			this::repaint
		);
		
		// this.addKeyListener(context.getInputHandler());
	}
	
	private void initializePanel() {
		this.setPreferredSize(new Dimension(WindowConfig.WIDTH, WindowConfig.HEIGHT));
		this.setBackground(ColorConfig.BLACK);
		this.setDoubleBuffered(true);
		this.setFocusable(true);
	}
	
	public void setupGame() {
		// later
	}
	
	public void startGameThread() {
		if (gameThread != null && gameThread.isAlive()) return;
		gameThread = new Thread(this, "game-thread");
		gameThread.start();
	}
	
	public void stopGameThread() {
		gameLoop.stop();
		// context.getAudioService().shutdown();
		gameThread = null;
	}
	
	@Override
	public void run() {
		gameLoop.run();
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //context.getRenderPipeline().render(g2);
        g2.dispose();
    }

    public GameContext getContext() {
        return context;
    }

}
