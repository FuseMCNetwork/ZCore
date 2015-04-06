package net.fusemc.zcore.shopAPI;

import net.fusemc.zcore.ZCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Niklas on 20.08.2014.
 */
public class PlayerShopData {

    private Player player;
    private int coins;
    private int points;
    private Set<PackageType> packages;
    private Set<String> ingameItems;
    private Set<String> selectedItems;

    PlayerShopData(Player player) {
        this.player = player;
        this.coins = 0;
        this.points = 0;
        this.packages = new HashSet<>();
        this.ingameItems = new HashSet<>();
        this.selectedItems = new HashSet<>();
        updateFromDatabase();
    }

    public boolean modifyCoins(final int amount, final boolean add, final Runnable future) {
        if (!add && coins - amount < 0) {
            if (future != null) future.run();
            return false;
        }
        if (ZCore.OFFLINE) {
            if (add) coins = coins + amount;
            else coins = coins - amount;
            if (future != null) future.run();
            return true;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                try (Connection connection = ZCore.getPlayerConnection()) {
                    PreparedStatement ps = connection.prepareStatement("INSERT INTO coins(`uuid`, `coins`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `coins`=`coins`" + (add ? "+" : "-") + "?");
                    ps.setString(1, player.getUniqueId().toString());
                    ps.setInt(2, amount);
                    ps.setInt(3, amount);
                    ps.execute();
                    if (add) coins = coins + amount;
                    else coins = coins - amount;
                    final PlayerShopDataUpdateEvent updateEvent = new PlayerShopDataUpdateEvent(player);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.getPluginManager().callEvent(updateEvent);
                        }
                    }.runTask(ZCore.getInstance());
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (future != null) future.run();
            }
        }.runTaskAsynchronously(ZCore.getInstance());
        return true;
    }

    public boolean modifyPoints(final int amount, final boolean add, final Runnable future) {
        if (!add && points - amount < 0) {
            if (future != null) future.run();
            return false;
        }
        if (ZCore.OFFLINE) {
            if (add) points = points + amount;
            else points = points - amount;
            if (future != null) future.run();
            return true;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                try (Connection connection = ZCore.getPlayerConnection()) {
                    PreparedStatement ps = connection.prepareStatement("INSERT INTO coins(`uuid`, `points`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `points`=`points`" + (add ? "+" : "-") + "?");
                    ps.setString(1, player.getUniqueId().toString());
                    ps.setInt(2, amount);
                    ps.setInt(3, amount);
                    ps.execute();
                    if (add) points = points + amount;
                    else points = points - amount;
                    final PlayerShopDataUpdateEvent updateEvent = new PlayerShopDataUpdateEvent(player);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.getPluginManager().callEvent(updateEvent);
                        }
                    }.runTask(ZCore.getInstance());
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (future != null) future.run();
            }
        }.runTaskAsynchronously(ZCore.getInstance());
        return true;
    }

    public void addShopItem(String name, final Runnable future) {
        final String lowerName = name.toLowerCase();
        if (ZCore.OFFLINE) {
            ingameItems.add(lowerName);
            if (future != null) future.run();
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                try (Connection connection = ZCore.getPlayerConnection()) {
                    PreparedStatement ps = connection.prepareStatement("INSERT INTO ingameitem(`uuid`, `item`) VALUES (?, ?)");
                    ps.setString(1, player.getUniqueId().toString());
                    ps.setString(2, lowerName);
                    ps.execute();
                    ingameItems.add(lowerName);
                    final PlayerShopDataUpdateEvent updateEvent = new PlayerShopDataUpdateEvent(player);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.getPluginManager().callEvent(updateEvent);
                        }
                    }.runTask(ZCore.getInstance());
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (future != null) future.run();
            }
        }.runTaskAsynchronously(ZCore.getInstance());
    }

    public void selectItem(String name, final Runnable future) {
        final String lowerName = name.toLowerCase();
        if (ZCore.OFFLINE) {
            selectedItems.add(lowerName);
            if (future != null) future.run();
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                try (Connection connection = ZCore.getPlayerConnection()) {
                    PreparedStatement ps = connection.prepareStatement("INSERT INTO selecteditem(`uuid`, `item`) VALUES (?, ?)");
                    ps.setString(1, player.getUniqueId().toString());
                    ps.setString(2, lowerName);
                    ps.execute();
                    selectedItems.add(lowerName);
                    final PlayerShopDataUpdateEvent updateEvent = new PlayerShopDataUpdateEvent(player);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.getPluginManager().callEvent(updateEvent);
                        }
                    }.runTask(ZCore.getInstance());
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (future != null) future.run();
            }
        }.runTaskAsynchronously(ZCore.getInstance());
    }

    public void deselectItem(final String[] names, final Runnable future) {
        if (ZCore.OFFLINE) {
            for (String name : names) {
                selectedItems.remove(name.toLowerCase());
            }
            if (future != null) future.run();
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                try (Connection connection = ZCore.getPlayerConnection()) {
                    PreparedStatement ps = connection.prepareStatement("DELETE FROM selecteditem WHERE `uuid`=? AND `item`=?");
                    for (String name : names) {
                        name = name.toLowerCase();
                        ps.setString(1, player.getUniqueId().toString());
                        ps.setString(2, name);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                    for (String name : names) {
                        selectedItems.remove(name);
                    }
                    final PlayerShopDataUpdateEvent updateEvent = new PlayerShopDataUpdateEvent(player);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.getPluginManager().callEvent(updateEvent);
                        }
                    }.runTask(ZCore.getInstance());
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (future != null) future.run();
            }
        }.runTaskAsynchronously(ZCore.getInstance());
    }

    public int getCoins() {
        return coins;
    }

    public int getPoints() {
        return points;
    }

    public boolean hasPackage(PackageType packageType) {
        return packages.contains(packageType);
    }

    public boolean hasIngameItem(String name) {
        return ingameItems.contains(name.toLowerCase());
    }

    public boolean hasItemSelected(String name) {
        return selectedItems.contains(name.toLowerCase());
    }

    public String[] getSelectedItems() {
        return selectedItems.toArray(new String[selectedItems.size()]);
    }

    boolean updateFromDatabase() {
        return updateCoins() &&
        updatePackages() &&
        updatePoints() &&
        updateIngameItems() &&
        updateSelectedItems();
    }

    public boolean updateCoins() {
        if (ZCore.OFFLINE) return true;
        try (Connection connection = ZCore.getPlayerConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT coins FROM coins WHERE `uuid`=?");
            ps.setString(1, player.getUniqueId().toString());
            ResultSet res = ps.executeQuery();
            if (res.next()) {
                coins = res.getInt("coins");
            }
            res.close();
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePoints() {
        if (ZCore.OFFLINE) return true;
        try (Connection connection = ZCore.getPlayerConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT points FROM coins WHERE `uuid`=?");
            ps.setString(1, player.getUniqueId().toString());
            ResultSet res = ps.executeQuery();
            if (res.next()) {
                points = res.getInt("points");
            }
            res.close();
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePackages() {
        if (ZCore.OFFLINE) return true;
        try (Connection connection = ZCore.getPlayerConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT package FROM shopitem WHERE `uuid`=?");
            ps.setString(1, player.getUniqueId().toString());
            ResultSet res = ps.executeQuery();
            packages.clear();
            while (res.next()) {
                packages.add(PackageType.fromName(res.getString("package")));
            }
            res.close();
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateIngameItems() {
        if (ZCore.OFFLINE) return true;
        try (Connection connection = ZCore.getPlayerConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT item FROM ingameitem WHERE `uuid`=?");
            ps.setString(1, player.getUniqueId().toString());
            ResultSet res = ps.executeQuery();
            ingameItems.clear();
            while (res.next()) {
                ingameItems.add(res.getString("item").toLowerCase());
            }
            res.close();
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSelectedItems() {
        if (ZCore.OFFLINE) return true;
        try (Connection connection = ZCore.getPlayerConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT item FROM selecteditem WHERE `uuid`=?");
            ps.setString(1, player.getUniqueId().toString());
            ResultSet res = ps.executeQuery();
            selectedItems.clear();
            while (res.next()) {
                selectedItems.add(res.getString("item").toLowerCase());
            }
            res.close();
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}