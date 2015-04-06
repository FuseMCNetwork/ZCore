package net.fusemc.zcore.featureSystem.features.messageFeature;

import me.michidk.DKLib.utils.ChatUtils;
import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.featureSystem.Feature;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author michidk
 */

public class WelcomeFeature extends Feature
{

    private String gamemode = "";
    private String[] commands;

    private static String[] joinMessage = new String[]{};


    public boolean enable(String gamemode)
    {
        if (super.enable())
        {
            this.gamemode = gamemode;

            joinMessage = makeJoinMessage(gamemode, commands);

            return true;
        }
        return false;

    }

    public boolean enable(String gamemode, String[] commands)
    {
        this.commands = commands;
        return enable(gamemode);
    }

    public static String[] makeJoinMessage(String name, String[] commands)
    {
        StringBuilder commandList = new StringBuilder();

        commandList.append("\u00A78");
        commandList.append("/lobby,");

        if (commands != null)
        {
            for (String s : commands)
            {
                commandList.append(s);
                commandList.append(",");
            }
        }

        //remove last ','
        if (commandList.length() > 0) {
            commandList.setLength(commandList.length() - 1);
        }

        return new String[]{
                "     \u00A72\u00A7l═════════════════════════",
                "     \u00A76\u00A7lWillkommen in \u00A7b\u00A7l" + name + "\u00A76\u00A7l!",
                "",
                "     \u00A7bCommands: " + commandList.toString(),
                "     \u00A77 \u00A92014 FuseMC.net",
                "     \u00A72\u00A7l═════════════════════════"
        };
    }

    @Override
    public boolean disable(){
        return super.disable();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        final Player p = e.getPlayer();

        ChatUtils.broadcastEmptyLines(p, 20);
        Bukkit.getScheduler().scheduleSyncDelayedTask(ZCore.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                ChatUtils.broadcastCentered(p, joinMessage);
            }
        },5L);

    }

    public static String[] getJoinMessage()
    {
        return joinMessage;
    }
}
