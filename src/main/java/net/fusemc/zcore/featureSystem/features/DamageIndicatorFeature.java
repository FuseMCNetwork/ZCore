package net.fusemc.zcore.featureSystem.features;

import net.fusemc.zcore.featureSystem.Feature;
import net.fusemc.zcore.holoAPI.Holo;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * @author michidk
 */

public class DamageIndicatorFeature extends Feature {

    @Override
    public boolean enable() {
        return super.enable();
    }

    @Override
    public boolean disable() {
        return super.disable();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void damage(EntityDamageEvent e) {
        if (e.isCancelled()) return;
        new Holo(e.getEntity().getLocation().add(0, 1.5, 0), true, new String[]{ChatColor.RED + "+ " + (int) Math.round(e.getDamage()) });
    }
}
