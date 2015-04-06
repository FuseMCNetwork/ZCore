package net.fusemc.zcore.featureSystem.features;

import me.michidk.DKLib.effects.ParticleEffect;
import net.fusemc.zcore.featureSystem.Feature;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;

public class BloodFeature extends Feature
{
    @Override
    public boolean enable() {
        return super.enable();
    }

    @Override
    public boolean disable() {
        return super.disable();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void bloodEffect(EntityDamageEvent e)
    {
        if (e.getEntityType() == EntityType.ITEM_FRAME || e.getEntityType() == EntityType.PAINTING) return;
        if (!e.isCancelled()) ParticleEffect.playIconCrack(e.getEntity().getLocation(), Material.REDSTONE, 0, 1, 0, 15);
    }
}
