package com.lobsterchops.infinitesquareshooter.core;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.lobsterchops.infinitesquareshooter.config.ColorConfig;
import com.lobsterchops.infinitesquareshooter.config.ScreenConfig;
import com.lobsterchops.infinitesquareshooter.input.InputManager;
import com.lobsterchops.infinitesquareshooter.manager.GameUpdater;
import com.lobsterchops.infinitesquareshooter.render.DebugMetrics;
import com.lobsterchops.infinitesquareshooter.render.RenderPipeline;

public class GamePanel extends JPanel implements Runnable {

    private static final long serialVersionUID = 1L;

    private Thread gameThread;
    private GameLoop gameLoop;          // built after context bootstraps
    private final GameContext context;  // kept only to call setupNewRun/restartRun

    public GamePanel() {
        initializePanel();
        this.context = new GameContext();   // registers all services

        // Now safe to resolve — everything is registered
        ServiceLocator.resolve(InputManager.class).register(this);

        this.gameLoop = new GameLoop(
            ServiceLocator.resolve(GameUpdater.class)::update,
            this::repaint,
            ServiceLocator.resolve(DebugMetrics.class)
        );
    }

    private void initializePanel() {
        this.setPreferredSize(new Dimension(ScreenConfig.WIDTH, ScreenConfig.HEIGHT));
        this.setBackground(ColorConfig.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
    }

    public void setupGame() {
        context.setupNewRun();
    }

    public void startGameThread() {
        if (gameThread != null && gameThread.isAlive()) return;
        gameThread = new Thread(this, "game-thread");
        gameThread.start();
    }

    public void stopGameThread() {
        gameLoop.stop();
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
        ServiceLocator.resolve(RenderPipeline.class).render(g2);
        g2.dispose();
    }
}