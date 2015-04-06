package net.fusemc.zcore.voteAPI;

import net.fusemc.zcore.ZCore;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * Copyright by michidk
 * Created: 22.10.2014.
 */
public class VoteScoreboard implements Listener {

    private static final String SB_PREFIX =  "\u00A7a";
    private static final String SB_SUFFIX = "";

    private String title;
    private Vote vote;

    public VoteScoreboard(String title, Vote vote) {
        this.title = title;
        this.vote = vote;
    }

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, ZCore.getInstance());

        for (Player p : Bukkit.getOnlinePlayers()) {
            registerScoreboard(p);
        }
    }

    public void unregister() {
        HandlerList.unregisterAll(this);

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getScoreboard().getObjective(getScoreboardTrimedName()) != null) {
                p.getScoreboard().getObjective(getScoreboardTrimedName()).unregister();
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        registerScoreboard(e.getPlayer());
    }

    public void registerScoreboard(Player player) {
        Scoreboard board = player.getScoreboard();
        Objective objective = null;
        if (board.getObjective(getScoreboardTrimedName()) == null) {
            objective = board.registerNewObjective(getScoreboardTrimedName(), "dummy");
        } else {
            objective = board.getObjective(getScoreboardTrimedName());
        }
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        for (VoteItem item : vote.getVoteItems()) {
            String trimedTitle = trim(title);

            Team team = null;
            if (board.getTeam(trimedTitle) == null) {
                team = board.registerNewTeam(trim(title));
            } else {
                team = board.getTeam(trimedTitle);
            }

            /* need that for strings > 16 chars, but is much slower because of OfflinePlayer
            OfflinePlayer ofp = Bukkit.getOfflinePlayer(trim(item.getScoreboardName()));
            team.setSuffix(trim32(getScoreboardName()));
            team.addPlayer(ofp);
            objective.getScore(ofp).setScore(1);
            objective.getScore(ofp).setScore(item.getVotes());
            */

            String name = trim(item.getScoreboardName());
            objective.getScore(name).setScore(1);
            objective.getScore(name).setScore(item.getVotes());
        }
    }

    public void updateVotes(VoteItem item) {
        if (item == null) return;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getScoreboard().getObjective(getScoreboardTrimedName()) != null) {
                p.getScoreboard().getObjective(getScoreboardTrimedName()).getScore(trim(item.getScoreboardName())).setScore(item.getVotes());
            }
        }
    }

    public String trim(String s) {
        return s.substring(0, Math.min(s.length(), 16));
    }

    public String trim32(String s) {
        if (s.length() < 16) return "";
        return s.substring(16, Math.min(s.length(), 32));
    }

    public String getScoreboardName() {
        return SB_PREFIX + " " + getTitle() + " " + SB_SUFFIX;
    }

    private String trimedScoreboardname;
    public String getScoreboardTrimedName() {
        if (trimedScoreboardname == null) {
            trimedScoreboardname = trim(getScoreboardName());
        }
        return trimedScoreboardname;
    }

    public String getTitle() {
        return title;
    }

}
