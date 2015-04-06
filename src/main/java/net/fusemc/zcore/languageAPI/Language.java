package net.fusemc.zcore.languageAPI;

public final class Language {
    private String name;
    private String minecraftName;
    private String translationId;
    private Dictionary dictionary;

    public Language(String name, String minecraftName, String translationId) {
        this.name = name;
        this.minecraftName = minecraftName;
        this.translationId = translationId;
        this.dictionary = new Dictionary(this);
    }

    public String getName() {
        return name;
    }

    public String getMinecraftName() {
        return minecraftName;
    }

    public String getTranslationId() {
        return translationId;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }
}
