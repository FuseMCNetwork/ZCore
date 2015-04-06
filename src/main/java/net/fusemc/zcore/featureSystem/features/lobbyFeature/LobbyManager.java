package net.fusemc.zcore.featureSystem.features.lobbyFeature;

import me.michidk.DKLib.FileHelper;
import me.michidk.DKLib.utils.ChatUtils;
import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.featureSystem.features.CatchGameFeature;
import net.fusemc.zcore.featureSystem.features.lobbyFeature.guide.GuideManager;
import net.fusemc.zcore.featureSystem.features.trailFeature.TrailFeature;
import net.fusemc.zcore.util.NamedItem;
import net.fusemc.zcore.voteAPI.Vote;
import net.fusemc.zcore.voteAPI.VoteItem;
import org.bukkit.*;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LobbyManager implements Listener
{

    private LobbySettings settings;
    private LobbyListener listener = new LobbyListener();

    private Vote vote;
    private HashMap<String, String[]> builder = new HashMap<String, String[]>();
    public static GuideManager guideManager;

    public LobbyManager(LobbySettings settings)
    {
        this.settings = settings;

        //start scheduler
        new CountdownScheduler(this);

        //WORLD SETTINGS:
        final World w = settings.getWorld();
        w.setPVP(true);
        w.setStorm(false);
        w.setThundering(false);
        w.setWeatherDuration(0);
        w.setTime(9000);
        w.setDifficulty(Difficulty.EASY);
        w.setGameRuleValue("doDaylightCycle", "false");
        w.setGameRuleValue("doFireTick", "false");
        w.setGameRuleValue("doMobSpawning", "false");
        w.setGameRuleValue("keepInventory", "true");
        w.setGameRuleValue("mobGriefing", "false");
        w.setGameRuleValue("naturalRegeneration", "false");

        //BUKKIT SETTINGS
        Bukkit.setDefaultGameMode(GameMode.ADVENTURE);
        Bukkit.setSpawnRadius(0);

        ZCore.getInstance().getCommand("start").setExecutor(new StartCommand());

        //enable chatch featureSystem!
        ZCore.getFeatureManager().getFeature(CatchGameFeature.class).enable();
        //ZCore.getFeatureManager().getFeature(DisguiseFeature.class).enable();
        //ZCore.getFeatureManager().getFeature(DisguisePickerFeature.class).enable();
        ZCore.getFeatureManager().getFeature(TrailFeature.class).enable();


        Bukkit.getScheduler().scheduleSyncRepeatingTask(ZCore.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                if (w == null) return;

                w.setStorm(false);
                w.setThundering(false);
                w.setTime(9000);

            }
        }, 0L, 20L);


        if (settings.isEnableVote())
        {
            //init vote system
            //loop through the worlds and get vote.json
            List<VoteItem> list = new ArrayList<>();
            for (File f : Bukkit.getWorldContainer().listFiles())
            {

                if (f.isDirectory())
                {
                    File json = new File(f.getAbsolutePath(), "mapinfo.json");
                    if (json.exists())
                    {

                        MapInfo info = (MapInfo) ZCore.getGson().fromJson(FileHelper.stringFromFile(json), MapInfo.class);
                        info.setWorldName(f.getName());
                        info.init();


                        VoteItem item = (VoteItem) new VoteItem(info.getMaterial(), info.getDisplayName());
                        item.setLore(1, "  \u00A7r\u00A78\u00A7oErbauer: \u00A77\u00A7o" + info.getAuthor());
                        list.add(item);
                    }
                }
            }

            String title = "Map Vote";
            vote = new Vote(title, list, true, true);

            guideManager = new GuideManager();
        }

        //register listener
        listener.enable(settings, vote);
    }

    public void disable()
    {
        listener.disable();
    }

    public LobbySettings getLobbySettings()
    {
        return settings;
    }

    public Vote getVote()
    {
        return vote;
    }


}
