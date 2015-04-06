package net.fusemc.zcore.featureSystem.features;

import net.fusemc.zcore.featureSystem.Feature;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.WorldLoadEvent;

public class AntiWorldTransformFeature extends Feature
{

    @Override
    public boolean enable() {
        for (World w : Bukkit.getWorlds())
        {
            w.setGameRuleValue("doFireTick", "false");
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
        for (World w : Bukkit.getWorlds())
        {
            w.setGameRuleValue("doDaylightCycle", "false");
        }
    }

    @EventHandler
    public void onPhysics(BlockPhysicsEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockFormTo(BlockFromToEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onMelting(BlockFadeEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler
    public void onGrow(BlockGrowEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler
    public void onIngnite(BlockIgniteEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onTrample(PlayerInteractEvent e)
    {
        if (e.getAction() != Action.PHYSICAL) return;
        if (e.getClickedBlock().getType().equals(Material.SOIL)) e.setCancelled(true);
    }

}
