package net.fusemc.zcore.rankSystem;

import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.featureSystem.features.disguiseFeature.DisguiseFeature;
import net.fusemc.zcore.shopAPI.PackageType;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class RankManager {

    private RankManager() {

    }

	private static RankDatabase database = new RankDatabase();

    private static HashMap<String, Rank> ranks = new HashMap<>();

	public static Rank getRankFromString(String s){
		for(Rank r : Rank.values()){
			if(r.name().equalsIgnoreCase(s))
			return r;
		}
		return Rank.USER;
	}
	
	public static Rank getRank(Player p){
		if(!ranks.containsKey(p.getUniqueId().toString()))
			loadPlayerRank(p);
        Rank rank = ranks.get(p.getUniqueId().toString());
        DisguiseFeature feature = ZCore.getFeatureManager().getFeature(DisguiseFeature.class);
        if(feature.isEnabled()) {
            if(feature.isDisguised(p)){
                rank = Rank.USER;
            }
        }
        if(rank == Rank.USER) {
            if(ZCore.getShopManager().getPlayerData(p).hasPackage(PackageType.VIP)) {
                rank = Rank.PREMIUM;
            }
        }
        return rank;
	}

	public static void loadPlayerRank(Player p){
		ranks.put(p.getUniqueId().toString(), getRankFromDB(p));
	}

    public static Rank getRankFromDB(Player p)
    {
        return getDatabase().getRank(p.getUniqueId().toString());
    }

    protected static void removeSavedRank(Player p){
        ranks.remove(p.getUniqueId().toString());
    }

    protected static RankDatabase getDatabase(){
        return database;
    }
	
}
