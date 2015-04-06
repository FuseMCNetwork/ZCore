package net.fusemc.zcore.featureSystem.features.tipFeature;

/**
 * Copyright by michidk
 * Created: 23.09.2014.
 */
public enum TipType {

    CHAT,
    ENDERBAR;

    public static TipType getType(String s) {
        for (TipType type:TipType.values()) {
            if (type.name().equalsIgnoreCase(s)) {
                return type;
            }
        }
        return null;
    }

}
