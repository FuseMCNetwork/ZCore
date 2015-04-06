package net.fusemc.zcore.rankSystem;

import net.fusemc.zcore.ZCore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RankDatabase {

	public Rank getRank(String uuid){
        if (ZCore.OFFLINE) {
            System.out.println("OFFLINE MODE IN ZCORE ENABLED!!!");
            return Rank.USER;
        }
        Rank rank = Rank.USER;
		try (Connection connection = ZCore.getPlayerConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT rank FROM rank WHERE uuid = ?");
            statement.setString(1, uuid);
			ResultSet result = statement.executeQuery();

			if (result.next())
            {
				rank = RankManager.getRankFromString(result.getString("rank"));
			}
            result.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return rank;
	}
}
