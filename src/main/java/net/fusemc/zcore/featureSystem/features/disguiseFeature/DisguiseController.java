package net.fusemc.zcore.featureSystem.features.disguiseFeature;

import me.johnking.jlib.protocol.PacketType;
import me.johnking.jlib.protocol.event.PacketEvent;
import me.johnking.jlib.protocol.event.PacketListener;
import me.johnking.jlib.protocol.event.ProtocolPacket;
import me.johnking.jlib.protocol.wrappers.WrappedGameProfile;
import net.fusemc.zcore.ZCore;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Marco on 20.08.2014.
 */
public class DisguiseController implements PacketListener {

    private final HashMap<UUID, String> disguises = new HashMap<>();
    private final DisguiseHelper disguiseHelper = new DisguiseHelper();

    public void handleLogin(Player player) {
        //check if someone is disguised and has the joining player-name
        if(currentPlayerDisguised(player.getName())){
            for(Map.Entry<UUID, String> entry: disguises.entrySet()) {
                if(entry.getValue().equalsIgnoreCase(player.getName())) {
                    Player disguisedPlayer = Bukkit.getPlayer(entry.getKey());
                    if(disguisedPlayer == null) {
                        continue;
                    }
                    //un disguise player if joining - player - name == disguise - name
                    disguises.remove(disguisedPlayer.getUniqueId());
                    disguiseHelper.changeName(disguisedPlayer, entry.getValue(), true);
                    disguisedPlayer.sendMessage("\u00A78\u00A7l[\u00A79\u00A7li\u00A78\u00A7l] \u00A7aDer Verwandler wurde deaktiviert, weil der echte Spieler (\u00A73" + player.getName() + "\u00A7a) mit deinem Verwandlungsnamen beigetreten ist.");
                }
            }
        }
        //check if player has a disguise
        String disguise = hasDisguise(player);
        if(disguise == null) {
            return;
        }
        //check if a player is online with a disguise
        boolean exists = false;
        for(Player current : Bukkit.getOnlinePlayers()) {
            if(current.getName().equalsIgnoreCase(disguise)) {
                exists = true;
                break;
            }
        }
        if(exists) {
            player.sendMessage("\u00A78\u00A7l[\u00A79\u00A7li\u00A78\u00A7l] \u00A7aDer Verwandler wurde deaktiviert, weil der echte Spieler (\u00A73" + player.getName() + "\u00A7a) mit deinem Verwandlungsnamen schon online ist.");
            return;
        }
        //disguise player
        disguises.put(player.getUniqueId(), player.getName());
        disguiseHelper.changeName(player, disguise, false);
        player.sendMessage("\u00a78\u00a7l[\u00a79\u00a7li\u00a78\u00a7l] \u00a7aDer Verwandler ist aktiv und du hast dich in \u00a73" + player.getName() + " \u00a7averwandelt!");
    }

    public void handleQuit(Player player) {
        if(disguises.containsKey(player.getUniqueId())){
            disguises.remove(player.getUniqueId());
        }
    }

    private String hasDisguise(Player player) {
        if (ZCore.OFFLINE) {
            return null;
        }
        try (Connection connection = ZCore.getPlayerConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT name FROM disguise_players WHERE uuid = ?");
            ps.setString(1, player.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            String result = null;
            if(rs.next()) {
                result = rs.getString(1);
            }
            rs.close();
            ps.close();
            return result;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    private boolean currentPlayerDisguised(String name){
        for(String player : disguises.values()){
            if(player.equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    public boolean isDisguised(Player player) {
        return disguises.containsKey(player.getUniqueId());
    }

    public void unDisguiseAll() {
        for(Map.Entry<UUID, String> entry: disguises.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            if(player == null) {
                continue;
            }
            disguiseHelper.changeName(player, entry.getValue(), true);
        }
        disguises.clear();
    }

    @Override
    public void onPacket(PacketEvent packetEvent) {
        ProtocolPacket packet = packetEvent.getPacket();
        if(packet.getPacketType() != PacketType.Server.NAMED_ENTITY_SPAWN) {
            return;
        }
        GameProfile current = packet.getObject(GameProfile.class, 0);
        if(!disguises.keySet().contains(current.getId())) {
            return;
        }
        WrappedGameProfile profile = new WrappedGameProfile(UUID.randomUUID(), current.getName());
        packet.setObject(GameProfile.class, 0, profile.getHandle());
    }
}
