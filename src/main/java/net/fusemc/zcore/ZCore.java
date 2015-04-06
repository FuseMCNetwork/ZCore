package net.fusemc.zcore;

import com.xxmicloxx.znetworkplugin.ZNetworkPlugin;
import me.michidk.DKLib.command.CommandManager;
import net.fusemc.zcore.barAPI.BarAPI;
import net.fusemc.zcore.featureSystem.FeatureManager;
import net.fusemc.zcore.friendSystem.FriendManager;
import net.fusemc.zcore.holoAPI.HoloManager;
import net.fusemc.zcore.languageAPI.LanguageManager;
import net.fusemc.zcore.languageAPI.LanguageSettingsManager;
import net.fusemc.zcore.menuAPI.MenuListener;
import net.fusemc.zcore.mysql.MySQL;
import net.fusemc.zcore.mysql.MySQLDBType;
import net.fusemc.zcore.projectileAPI.ProjectileRegistrator;
import net.fusemc.zcore.rankSystem.RankListener;
import net.fusemc.zcore.shopAPI.ShopManager;
import net.fusemc.zcore.statsAPI.StatsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;

public class ZCore extends JavaPlugin implements Listener
{

    private static ZCore instance;

    private CommandManager commandManager;
    private FeatureManager featureManager;
    private LanguageManager languageManager;
    private LanguageSettingsManager languageSettingsManager;
    private FriendManager friendManager;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private MySQL networkMySQL;
    private MySQL playerMySQL;
    private StatsManager statsManager;
    private ShopManager shopManager;
    private BarAPI barAPI;

    public static String serverName = "unknown_server";

    //IF you using WINDOWS you will be in OFFLINE Mode
    //IF that plugin is on the LINUX server it will be ONLINE Mode
    public static boolean OFFLINE = false;
    private static boolean useNocheatplus;

    @Override
    public void onEnable(){
        instance = this;

        //SET OFFLINE:
        OFFLINE = System.getProperty("os.name").startsWith("Windows") || System.getProperty("debug") != null;

        //for buildserver
        //OFFLINE = true;
        if(!OFFLINE) {
            networkMySQL = new MySQL(MySQLDBType.NETWORK);
            playerMySQL = new MySQL(MySQLDBType.PLAYER);
        }

        if (Bukkit.getPluginManager().getPlugin("ZBukkitPlugin") == null) {
            serverName = "unknown_server";
        } else {
            serverName = ZNetworkPlugin.getInstance().getConnectionName();
        }

        shopManager = new ShopManager();
        statsManager = new StatsManager();
        barAPI = new BarAPI();

        commandManager = new CommandManager(this);
        featureManager = new FeatureManager();
        languageManager = new LanguageManager();
        languageSettingsManager = new LanguageSettingsManager();
        friendManager = new FriendManager();

        register();
        languageManager.load();

        ProjectileRegistrator.registeProjectiles();

        if(Bukkit.getServer().getPluginManager().getPlugin("NoCheatPlus") != null)
        {
            useNocheatplus = true;
        }
    }

    @Override
    public void onDisable(){
        for (Player p:Bukkit.getOnlinePlayers()) {
            p.kickPlayer("lobby");
        }

        featureManager.serverStop();
    }

    private void register(){
        Listener[] listeners = {
                this,
                new HoloManager(),
                new RankListener(),
                new MenuListener(),
                languageSettingsManager,
                languageManager
        };
        PluginManager pm = Bukkit.getPluginManager();
        for(Listener listener: listeners)
            pm.registerEvents(listener, this);

    }

    //send offline warning - delayed because welcome message
    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent e){
        if (OFFLINE){
           e.getPlayer().sendMessage(ChatColor.DARK_RED + "Warning: this server is in offline-mode");
        }
        else if (e.getPlayer().isOp()){
           e.getPlayer().sendMessage(ChatColor.DARK_RED + "Warning: you are OP");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        PlayerLeaveEvent ple = new PlayerLeaveEvent(e.getPlayer(), e.getQuitMessage(), null);
        Bukkit.getPluginManager().callEvent(ple);

        e.setQuitMessage(ple.getLeaveMessage());
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        PlayerLeaveEvent ple = new PlayerLeaveEvent(e.getPlayer(), e.getLeaveMessage(), e.getReason());
        Bukkit.getPluginManager().callEvent(ple);

        e.setLeaveMessage(ple.getLeaveMessage());
        e.setReason(ple.getReason());
    }

    public static ZCore getInstance()
    {
        return instance;
    }

    public static CommandManager getCommandManager(){
        return instance.commandManager;
    }

    public static FeatureManager getFeatureManager(){
        return instance.featureManager;
    }
    
    public static LanguageManager getLanguageManager(){
    	return instance.languageManager;
    }

    public static LanguageSettingsManager getLanguageSettingsManager() {
        return instance.languageSettingsManager;
    }

    public static FriendManager getFriendManager(){
        return instance.friendManager;
    }

    public static Gson getGson() {
        return instance.gson;
    }

    public static boolean isUseNocheatplus() {
        return useNocheatplus;
    }

    public static Connection getNetworkConnection() throws SQLException{
        return instance.networkMySQL.getConnection();
    }

    public static Connection getPlayerConnection() throws SQLException {
        return instance.playerMySQL.getConnection();
    }

    public static StatsManager getStatsManager() {
        return instance.statsManager;
    }

    public static ShopManager getShopManager() {
        return instance.shopManager;
    }

    public static BarAPI getBarAPI() {
        return instance.barAPI;
    }
}
