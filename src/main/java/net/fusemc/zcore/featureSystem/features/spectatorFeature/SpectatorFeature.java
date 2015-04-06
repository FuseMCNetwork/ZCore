package net.fusemc.zcore.featureSystem.features.spectatorFeature;

import net.fusemc.zcore.featureSystem.Feature;
import net.fusemc.zcore.featureSystem.FeatureException;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class SpectatorFeature extends Feature{

	public SpectatorController controller;

    public boolean enable() {
        return enable(null);
    }

	public boolean enable(Location spawn) {
        if(super.enable()){
            controller = new SpectatorController(spawn);
            return true;
        }
        return false;
	}

	@Override
	public boolean disable() {
        if(super.disable()){
            controller.unregister();
            return true;
        }
        return false;
	}

    public void addSpectator(Player p) {
        addSpectator(p, false);
    }
	
	public void addSpectator(Player p, boolean showRespawnDialog){
		if(!check()){
            return;
        }
        controller.addSpectator(p, showRespawnDialog);
	}
	
	public void removeSpectator(Player p){
		if(!check()){
            return;
        }
		controller.removeSpectator(p);
	}

    public List<Player> getSpectators(){
        if(!check()){
            return null;
        }
        return controller.getSpectators();
    }

    public List<Player> getPlayingPlayers(){
        if(!check()){
            return null;
        }
        return controller.getPlayingPlayers();
    }


    private boolean check(){
        if(controller == null){
            try {
                throw new FeatureException("Feature not enabled");
            } catch (FeatureException e){
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }
}
