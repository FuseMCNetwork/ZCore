package net.fusemc.zcore.shopAPI.packets;

import com.xxmicloxx.znetworklib.PacketRegistry;
import com.xxmicloxx.znetworklib.codec.NetworkEvent;
import com.xxmicloxx.znetworkplugin.EventListener;
import com.xxmicloxx.znetworkplugin.ZNetworkPlugin;
import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.shopAPI.PlayerShopDataUpdateEvent;
import net.fusemc.zcore.shopAPI.ShopManager;
import net.fusemc.zcore.util.MessagePrefix;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Copyright by michidk
 * Created: 22.08.2014.
 */
public class UpdateListener {

    public static void registerEvents() {

        PacketRegistry.registerPacket(UpdateCoinsEvent.class, 665678);
        PacketRegistry.registerPacket(UpdatePackagesEvent.class, 665670);

        ZNetworkPlugin.getInstance().registerEvent(UpdateCoinsEvent.eventName, new EventListener() {
            @Override
            public void onEventReceived(String event, String sender, NetworkEvent data) {
                if (!(data instanceof UpdateCoinsEvent)) {
                    return;
                }
                UpdateCoinsEvent updateCoinsEvent = (UpdateCoinsEvent) data;
                Player p = ShopManager.getPlayerByUuid(updateCoinsEvent.getUuid());
                if (p == null) return;

                ZCore.getShopManager().updateCoinsFromDatabase(p);
                PlayerShopDataUpdateEvent updateEvent = new PlayerShopDataUpdateEvent(p);
                Bukkit.getPluginManager().callEvent(updateEvent);
                p.sendMessage(MessagePrefix.SHOP_PREFIX + "Du hast \u00A73" + updateCoinsEvent.getCoins() + " M\u00FCnzen \u00A7aerhalten!");
            }
        });

        ZNetworkPlugin.getInstance().registerEvent(UpdatePackagesEvent.eventName, new EventListener() {
            @Override
            public void onEventReceived(String event, String sender, NetworkEvent data) {
                if (!(data instanceof UpdatePackagesEvent)) {
                    return;
                }
                UpdatePackagesEvent updatePackagesEvent = (UpdatePackagesEvent) data;
                Player p = ShopManager.getPlayerByUuid(updatePackagesEvent.getUuid());
                if (p == null) return;

                ZCore.getShopManager().updatePackagesFromDatabase(p);
                PlayerShopDataUpdateEvent updateEvent = new PlayerShopDataUpdateEvent(p);
                Bukkit.getPluginManager().callEvent(updateEvent);
                if (updatePackagesEvent.getAction().equalsIgnoreCase("ADD")) {
                    p.sendMessage(MessagePrefix.SHOP_PREFIX + "Du hast \u00A73" + (updatePackagesEvent.getPackageName().equals("VIP") ? "Premium" : updatePackagesEvent.getPackageName()) + " \u00A7aerhalten!");
                } else if (updatePackagesEvent.getAction().equalsIgnoreCase("REMOVE")) {
                    p.sendMessage(MessagePrefix.SHOP_PREFIX + "Dein \u00A73" + (updatePackagesEvent.getPackageName().equals("VIP") ? "Premium" : updatePackagesEvent.getPackageName()) + " \u00A7aist abgelaufen!");
                }
            }
        });
    }

}
