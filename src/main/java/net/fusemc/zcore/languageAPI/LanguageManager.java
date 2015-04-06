package net.fusemc.zcore.languageAPI;

import net.fusemc.zcore.ZCore;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LanguageManager implements Listener {
    private Map<String, Language> languageMap = new HashMap<>();

    public Language getLanguageByName(String name) {
        return languageMap.get(name);
    }

    @EventHandler
    public void onRegisterGroups(TranslationGroupRegisterEvent e) {
        e.addGroup("zcore");
    }

    public void load() {
        try (Connection networkConnection = ZCore.getNetworkConnection()) {
            Statement st = networkConnection.createStatement();
            ResultSet rs = st.executeQuery("SELECT name, minecraft_name, translation_id FROM language_definitions");

            while (rs.next()) {
                String name = rs.getString(1);
                String minecraftName = rs.getString(2);
                String translationId = rs.getString(3);
                Language lang = new Language(name, minecraftName, translationId);
                languageMap.put(name, lang);
            }

            rs.close();
            st.close();

            TranslationGroupRegisterEvent event = new TranslationGroupRegisterEvent();
            Bukkit.getPluginManager().callEvent(event);
            Collection<String> groups = event.getGroups();
            if (groups.isEmpty()) {
                // no translations required
                return;
            }

            PreparedStatement ps = networkConnection.prepareStatement("CALL lang_translations_for_group(?)");
            for (String group : groups) {
                ps.setString(1, group);
                rs = ps.executeQuery();

                while (rs.next()) {
                    String lang = rs.getString("language");
                    Language language = getLanguageByName(lang);
                    language.getDictionary().addTranslation(rs.getString("translation_id"), rs.getString("translation"));
                }

                rs.close();
            }

            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
