package net.fusemc.zcore.featureSystem;

import net.fusemc.zcore.ZCore;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class Feature implements Listener{

    private boolean enabled;

    //need to be called
    protected boolean enable(){
        if(enabled){
            return false;
        }
        Bukkit.getPluginManager().registerEvents(this, ZCore.getInstance());
        this.enabled = true;
        return true;
    }

    //need to be called
    protected boolean disable(){
        if(!enabled){
            return false;
        }
        HandlerList.unregisterAll(this);
        this.enabled = false;
        return true;
    }

    public boolean isEnabled(){
        return enabled;
    }

    public void onServerStop(){

    }
}
