package net.fusemc.zcore.holoAPI;

import net.fusemc.zcore.ZCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author michidk
 */

public class Holo {

    public static transient List<Holo> list = new ArrayList<>();

    private Location location;
    private String[] message;
    private HoloPart[] parts;
    private Player player;
    private int ticks;
    private boolean slider;

    private boolean alive;
    private boolean slideInit;
    private boolean destroyInit;

    /**
     * Spawn a new Holo
     */
    public Holo(Location location, String ... message) {
        this(location, checkMessage(message), null, false, -1);
    }

    /**
     * @param player the player who can see the holo
     */
    public Holo(Location location,  Player player, String ... message) {
        this(location, checkMessage(message), player, false, -1);
    }

    /**
     * @param slider if true the holo will be sliding upwarts
     */
    public Holo(Location location, boolean slider, String ... message) {
        this(location, checkMessage(message), null, slider, -1);
    }

    /**
     * @param slider if true the holo will be sliding upwarts
     */
    public Holo(Location location, Player player, boolean slider, String ... message) {
        this(location, checkMessage(message), player, slider, -1);
    }

    /**
     * @param slider slider enabled?
     * @param ticks the count of ticks, after the holo despawns
     */
    public Holo(Location location, boolean slider, int ticks, String ... message) {
        this(location, checkMessage(message), null, slider, ticks);
    }

    /**
     * @param player the player who can see the holo
     * @param slider slider enabled?
     * @param ticks the count of ticks, after the holo despawns
     */
    public Holo(Location location, Player player, boolean slider, int ticks, String ... message) {
        this(location, checkMessage(message), player, slider, ticks);
    }

    private Holo(Location location, String[] message, Player player, boolean slider, int ticks) {
        this.location = location;
        this.message = message;
        this.player = player;
        this.slider = slider;
        this.ticks = ticks;
        this.alive = true;

        init();
        list.add(this);
    }

    private static String[] checkMessage(String[] message){
        if((message == null) || message.length == 0){
            throw new IllegalArgumentException("Message cannot be null");
        }
        return message;
    }

    private void init() {
        if(this.slider && this.ticks == -1){
            this.ticks = 20;
        }
        if(this.ticks != -1){
            destroy(this.ticks);
        }
        if(slider){
            slide(this.ticks);
        }

        parts = new HoloPart[message.length];
        for(int i = 0; i < parts.length; i++){
            parts[i] = new HoloPart(location.clone().add(0, i * 0.25D, 0), message[i]);
        }

        if(this.player == null){
            for(HoloPart part: parts){
                part.display();
            }
        } else {
            for(HoloPart part: parts){
                part.display(player);
            }
        }
    }

    protected void display(Player p) {
    	if((this.player == null) || p.equals(this.player)){
            for(HoloPart part: parts){
                part.display(p);
            }
        }
    }

    /**
     * destroy the holo
     */
    public void destroy() {
        if(!alive){
            return;
        }
        if(this.player == null){
            for(HoloPart part: parts){
                part.destroy();
            }
        } else {
            for(HoloPart part: parts){
                part.destroy(this.player);
            }
        }
        list.remove(this);
        alive = false;
    }

    /**
     * let the holo slide upwarts
     * @param ticks how long should the holo slide
     */
    public void slide(int ticks) {
        if (slideInit || !alive) {
            return;
        }

        final int schedulerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(ZCore.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                if(alive){
                    addVelocity(new Vector(0, 0.1, 0));
                }
            }
        }, 0, 1);

        Bukkit.getScheduler().scheduleSyncDelayedTask(ZCore.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                Bukkit.getScheduler().cancelTask(schedulerID);
            }
        }, ticks);

        slideInit = true;
    }

    /**
     * destroy the holo after a certain time
     */
    public void destroy(int ticks)
    {
        if(destroyInit || !alive){
            return;
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(ZCore.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                destroy();
            }
        }, ticks);

        destroyInit = true;
    }

    /**
     * add velocity to the entity
     */
    public void addVelocity(Vector velocity)
    {
        if(this.player == null){
            for(HoloPart part: parts){
                part.move(velocity);
            }
        } else {
            for(HoloPart part: parts){
                part.move(velocity, player);
            }
        }
    }

    public Location getLocation()
    {
        return location;
    }

    public String[] getMessage()
    {
        return message;
    }

    public Player getPlayer()
    {
        return player;
    }

    public int getTicks()
    {
        return ticks;
    }

    public boolean isAlive()
    {
        return alive;
    }

    public boolean isSliding()
    {
        return slideInit;
    }

    public boolean isSlider()
    {
        return slider;
    }

}
