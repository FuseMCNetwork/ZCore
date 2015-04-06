package net.fusemc.zcore.util;

/**
 * @author michidk
 */
public class UUIDUtil
{

    public static String removeDashes(String uuid)
    {
        return uuid.replaceAll("-", uuid);
    }

    public static String addDashes(String uuid)
    {
        return uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
    }

}
