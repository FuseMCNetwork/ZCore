package net.fusemc.zcore.holoAPI;

import me.johnking.jlib.JLib;
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

public class HoloOld
{
    public static transient List<HoloOld> list = new ArrayList<>();

    private Location location;
    private String message;
    private Player player;
    private int ticks;
    private boolean slider;

    private int skull;
    private int horse;
    private boolean alive;
    private boolean sliding;

    /**
     * Spawn a new Holo
     */
    public HoloOld(Location location, String message)
    {
        this.location = location;
        this.message = message;

        spawn();
        list.add(this);
    }

    /**
     * @param player the player who can see the holo
     */
    public HoloOld(Location location, String message, Player player)
    {
        this.location = location;
        this.message = message;
        this.player = player;

        spawn();
        list.add(this);
    }

    /**
     * @param slider if true the holo will be sliding upwarts
     */
    public HoloOld(Location location, String message, boolean slider)
    {
        this.location = location;
        this.message = message;
        this.slider = slider;

        spawn();
        list.add(this);

        if (slider)
        {
            slide(10);
            destroy(10);
        }
    }

    /**
     * @param slider if true the holo will be sliding upwarts
     */
    public HoloOld(Location location, String message, Player player, boolean slider)
    {
        this.location = location;
        this.message = message;
        this.slider = slider;
        this.player = player;

        spawn();
        list.add(this);

        if (slider)
        {
            slide(10);
            destroy(10);
        }
    }

    /**
     * @param ticks the count of ticks, after the holo despawns
     */
    public HoloOld(Location location, String message, int ticks)
    {
        this.location = location;
        this.message = message;
        this.ticks = ticks;

        spawn();
        list.add(this);

        destroy(ticks);
    }

    /**
     * @param player the player who can see the holo
     * @param ticks the count of ticks, after the holo despawns
     */
    public HoloOld(Location location, String message, Player player, int ticks)
    {
        this.location = location;
        this.message = message;
        this.player = player;
        this.ticks = ticks;

        spawn();
        list.add(this);

        destroy(ticks);
    }

    /**
     * spawn the holo (called by constructor)
     */
    private void spawn()
    {
        if (alive == true) return;

        this.skull = HoloUtil.getFreeEID();
        this.horse = HoloUtil.getFreeEID();

        Object[] packets = {
        		HoloUtil.getSkullPacket(location, skull),
        		HoloUtil.getHorsePacket(location, horse, message),
        		HoloUtil.getAttachPacket(skull, horse)
        };
        if(this.player == null){
            for (Player p:Bukkit.getOnlinePlayers())
            {

                if (p.getWorld().getName() == this.location.getWorld().getName())
                {
                    JLib.getProtocolManager().sendPacket(packets, p);
                }

            }
        } else {
            if (player.getWorld().getName() == this.location.getWorld().getName())
            {
                JLib.getProtocolManager().sendPacket(packets, player);
            }
        }

        alive = true;
    }

    /**
     * send the holo to a player (if a new player joins the server)
     */
    protected void send(Player p)
    {
    	Object[] packets = {
        		HoloUtil.getSkullPacket(location, skull),
        		HoloUtil.getHorsePacket(location, horse, message),
        		HoloUtil.getAttachPacket(skull, horse)
        };
        JLib.getProtocolManager().sendPacket(packets, p);
    }

    /**
     * destroy the holo
     */
    public void destroy()
    {
        if (alive == false) {
            return;
        }
        
        if(this.player == null){
        	JLib.getProtocolManager().sendPacket(HoloUtil.getDestroyPacket(this.horse, this.skull));
        } else {
            JLib.getProtocolManager().sendPacket(HoloUtil.getDestroyPacket(this.horse, this.skull), player);
        }
        
        alive = false;
    }

    /**
     * destroy the holo for a certain player
     */
    public void destroy(Player player)
    {
        if (alive == false) {
            return;
        }

        JLib.getProtocolManager().sendPacket(HoloUtil.getDestroyPacket(this.horse, this.skull), player);
    }

    /**
     * let the holo slide upwarts
     * @param ticks how long should the holo slide
     */
    public void slide(int ticks)
    {
        if (sliding)
        {
            return;
        }

        final int schedulerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(ZCore.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                addVelocity(new Vector(0, 0.1, 0));
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

        sliding = true;
    }

    /**
     * destroy the holo after a certain time
     */
    public void destroy(int ticks)
    {
        Bukkit.getScheduler().scheduleSyncDelayedTask(ZCore.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                destroy();
            }
        }, ticks);
    }

    /**
     * add velocity to the entity
     */
    public void addVelocity(Vector velocity)
    {
        Object packet = HoloUtil.getVelocityPacket(skull, velocity);
        if(this.player == null){
            JLib.getProtocolManager().sendPacket(packet);
        } else {
            JLib.getProtocolManager().sendPacket(packet, player);
        }
    }

    public Location getLocation()
    {
        return location;
    }

    public String getMessage()
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
        return sliding;
    }

    public boolean isSlider()
    {
        return slider;
    }

}
