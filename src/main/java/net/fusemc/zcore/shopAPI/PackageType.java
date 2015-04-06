package net.fusemc.zcore.shopAPI;

/**
 * Created by Niklas on 21.08.2014.
 */
public enum PackageType {
    VIP("VIP"),
    UNKNOWN("unknown");

    private String packageName;

    private PackageType(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public static PackageType fromName(String name) {
        for (PackageType type : PackageType.values()) {
            if (name.equals(type.getPackageName())) {
                return type;
            }
        }
        return PackageType.UNKNOWN;
    }
}
