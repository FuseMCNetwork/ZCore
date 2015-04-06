package net.fusemc.zcore.util;

import me.johnking.jlib.JLib;
import me.johnking.jlib.protocol.ProtocolManager;
import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.spigotmc.ProtocolInjector;

/**
 * Created by Niklas on 05.10.2014.
 */
public class MC1_8Utils {

    public static void displayActionbarMessage(Player player, String message) {
        if (((CraftPlayer)player).getHandle().playerConnection.networkManager.getVersion() < 47) return;

        IChatBaseComponent serializedMessage = ChatSerializer.a(String.format("{'text':'%s'}", message));

        JLib.getProtocolManager().sendPacket(new PacketPlayOutChat(serializedMessage, 2), player);
    }

    public static void setTabTitle(Player player, String header, String footer) {
        if (((CraftPlayer)player).getHandle().playerConnection.networkManager.getVersion() < 47) return;

        IChatBaseComponent serializedHeader = ChatSerializer.a(String.format("{'text':'%s'}", header));
        IChatBaseComponent serializedFooter = ChatSerializer.a(String.format("{'text':'%s'}", footer));

        JLib.getProtocolManager().sendPacket(new ProtocolInjector.PacketTabHeader(serializedHeader, serializedFooter), player);
    }

    public static void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subTitle) {
        if (((CraftPlayer)player).getHandle().playerConnection.networkManager.getVersion() < 47) return;

        IChatBaseComponent serializedTitle = ChatSerializer.a(String.format("{'text':'%s'}", title));
        IChatBaseComponent serializedSubTitle = ChatSerializer.a(String.format("{'text':'%s'}", subTitle));

        ProtocolManager pm = JLib.getProtocolManager();
        pm.sendPacket(new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.TIMES, fadeIn, stay, fadeOut), player);
        pm.sendPacket(new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.TITLE, serializedTitle), player);
        pm.sendPacket(new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.SUBTITLE, serializedSubTitle), player);
    }

    public static boolean is1_8(Player player) {
        return (((CraftPlayer)player).getHandle().playerConnection.networkManager.getVersion() >= 47);
    }
}
