package com.lobsterchops.infinitesquareshooter.model.pickup;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.lobsterchops.infinitesquareshooter.config.ColorConfig;
import com.lobsterchops.infinitesquareshooter.config.types.PowerUpType;
import com.lobsterchops.infinitesquareshooter.math.Vector2;
import com.lobsterchops.infinitesquareshooter.model.GameWorld;
import com.lobsterchops.infinitesquareshooter.model.Pickup;
import com.lobsterchops.infinitesquareshooter.model.entity.Player;
import com.lobsterchops.infinitesquareshooter.utils.SpriteRegistry;

public class PowerUpPickup extends Pickup {

	private static final float PICKUP_SIZE = 16f;

	private final PowerUpType type;

	public PowerUpPickup(PowerUpType type, Vector2 position) {
		super(position, PICKUP_SIZE, PICKUP_SIZE);
		this.type = type;
	}

	@Override
	protected void applyEffect(Player player, GameWorld world) {
		switch (type) {
			case EXTRA_LIFE -> player.addLife();
			case WEAPON_TIER -> player.upgradeWeaponTier();
			case FIRE_RATE, SPEED, SCORE_MULTIPLIER -> world.getPowerUpManager().apply(type, world.getElapsedMillis());
			default -> {
			}
		}
	}

	@Override
	public void render(Graphics2D g2) {
	    BufferedImage sprite = SpriteRegistry.forPowerUp(type);

	    if (sprite != null) {
	        g2.drawImage(
	            sprite,
	            Math.round(getBounds().x()),
	            Math.round(getBounds().y()),
	            Math.round(getBounds().width()),
	            Math.round(getBounds().height()),
	            null
	        );
	    } else {
	        g2.setColor(resolveColor(type));
	        g2.fillOval(
	            Math.round(getBounds().x()),
	            Math.round(getBounds().y()),
	            Math.round(getBounds().width()),
	            Math.round(getBounds().height())
	        );
	    }
	}

	public PowerUpType getType() {
		return type;
	}

	private java.awt.Color resolveColor(PowerUpType value) {
		return switch (value) {
			case EXTRA_LIFE -> ColorConfig.PLAYER;
			case WEAPON_TIER -> ColorConfig.PLAYER_INVINCIBLE;
			case FIRE_RATE -> ColorConfig.SCORE_COLOUR;
			case SPEED -> ColorConfig.HUD_TEXT;
			case SCORE_MULTIPLIER -> ColorConfig.WAVE_ACCENT;
		};
	}
}
