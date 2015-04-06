package net.fusemc.zcore.featureSystem.features.messageFeature;

import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.featureSystem.Feature;
import net.fusemc.zcore.featureSystem.features.spectatorFeature.SpectatorFeature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveFeature extends Feature{

    @Override
    public boolean enable(){
        return super.enable();
    }

    @Override
    public boolean disable(){
        return super.disable();
    }

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		
		e.setJoinMessage("\u00A72\u2B08 \u00A73" + p.getDisplayName());
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e){
		Player p = e.getPlayer();

        SpectatorFeature feature = ZCore.getFeatureManager().getFeature(SpectatorFeature.class);
        if(feature.isEnabled()){
            if(feature.getSpectators().contains(p)){
                e.setQuitMessage(null);
                return;
            }
        }

		e.setQuitMessage("\u00A74\u2B0B \u00A73" + p.getDisplayName());
	}

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e){
        Player p = e.getPlayer();

        SpectatorFeature feature = ZCore.getFeatureManager().getFeature(SpectatorFeature.class);
        if(feature.isEnabled()){
            if(feature.getSpectators().contains(p)){
                e.setLeaveMessage(null);
                return;
            }
        }

        e.setLeaveMessage("\u00A74\u2B0B \u00A73" + p.getDisplayName());
    }

}
