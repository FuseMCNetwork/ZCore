package net.fusemc.zcore.featureSystem.features.corpseFeature;

import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.featureSystem.Feature;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;
import java.util.List;

public class CorpseFeature extends Feature{

    private int timeoutTicks;
    public List<CustomPlayer> cps = new ArrayList<>();

    @Override
    public boolean enable(){
        return super.enable();
    }

    public boolean enable(int timeoutTicks) {
        if (super.enable()) {
            this.timeoutTicks = timeoutTicks;
            return true;
        }
        return false;
    }

    @Override
    public boolean disable(){
        if(super.disable()){
            for(CustomPlayer cp: cps){
                cp.world.removeEntity(cp);
            }
            return true;
        }
        return false;
    }

    @EventHandler
	public void onPlayerDeath(PlayerDeathEvent e){
		Player p = e.getEntity().getPlayer();
		Location loc = getLocation(p.getLocation());
		final CustomPlayer cp = new SleepingPlayer(loc.add(0, 0.3, 0), p.getDisplayName());
		((CraftWorld)p.getWorld()).getHandle().addEntity(cp);
		cps.add(cp);
        if (timeoutTicks > 0) {
            Bukkit.getScheduler().runTaskLater(ZCore.getInstance(), new Runnable() {
                @Override
                public void run() {
                    if (cp.world != null) {
                        cp.world.removeEntity(cp);
                    }
                    cps.remove(cp);
                }
            }, timeoutTicks);
        }
	}
	
	@EventHandler
	public void onPlayerTarget(EntityTargetEvent e){
		for(CustomPlayer cp: cps){
			if(e.getTarget().getEntityId() == cp.getId())
				e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e){
		for(CustomPlayer cp: cps){
			if(e.getEntity().getEntityId() == cp.getId()){
				e.setCancelled(true);
				return;
			}
		}
	}
	
	private Location getLocation(Location loc){
		Location blockloc = loc.getBlock().getLocation().clone();
		while(blockloc.getY() > 0){
			if(blockloc.getBlock() != null){
				if(blockloc.getBlock().getType().isSolid()){
					return blockloc.add(0, 1, 0);
				}
			}
			blockloc.add(0, -1, 0);
		}
		return blockloc;
	}
}
