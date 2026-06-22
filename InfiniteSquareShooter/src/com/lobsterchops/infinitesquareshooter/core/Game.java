package com.lobsterchops.infinitesquareshooter.core;

import javax.swing.JFrame;

import com.lobsterchops.infinitesquareshooter.config.WindowConfig;

public class Game {

    private final JFrame window;
    private final GamePanel gamePanel;

    public Game() {
        this.window    = buildWindow();
        this.gamePanel = new GamePanel();

        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
    }

    public void start() {
        window.setVisible(true);
        gamePanel.setupGame();
        gamePanel.startGameThread();
    }

    private static JFrame buildWindow() {
        JFrame frame = new JFrame();
        frame.setTitle(WindowConfig.TITLE + " v" + WindowConfig.VERSION);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        return frame;
    }
}