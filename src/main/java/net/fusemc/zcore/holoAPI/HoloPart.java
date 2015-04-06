package net.fusemc.zcore.holoAPI;

import me.johnking.jlib.JLib;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Created by Marco on 08.07.2014.
 */
public class HoloPart {

    private Location location;
    private String message;
    private int horse;
    private int skull;

    protected HoloPart(Location location, String message){
        this.location = location;
        this.message = message;

        this.horse = HoloUtil.getFreeEID();
        this.skull = HoloUtil.getFreeEID();
    }

    public void display(Player ... players) {
        if (message == null) {
            return;
        }
        if (players.length == 0) {
            players = Bukkit.getOnlinePlayers();
        }
        Object[] packets = {
                HoloUtil.getSkullPacket(location, skull),
                HoloUtil.getHorsePacket(location, horse, message),
                HoloUtil.getAttachPacket(skull, horse)
        };
        Object armorStand = HoloUtil.getArmorStandPacket(location, skull, message);
        for (Player player : players) {
            if (player.getWorld().getName() == this.location.getWorld().getName()) {
                if(is1_8(player)) {
                    JLib.getProtocolManager().sendPacket(armorStand, player);
                } else {
                    JLib.getProtocolManager().sendPacket(packets, player);
                }
            }
        }
    }

    public void destroy(Player ... players){
        if (players.length == 0) {
            players = Bukkit.getOnlinePlayers();
        }
        Object packet = HoloUtil.getDestroyPacket(this.horse, this.skull);
        Object armorPacket = HoloUtil.getDestroyPacket(this.skull);
        for (Player player : players) {
            if (player.getWorld().getName() == this.location.getWorld().getName()) {
                if(is1_8(player)) {
                    JLib.getProtocolManager().sendPacket(armorPacket, player);
                } else {
                    JLib.getProtocolManager().sendPacket(packet, player);
                }
            }
        }
    }

    public void move(Vector vector, Player ... players){
        if (players.length == 0) {
            players = Bukkit.getOnlinePlayers();
        }
        Object packet = HoloUtil.getVelocityPacket(this.skull, vector);
        for (Player player : players) {
            if (player.getWorld().getName() == this.location.getWorld().getName()) {
                JLib.getProtocolManager().sendPacket(packet, player);
            }
        }
    }

    private static boolean is1_8(Player player) {
        return ((CraftPlayer) player).getHandle().playerConnection.networkManager.getVersion() > 5;
    }
}
