package net.fusemc.zcore.util;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Copyright by michidk
 * Created: 25.08.2014.
 */
public enum Sounds {

    ERROR (Sound.ITEM_BREAK, 1f, 1f),
    SELECT (Sound.FIRE_IGNITE, 1f, 1f), //select somthing in a inventory
    CLICK (Sound.CLICK, 1f, 1f),        //use a item
    OPEN (Sound.DIG_SNOW, 1f, 0.2f),      //open a inventory
    SUCCESS (Sound.LEVEL_UP, 1f, 1f);

    private final Sound sound;
    private final float volume;
    private final float pitch;

    Sounds(Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public void play(Location location) {
        location.getWorld().playSound(location, sound, volume, pitch);
    }

    public void play(Player player) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    public void play(Location location, Player player) {
        player.playSound(location, sound, volume, pitch);
    }

    public Sound getSound() {
        return sound;
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }
}
