package net.fusemc.zcore.featureSystem.features.borderFeature;

import me.michidk.DKLib.BlockLocation;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;
public class BorderPlayer {

    private static Map<UUID, BorderPlayer> borderPlayers = new HashMap<>();

    public static BorderPlayer getBorderPlayer(Player player) {
        BorderPlayer bp = borderPlayers.get(player.getUniqueId());
        if (bp == null) {
            bp = new BorderPlayer(player.getUniqueId());
            borderPlayers.put(player.getUniqueId(), bp);
        }
        return bp;
    }

    public static void remove(Player player) {
        borderPlayers.remove(player.getUniqueId());
    }

    public static void disable() {
        for (BorderPlayer bp : borderPlayers.values()) {
            //kill
            bp.disablePlayer();
        }
        borderPlayers.clear();
    }

    private UUID uniqueId;

    private Set<BlockLocation> borderBlocks = new HashSet<>();
    private Set<Vector> newBlocks = new HashSet<>();

    public BorderPlayer(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void disablePlayer() {
        for (BlockLocation bl : borderBlocks) {
            Block block = bl.toBlock();
            //noinspection deprecation
            getPlayer().sendBlockChange(bl.toLocation(), block.getType(), block.getData());
        }
        borderBlocks.clear();
    }

    public void setBorderedBlocks(Set<BlockLocation> bordered, Material material, byte data) {
        for (BlockLocation bl : bordered) {
            // send material
            //noinspection deprecation
            getPlayer().sendBlockChange(bl.toLocation(), material, data);
        }

        Set<BlockLocation> oldBlocks = borderBlocks;
        borderBlocks = bordered;

        newBlocks = new HashSet<>();

        for(BlockLocation bl: borderBlocks){
            if(!oldBlocks.contains(bl)){
                newBlocks.add(new Vector(bl.getX(), bl.getY(), bl.getZ()));
            }
        }

        for (BlockLocation bl : oldBlocks) {
            if (!bordered.contains(bl)) {
                // remove
                Block block = bl.toBlock();
                //noinspection deprecation
                getPlayer().sendBlockChange(bl.toLocation(), block.getType(), block.getData());
            }
        }
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uniqueId);
    }

    public Set<BlockLocation> getBorderBlocks(){
        return borderBlocks;
    }

    public Set<Vector> getNewBlocks(){
        return newBlocks;
    }
}
