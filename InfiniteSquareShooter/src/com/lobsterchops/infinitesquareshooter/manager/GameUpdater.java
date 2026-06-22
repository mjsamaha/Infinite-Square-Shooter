package com.lobsterchops.infinitesquareshooter.manager;

import com.lobsterchops.infinitesquareshooter.audio.AudioService;
import com.lobsterchops.infinitesquareshooter.input.Command;
import com.lobsterchops.infinitesquareshooter.input.InputManager;
import com.lobsterchops.infinitesquareshooter.model.GameWorld;
import com.lobsterchops.infinitesquareshooter.render.RenderPipeline;
import com.lobsterchops.infinitesquareshooter.state.GameState;

public class GameUpdater {

    private final GameWorld world;
    private final InputManager input;
    private final RenderPipeline renderPipeline;
    private final AudioService audioService;
    private final Runnable restartCallback;

    public GameUpdater(
            GameWorld world,
            InputManager input,
            RenderPipeline renderPipeline,
            AudioService audioService,
            Runnable restartCallback) {

        this.world = world;
        this.input = input;
        this.renderPipeline = renderPipeline;
        this.audioService = audioService;
        this.restartCallback = restartCallback;
    }

    public void update() {
        processCommands();
        world.update();
        audioService.update();
    }

    private void processCommands() {

        Command command;

        while ((command = input.pollCommand()) != null) {

            switch (command) {

                case TOGGLE_DEBUG ->
                        renderPipeline.toggleDebug();

                case TOGGLE_PAUSE ->
                        togglePause();

                case RESTART -> {
                    if (world.getState() == GameState.GAME_OVER) {
                        restartCallback.run();
                    }
                }
            }
        }
    }

    private void togglePause() {

        if (world.getState() == GameState.PLAYING) {

            world.setState(GameState.PAUSED);
            audioService.pauseAll();

        } else if (world.getState() == GameState.PAUSED) {

            world.setState(GameState.PLAYING);
            audioService.resumeAll();
        }
    }
}