package net.fusemc.zcore.featureSystem.features.disguiseFeature;

import me.johnking.jlib.JLib;
import me.johnking.jlib.protocol.PacketType;
import me.johnking.jlib.protocol.util.EntityUtilities;
import me.johnking.jlib.protocol.wrappers.WrappedGameProfile;
import net.fusemc.zcore.util.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Marco on 20.08.2014.
 */
public class DisguiseHelper {

    private static final Class<?> ENTITY_HUMAN_CLASS = ReflectionUtil.getMinecraftClass("EntityHuman");

    public void changeName(Player player, String name, boolean sendPackets) {
        try {
            //replace name in game_profile
            String oldName = player.getName();
            WrappedGameProfile profile = new WrappedGameProfile(player);
            profile.setName(name);
            player.setDisplayName(name);
            player.setPlayerListName(name);

            //update scoreboard
            for(Player target : Bukkit.getOnlinePlayers()){
                target.setScoreboard(target.getScoreboard());
            }

            if(!sendPackets) {
                return;
            }

            //send entity packets
            Object nms = EntityUtilities.getHandle(player);

            Object destroyPacket = PacketType.Server.ENTITY_DESTROY.getPacketClass().getConstructors()[1].newInstance(new int[] {player.getEntityId()});
            Object spawnPacket = PacketType.Server.NAMED_ENTITY_SPAWN.getPacketClass().getConstructor(ENTITY_HUMAN_CLASS).newInstance(nms);

            Player[] players = new Player[Bukkit.getOnlinePlayers().length - 1];
            int i = 0;
            for (Player target: Bukkit.getOnlinePlayers()){
                if(player.equals(target)){
                    continue;
                }
                players[i] = target;
                i++;
            }

            JLib.getProtocolManager().sendPacket(new Object[]{destroyPacket, spawnPacket}, players);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
