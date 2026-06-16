package com.lobsterchops.infinitesquareshooter.core;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.lobsterchops.infinitesquareshooter.config.WindowConfig;

public class Main {
	
	public static void main(String[] args) {
		
		JFrame w = new JFrame();
		
		w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		w.setResizable(false);
		w.setTitle(WindowConfig.TITLE + " v" + WindowConfig.VERSION);
		
		GamePanel gp = new GamePanel();
		
		w.add(gp);
		
		w.pack();
		w.setLocationRelativeTo(null);
		w.setVisible(true);
		
		gp.setupGame();
		gp.startGameThread();
		
	}
	
}
