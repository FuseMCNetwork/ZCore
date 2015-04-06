package net.fusemc.zcore.bungeeBridge;

import com.xxmicloxx.znetworklib.PacketRegistry;
import com.xxmicloxx.znetworkplugin.ZNetworkPlugin;
import net.fusemc.zcore.ZCore;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Copyright by michidk
 * Created: 28.09.2014.
 */
public class BungeeBridge {

    static {
        PacketRegistry.registerPacket(KickPlayerEvent.class, 665341);
        PacketRegistry.registerPacket(BanPlayerEvent.class, 6977878);
    }

    /**
     * kicks a player from the network
     * @param player    the player
     * @param reason    the reason
     * @param sender    the sender of the kick
     */
    public static void kick(Player player, String reason, String sender) {
        if (ZCore.OFFLINE) return;

        ZNetworkPlugin.getInstance().sendEvent(KickPlayerEvent.EVENT_NAME, new KickPlayerEvent(player.getName(), reason, sender));
    }

    /**
     * bans a player for a certain time
     * @param player    the player
     * @param reason    the reason
     * @param millis    the time the player is unbanned in millis
     * @param sender    the sender of the ban
     */
    public static void ban(Player player, String reason, long millis, String sender) {
        if (ZCore.OFFLINE) return;

        ZNetworkPlugin.getInstance().sendEvent(BanPlayerEvent.EVENT_NAME, new BanPlayerEvent(player.getName(), reason, millis, sender));

        try (Connection connection = ZCore.getPlayerConnection()) {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO bans (uuid, reason, ban_end, sender) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE reason = VALUES(reason), ban_end = VALUES(ban_end), sender = VALUES(sender);");
            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, reason);
            ps.setTimestamp(3, new Timestamp(millis));
            ps.setString(4, sender);
            ps.execute();
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }

}
