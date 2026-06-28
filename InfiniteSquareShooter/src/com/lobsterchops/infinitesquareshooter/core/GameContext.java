package com.lobsterchops.infinitesquareshooter.core;

import com.lobsterchops.infinitesquareshooter.audio.AudioService;
import com.lobsterchops.infinitesquareshooter.audio.JavaSoundAudioService;
import com.lobsterchops.infinitesquareshooter.audio.SoundType;
import com.lobsterchops.infinitesquareshooter.config.ScreenConfig;
import com.lobsterchops.infinitesquareshooter.input.InputManager;
import com.lobsterchops.infinitesquareshooter.manager.GameUpdater;
import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.model.GameWorld;
import com.lobsterchops.infinitesquareshooter.model.entity.Player;
import com.lobsterchops.infinitesquareshooter.render.DebugMetrics;
import com.lobsterchops.infinitesquareshooter.render.RenderPipeline;
import com.lobsterchops.infinitesquareshooter.utils.SpriteRegistry;

public class GameContext {

    public GameContext() {
        // 1. No-dependency services first
        InputManager inputManager = new InputManager();
        GameWorld world           = new GameWorld();
        DebugMetrics debugMetrics = new DebugMetrics();

        // 2. Services that depend on the above
        RenderPipeline renderPipeline = new RenderPipeline(world, debugMetrics);

        AudioService audioService = new JavaSoundAudioService();
        audioService.init();

        // 3. Updater wires everything together
        GameUpdater updater = new GameUpdater(
            world, inputManager, renderPipeline, audioService, this::restartRun
        );

        // 4. Register — order doesn't matter here, all constructed already
        ServiceLocator.register(InputManager.class,  inputManager);
        ServiceLocator.register(GameWorld.class,     world);
        ServiceLocator.register(DebugMetrics.class,  debugMetrics);
        ServiceLocator.register(RenderPipeline.class, renderPipeline);
        ServiceLocator.register(AudioService.class,  audioService);
        ServiceLocator.register(GameUpdater.class,   updater);
        
        SpriteRegistry.load();
    }

    public void setupNewRun() {
        InputManager inputManager = ServiceLocator.resolve(InputManager.class);
        GameWorld world           = ServiceLocator.resolve(GameWorld.class);
        AudioService audioService = ServiceLocator.resolve(AudioService.class);

        Vector2 startPosition = new Vector2(
            ScreenConfig.WIDTH  / ScreenConfig.CENTER_DIVISOR,
            ScreenConfig.HEIGHT / ScreenConfig.CENTER_DIVISOR
        );
        world.setPlayer(new Player(startPosition, inputManager));
        audioService.playMusic(SoundType.MUSIC_GAMEPLAY);
    }

    public void restartRun() {
        ServiceLocator.resolve(GameWorld.class).clear();
        setupNewRun();
    }
}