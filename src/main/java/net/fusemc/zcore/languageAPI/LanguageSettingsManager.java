package net.fusemc.zcore.languageAPI;

import net.fusemc.zcore.ZCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LanguageSettingsManager implements Listener {
    private Map<String, String> languageMap = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPreLogin(AsyncPlayerPreLoginEvent e) {
        try (Connection conn = ZCore.getPlayerConnection()) {

            PreparedStatement ps = conn.prepareStatement("SELECT language FROM language_settings WHERE user = ?");
            ps.setString(1, e.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                // set to german
                // TODO read from geoip
                languageMap.put(e.getUniqueId().toString(), "de_DE");
                rs.close();
                ps.close();
                return;
            }

            languageMap.put(e.getUniqueId().toString(), rs.getString("language"));

            rs.close();
            ps.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public Language getLanguage(Player p) {
        return ZCore.getLanguageManager().getLanguageByName(languageMap.get(p.getUniqueId().toString()));
    }

    public void setLanguage(Player p, Language language) {
        // save to mysql
        languageMap.put(p.getUniqueId().toString(), language.getName());

        try (Connection conn = ZCore.getPlayerConnection()) {

            PreparedStatement ps = conn.prepareStatement("INSERT INTO language_settings(user, language) VALUES(?, ?)" +
                    "ON DUPLICATE KEY UPDATE language=VALUES(language)");
            ps.setString(1, p.getUniqueId().toString());
            ps.setString(2, language.getName());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
