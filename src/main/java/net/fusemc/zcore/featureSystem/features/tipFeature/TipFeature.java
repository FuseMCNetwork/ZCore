package net.fusemc.zcore.featureSystem.features.tipFeature;

import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.featureSystem.Feature;
import net.fusemc.zcore.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TipFeature extends Feature{

    private int intervall;
    private TipType type;
    private List<String> tips;
    private boolean showPrefix;
    private int schedulerId;
    private TipScheduler tipScheduler;

    public TipFeature(){
        Config c = new Config("tips", ZCore.getInstance());
        setDefaults(c);
    }

    private void setDefaults(Config c){
        c.getConfig().options().copyDefaults(true);
        List<String> list = Arrays.asList(new String[]{"Default Tip!", "Another Tip"});
        c.getConfig().addDefault("tips", list);
        c.getConfig().addDefault("type", TipType.ENDERBAR.name());
        c.getConfig().addDefault("intervall", 10);
        c.getConfig().addDefault("showPrefix", false);
        c.save();
    }

    @Override
    public boolean enable() {
        Config c = new Config("tips", ZCore.getInstance());
        return enable(intervall = c.getConfig().getInt("intervall"), TipType.getType(c.getConfig().getString("type")), c.getConfig().getStringList("tips"), c.getConfig().getBoolean("showPrefix"));
    }

    public boolean enable(int intervall, List<String> tips) {
         return this.enable(intervall, TipType.ENDERBAR, tips, true);
    }

    public boolean enable(int intervall, TipType type, List<String> tips, boolean showPrefix){
        if(super.enable()){
            this.intervall = intervall;
            this.type = type;
            this.tips = tips;
            this.showPrefix = showPrefix;
            this.tipScheduler = new TipScheduler(type, replaceColorCodes(this.tips), showPrefix);
            this.schedulerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(ZCore.getInstance(), tipScheduler, 20L * 10, this.intervall * 20);
            return true;
        }
        return false;
    }

    @Override
    public boolean disable() {
        if(super.disable()){
            Bukkit.getScheduler().cancelTask(schedulerId);
            if (type == TipType.ENDERBAR) {
                ZCore.getBarAPI().removeBar();
            }
            return true;
        }
        return false;
    }

    /* Is now automatic
    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        if (type == TipType.ENDERBAR) {
            BarAPI.setMessage(e.getPlayer(), tipScheduler.getCurrentTip());
        }
    }
    */

    public List<String> replaceColorCodes(List<String> list)
    {
        List<String> newList = new ArrayList<>();
        for (String s:list)
        {
            newList.add(ChatColor.translateAlternateColorCodes('\u0026', s));
        }
        return newList;
    }
}
