package net.fusemc.zcore.shopAPI;

import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.shopAPI.packets.UpdateListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Niklas on 20.08.2014.
 */
public class ShopManager {

    private Map<Player, PlayerShopData> playerCache;

    public ShopManager() {
        playerCache = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(new LeaveListener(), ZCore.getInstance());
        if (!ZCore.OFFLINE)UpdateListener.registerEvents();
    }

    public PlayerShopData getPlayerData(Player player) {
        if (playerCache.get(player) == null) {
            playerCache.put(player, new PlayerShopData(player));
        }
        return playerCache.get(player);
    }

    public PlayerShopData updateFromDatabase(Player player) {
        if (playerCache.get(player) != null) {
            playerCache.get(player).updateFromDatabase();
        }
        return getPlayerData(player);
    }

    public PlayerShopData updateCoinsFromDatabase(Player player) {
        if (playerCache.get(player) != null) {
            playerCache.get(player).updateCoins();
        }
        return getPlayerData(player);
    }

    public PlayerShopData updatePackagesFromDatabase(Player player) {
        if (playerCache.get(player) != null) {
            playerCache.get(player).updatePackages();
        }
        return getPlayerData(player);
    }

    public static Player getPlayerByUuid(String uuid) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getUniqueId().toString().equals(uuid)) {
                return p;
            }
        }
        return null;
    }

    void removeFromCache(Player player) {
        playerCache.remove(player);
    }
}
