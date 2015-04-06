package net.fusemc.zcore.rankSystem;

import net.fusemc.zcore.ZCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class RankListener implements Listener
{

    @EventHandler
    public void onJoin(PlayerLoginEvent e)
    {
        if (ZCore.OFFLINE) return;

        RankManager.loadPlayerRank(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e)
    {
        if (ZCore.OFFLINE) return;

        RankManager.removeSavedRank(e.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent e)
    {
        if (ZCore.OFFLINE) return;

        RankManager.removeSavedRank(e.getPlayer());
    }
}
