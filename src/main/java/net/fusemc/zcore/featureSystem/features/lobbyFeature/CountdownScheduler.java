package net.fusemc.zcore.featureSystem.features.lobbyFeature;

import me.michidk.DKLib.utils.ChatUtils;
import me.michidk.DKLib.utils.MathHelper;
import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.featureSystem.features.CatchGameFeature;
import net.fusemc.zcore.featureSystem.features.tipFeature.TipFeature;
import net.fusemc.zcore.featureSystem.features.trailFeature.TrailFeature;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class CountdownScheduler implements Runnable
{

    private static CountdownScheduler instance;
    private LobbyManager manager;

    private int schedulerID = 0;
    private int time;

    public boolean started = false;

    public CountdownScheduler(LobbyManager manager)
    {
        if (instance == null) instance = this;
        else return;

        this.manager = manager;
        time = manager.getLobbySettings().getTimeToStart();

        //start scheduler
        schedulerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(ZCore.getInstance(), this, 0, 20);

        System.out.println("[Lobby] waiting for players....");
    }

    boolean breaked = true;
    @Override
    public void run()
    {

        //wait for players to start
        if (Bukkit.getOnlinePlayers().length < manager.getLobbySettings().getMinPlayers())
        {
            double playerPercentage = MathHelper.getPercentage(Bukkit.getOnlinePlayers().length, manager.getLobbySettings().getMinPlayers());
            ZCore.getBarAPI().setMessage("\u00A79Warte auf Spieler \u00A73[" + Bukkit.getOnlinePlayers().length + "/" + manager.getLobbySettings().getMinPlayers() + "]", (float) playerPercentage * 100F);
            time = manager.getLobbySettings().getTimeToStart();

            if (breaked == false){
                System.out.println("[Lobby] a player left, waiting for more players...");
                Bukkit.getPluginManager().callEvent(new CountdownBreakEvent());
                breaked = true;
            }
        }
        //count
        else
        {
            double timePercentage = MathHelper.getPercentage(time, manager.getLobbySettings().getTimeToStart());
            ZCore.getBarAPI().setMessage("\u00A7b\u22D9 \u00A79Startet in \u00A72" + time + "s \u00A7b\u22D8");

            time--;
            breaked = false;
        }

        //call the event
        CountdownTickEvent countdownTickEvent = new CountdownTickEvent(time);
        Bukkit.getPluginManager().callEvent(countdownTickEvent);


        //check for end
        if (time <= 0)
        {
            System.out.println("[Lobby] started the game");
            startGame();
        }

    }

    public void startGame()
    {
        if (started == true) return;
        started = true;

        for(Player p:Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 0, 0);
        this.stop();

        //das solln die jeweiligen spielmodie selbst handeln
        //Z_Core.activateFeature(new TipFeature());
        //Z_Core.activateFeature(new BloodFeature());

        //activate shopitem featureSystem & deaktivate after lobby

        ZCore.getBarAPI().removeBar();
        //reset player
        for (Player p : Bukkit.getOnlinePlayers())
        {
            ((CraftPlayer) p).getHandle().reset();
            p.setGameMode(GameMode.SURVIVAL);
            p.setExp(0);
            p.setLevel(0);
            p.closeInventory();
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);
            p.getActivePotionEffects().removeAll(p.getActivePotionEffects());
            p.setMaxHealth(20D);
            p.setHealth(20D);
            p.setFoodLevel(20);
            p.setWalkSpeed(0.2f);
            p.setAllowFlight(false);
            p.setFlying(false);
        }


        if (!manager.getLobbySettings().isEnableVote())
        {
            GameStartEvent event = new GameStartEvent();
            Bukkit.getPluginManager().callEvent(event);
            return;
        }

        //get map info
        MapInfo info = MapInfo.getByDisplayName(manager.getVote().getResult().getName());

        //Bukkit.broadcastMessage(info.getAuthor() + " - " + info.getAuthorLink() + " - " + info.getDisplayName() + " - " + info.getMaterial() + " - " + info.getWorldName());

        //prepare message
        String[] messageArray = {
                "\u00A72\u00A7l═════════════════════════",
                "\u00A7aEs wird auf der Map \u00A76\u00A7l" + info.getDisplayName() + " \u00A7r\u00A7agespielt!",
                "\u00A73Erbaut von \u00A7b" + info.getAuthor(),
                "\u00A77(" + info.getAuthorLink() + "\u00A77)",
        };

        //array to list
        List<String> messageList = new ArrayList<String>();
        for (String message : messageArray)
            messageList.add(message);


        PreGameStartEvent preEvent = new PreGameStartEvent(messageList);
        Bukkit.getPluginManager().callEvent(preEvent);

        preEvent.addMessage("\u00A72\u00A7l═════════════════════════");

        if (preEvent.isBroadcastMessages())
        {
            messageArray = preEvent.getMessages().toArray(new String[preEvent.getMessages().size()]);


            for (Player p : Bukkit.getOnlinePlayers())
            {
                ChatUtils.broadcastEmptyLines(p, 20);
                ChatUtils.broadcastCentered(p, messageArray);
            }
        }


        //load world
        World world = Bukkit.createWorld(WorldCreator.name(info.getWorldName()));
        world.setAutoSave(false);

        CatchGameFeature minigame = ZCore.getFeatureManager().getFeature(CatchGameFeature.class);
        minigame.disable();
        Player minigameWinner = minigame.getWinner();
        //ZCore.getFeatureManager().getFeature(DisguisePickerFeature.class).disable();
        ZCore.getFeatureManager().getFeature(TrailFeature.class).disable();

        //little jump fix
        for (Player p:Bukkit.getOnlinePlayers()) p.setVelocity(new Vector(0,0,0));

        /**
         CALL FINAL EVENT
         */
        GameStartEvent event = new GameStartEvent(world, info, minigameWinner);
        Bukkit.getPluginManager().callEvent(event);

        ZCore.getFeatureManager().getFeature(TipFeature.class).enable();

    }

    public void stop()
    {
        Bukkit.getScheduler().cancelTask(schedulerID);
        manager.disable();
    }

    public static CountdownScheduler getInstance() {
        return instance;
    }
}
