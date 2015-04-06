package net.fusemc.zcore.featureSystem.features;

import net.fusemc.zcore.featureSystem.Feature;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldLoadEvent;

/**
 * Created by michidk on 01.08.2014.
 */
public class NoWeatherFeature extends Feature
{

    @Override
    public boolean enable() {
        for (World w : Bukkit.getWorlds())
        {
            w.setStorm(false);
            w.setThundering(false);
            w.setTime(9000);
        }

        return super.enable();
    }

    @Override
    public boolean disable() {
        return super.disable();
    }

    @EventHandler
    public void onWorldLooad(WorldLoadEvent e)
    {
        World w = e.getWorld();

        w.setStorm(false);
        w.setThundering(false);
        w.setTime(9000);
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent e)
    {
        if (e.toWeatherState()) e.setCancelled(true);
    }

    @EventHandler
    public void onThunder(ThunderChangeEvent e)
    {
        if (e.toThunderState()) e.setCancelled(true);
    }

    @EventHandler
    public void onLightning(LightningStrikeEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent e)
    {
        if (e.getNewState().getType().equals(Material.ICE) || e.getNewState().getType().equals(Material.SNOW))
        {
            e.setCancelled(true);
        }
    }
}
