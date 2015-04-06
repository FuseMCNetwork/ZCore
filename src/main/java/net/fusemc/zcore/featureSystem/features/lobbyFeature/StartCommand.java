package net.fusemc.zcore.featureSystem.features.lobbyFeature;

import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.rankSystem.Rank;
import net.fusemc.zcore.util.Strings;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Copyright by michidk
 * Created: 12.08.2014.
 */
public class StartCommand implements CommandExecutor {

    private boolean canExecute(CommandSender commandSender) {
        //console
        if (!(commandSender instanceof Player)) return true;
        Player player = (Player) commandSender;
        //offline
        if (ZCore.OFFLINE) return true;
        //teamleader
        if (Rank.BUILDER.isRank(player)) return true;
        //premium
        /*  disabled
        if (Rank.PREMIUM.isRank(player)) {
            //if more than 7 players
            if (Bukkit.getOnlinePlayers().length > 7) return true;
        }
        */
        return false;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!canExecute(commandSender)) {
            commandSender.sendMessage(Strings.UNKNOWN_COMMAND);
            return true;
        }

        if (CountdownScheduler.getInstance().started) {
            commandSender.sendMessage("\u00A78\u00A7l[\u00A74\u2716\u00A78\u00A7l] \u00A7cDas Spiel hat bereits begonnen!");
            return true;
        }

        String name;
        if (commandSender instanceof Player) {
            name = ((Player) commandSender).getName();
        } else {
            name = "CONSOLE";
        }

        CountdownScheduler.getInstance().startGame();
        commandSender.sendMessage("\u00A78\u00A7l[\u00A79\u00A7li\u00A78\u00A7l] \u00A7aSpiel gestartet!");

        Bukkit.broadcastMessage("\u00a78\u00a7l[\u00a79\u00a7li\u00a78\u00a7l] \u00a76" + name + " \u00a7ahat das Spiel gestartet!");
        Bukkit.getScheduler().scheduleSyncDelayedTask(ZCore.getInstance(), new Runnable() {
            @Override
            public void run() {
                CountdownScheduler.getInstance().startGame();
            }
        }, 100L);

        return true;
    }
}
