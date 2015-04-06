package net.fusemc.zcore.languageAPI;

import java.util.HashMap;
import java.util.Map;

public class Dictionary {
    private Language language;
    private Map<String, String> translations = new HashMap<>();

    public Dictionary(Language language) {
        this.language = language;
    }

    public void addTranslation(String translationId, String translation) {
        translations.put(translationId, translation);
    }

    public Language getLanguage() {
        return language;
    }

    public String getTranslation(String id) {
        return translations.get(id);
    }
}
