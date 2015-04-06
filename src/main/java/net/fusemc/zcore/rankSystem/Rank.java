package net.fusemc.zcore.rankSystem;

import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.shopAPI.PackageType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum Rank {

    NONE(99999, ChatColor.BLACK, "none", "[NONE]", ChatColor.BLACK + ""),
    ADMIN(100, ChatColor.DARK_RED, "admin", "\u00A78\u00a7l[\u00A74Admin\u00A78\u00a7l] \u00a7f", " \u00A78\u00BB\u00A7f "),
    DEVELOPER(90, ChatColor.GRAY, "developer", "\u00A78\u00a7l[\u00A77Dev\u00A78\u00a7l] \u00a7f", " \u00A78\u00BB\u00A7f "),
    TEAMLEADER(75, ChatColor.DARK_AQUA, "teamleader", "\u00A78\u00a7l[\u00A73Teamleiter\u00A78\u00a7l] \u00a7f", " \u00A78\u00BB\u00A7f "),
    SUPPORTER(65, ChatColor.AQUA, "supporter", "\u00A78\u00a7l[\u00A7bSupporter\u00A78\u00a7l] \u00a7f", " \u00A78\u00BB\u00A7f "),
    BUILDER(50, ChatColor.BLUE, "builder", "\u00A78\u00a7l[\u00A79Architekt\u00A78\u00a7l] \u00a7f", " \u00A78\u00BB\u00A7f "),
    YOUTUBER(30, ChatColor.DARK_PURPLE, "youtuber", "\u00A78\u00a7l[\u00A75YouTuber\u00A78\u00a7l] \u00a7f", " \u00A78\u00BB\u00A7f "),
    PREMIUM(10, ChatColor.GOLD, "premium", "\u00a76", " \u00A78\u00BB\u00A7f ") {
        @Override
        public boolean isRank(Player player) {
            Rank rank = RankManager.getRank(player);
            if(Rank.BUILDER.isExactRank(player) || rank.getRanking() >= Rank.TEAMLEADER.getRanking() || rank == Rank.PREMIUM) {
                return true;
            }
            if(ZCore.getShopManager().getPlayerData(player).hasPackage(PackageType.VIP)) {
                return true;
            }
            return false;
        }
    },
    USER(1, ChatColor.GREEN, "user", "\u00A7a", " \u00A78\u00BB\u00A77 ");
	
	//to compare different ranks
	private int ranking;
    //for the tablist and website
    private ChatColor color;
    //for the DB
	private String name;
    //for the chat (name color)
	private String prefix;
    //for the chat (message color)
  	private String suffix;

	Rank(int ranking, ChatColor color, String name, String prefix, String suffix){
		this.ranking = ranking;
        this.color = color;
		this.name = name;
		this.prefix = prefix;
      	this.suffix = suffix;
	}

    public static boolean isTeam(Player p)
    {
        Rank rank = RankManager.getRank(p);
        return (rank.getRanking() >= 65);
    }

	public int getRanking(){
		return this.ranking;
	}
	
	public String getPrefix(){
		return this.prefix;
	}
  
  	public String getSuffix(){
		return this.suffix;
	}
	
	public String getName(){
		return this.name;
	}

    public ChatColor getColor()
    {
        return color;
    }

    public boolean isRank(Player p){
		return RankManager.getRank(p).getRanking() >= this.getRanking();
	}
	
	public boolean isExactRank(Player p){
		return RankManager.getRank(p) == this;
	}

}
