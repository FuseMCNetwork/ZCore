package net.fusemc.zcore.languageAPI;

import net.fusemc.zcore.ZCore;
import org.apache.commons.lang.text.StrSubstitutor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

public final class TranslationHelper {
    public static String translate(Player p, String id, String... substitutions) {
        checkArgument(substitutions.length % 2 == 0, "Substitution count must be dividable by two");

        Language lang = ZCore.getLanguageSettingsManager().getLanguage(p);
        String translation = lang.getDictionary().getTranslation(id);
        if (translation == null) {
            // get english translation
            translation = ZCore.getLanguageManager().getLanguageByName("en_US").getDictionary().getTranslation(id);
        }

        Map<String, String> replacementMap = new HashMap<>();

        // note: this increments by two
        for (int i = 0; i < substitutions.length; i+=2) {
            replacementMap.put(substitutions[i], substitutions[i+1]);
        }

        StrSubstitutor substitutor = new StrSubstitutor(replacementMap);
        return substitutor.replace(translation);
    }

    public static void broadcast(Collection<Player> players, String id, String... substitutions) {
        for (Player p : players) {
            p.sendMessage(translate(p, id, substitutions));
        }
    }

    public static void broadcast(String id, String... substitutions) {
        broadcast(Arrays.asList(Bukkit.getOnlinePlayers()), id, substitutions);
    }
}
