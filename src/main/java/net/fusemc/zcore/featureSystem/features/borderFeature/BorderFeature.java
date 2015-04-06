package net.fusemc.zcore.featureSystem.features.borderFeature;

import me.johnking.jlib.JLib;
import me.johnking.jlib.protocol.PacketType;
import me.johnking.jlib.protocol.event.PacketEvent;
import me.johnking.jlib.protocol.event.PacketListener;
import me.johnking.jlib.protocol.event.ProtocolPacket;
import me.michidk.DKLib.BlockLocation;
import me.michidk.DKLib.event.PlayerBlockMoveEvent;
import net.fusemc.zcore.featureSystem.Feature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import java.util.Set;

public class BorderFeature extends Feature implements PacketListener{

    private BorderSettings settings;
    private boolean isRegistered = false;

    public boolean enable(BorderSettings settings){
        if(super.enable()){
            if(!this.isRegistered){
                JLib.getProtocolManager().registerListener(this, PacketType.Server.BLOCK_CHANGE, PacketType.Client.BLOCK_DIG);
                this.isRegistered = true;
            }
            this.settings = settings;
            return true;
        }
        return false;
    }

    @Override
    public boolean enable() {
        return super.enable();
    }

    @Override
    public boolean disable() {
        if(super.disable()){
            BorderPlayer.disable();
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent e) {
        BorderPlayer.remove(e.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
        BorderPlayer.remove(e.getPlayer());
    }

    @EventHandler
    public void onPlayerMove(PlayerBlockMoveEvent e) {
        Player player = e.getPlayer();
        BorderPlayer bp = BorderPlayer.getBorderPlayer(player);
        BorderSettings.Shape borderShape = settings.getBorderShape();
        BorderSettings.Shape playerShape = settings.getPlayerShape();
        Set<BlockLocation> playerCollisionBlocks = playerShape.calculateContainedBlocks(BlockLocation.fromLocation(player.getLocation()), settings.getPlayerRadius());
        Set<BlockLocation> realCollidedBlocks = borderShape.calculateCollidingBlocks(settings.getCenter(), settings.getBorderRadius(), playerCollisionBlocks);
        bp.setBorderedBlocks(realCollidedBlocks, settings.getBorderMaterial(), settings.getBorderData());
        if(!borderShape.inArea(settings.getCenter(), settings.getBorderRadius(), e.getTo())){
            e.setCancelled(true);
        }
    }

    @Override
    public void onPacket(PacketEvent packetEvent) {
        if(!this.isEnabled()){
            return;
        }
        BorderPlayer bp = BorderPlayer.getBorderPlayer(packetEvent.getPlayer());
        ProtocolPacket packet = packetEvent.getPacket();
        int locX = packet.getInt(0); int locY = packet.getInt(1); int locZ = packet.getInt(2);

        if(packet.getPacketType() == PacketType.Server.BLOCK_CHANGE){
            Set<BlockLocation> blockLocations = bp.getBorderBlocks();
            for(BlockLocation blockLocation: blockLocations){
                if(blockLocation.getX() == locX && blockLocation.getY() == locY && blockLocation.getZ() == locZ){
                    Vector vec = new Vector(locX, locY, locZ);
                    if(!bp.getNewBlocks().contains(vec)){
                        packetEvent.setCancelled(true);
                    } else {
                        bp.getNewBlocks().remove(vec);
                    }
                    return;
                }
            }
        } else if (packet.getPacketType() == PacketType.Client.BLOCK_DIG){
            Set<BlockLocation> blockLocations = bp.getBorderBlocks();
            for(BlockLocation blockLocation: blockLocations){
                if(blockLocation.getX() == locX && blockLocation.getY() == locY && blockLocation.getZ() == locZ){
                    bp.getNewBlocks().add(new Vector(locX, locY, locZ));
                    packetEvent.getPlayer().sendBlockChange(blockLocation.toLocation(), settings.getBorderMaterial(), settings.getBorderData());
                    packetEvent.setCancelled(true);
                    return;
                }
            }
        }
    }
}
