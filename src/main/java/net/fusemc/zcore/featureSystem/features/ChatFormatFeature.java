package net.fusemc.zcore.featureSystem.features;

import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.featureSystem.Feature;
import net.fusemc.zcore.featureSystem.features.spectatorFeature.SpectatorFeature;
import net.fusemc.zcore.rankSystem.Rank;
import net.fusemc.zcore.rankSystem.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ChatFormatFeature extends Feature {

    @Override
    public boolean enable() {
        return super.enable();
    }

    @Override
    public boolean disable() {
        return super.disable();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(final PlayerJoinEvent e) {
        Scoreboard sc = Bukkit.getScoreboardManager().getNewScoreboard();
        Rank ownRank = RankManager.getRank(e.getPlayer());
        for (Player p : Bukkit.getOnlinePlayers()) {
            Rank rank = RankManager.getRank(p);
            Team team = sc.getTeam(rank.getName());
            if (team == null) team = sc.registerNewTeam(rank.getName());
            team.setCanSeeFriendlyInvisibles(false);
            team.setPrefix(rank.getColor() + "");
            team.addPlayer(p);
            if (!p.equals(e.getPlayer())) {
                Team ownTeam = p.getScoreboard().getTeam(ownRank.getName());
                if (ownTeam == null) ownTeam = p.getScoreboard().registerNewTeam(ownRank.getName());
                ownTeam.setCanSeeFriendlyInvisibles(false);
                ownTeam.setPrefix(ownRank.getColor() + "");
                ownTeam.addPlayer(e.getPlayer());
            }
        }
        e.getPlayer().setScoreboard(sc);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        Rank r = RankManager.getRank(p);

        SpectatorFeature feature = ZCore.getFeatureManager().getFeature(SpectatorFeature.class);
        if(feature.isEnabled()){
            synchronized (feature.getSpectators()){
                if(feature.getSpectators().contains(p)){
                    String message = "\u00a78\u00a7l[\u00a79Zuschauer\u00a78\u00a7l] " + r.getPrefix() + p.getDisplayName() + r.getSuffix() + e.getMessage();
                    for(Player spectator : feature.getSpectators()){
                        spectator.sendMessage(message);
                    }
                    e.setCancelled(true);
                    return;
                }
            }
        }

        e.setFormat(r.getPrefix() + "%1$s" + r.getSuffix() + "%2$s");
        if (Rank.SUPPORTER.isRank(p)) { //replace color codes
            e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
        }
        //e.setFormat(RankManager.getRank(p).getPrefix() + p.getDisplayName() + RankManager.getRank(p).getSuffix() + e.getMessage());
    }

    public boolean existObjective(Scoreboard sc, String s) {
        for (Objective ob : sc.getObjectives()) {
            if (ob.getName().equals(s)) {
                return true;
            }
        }
        return false;
    }

    public boolean existTeam(Scoreboard sc, String s) {
        for (Team team : sc.getTeams()) {
            if (team.getName().equals(s)) {
                return true;
            }
        }
        return false;
    }

    public String trim(String str) {
        if (str.length() > 16) {
            return str.substring(0, 16);
        } else {
            return str;
        }
    }
}
