package net.fusemc.zcore.featureSystem.features.corpseFeature.connection;

import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.server.v1_7_R4.NetworkManager;
import net.minecraft.util.io.netty.channel.Channel;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

public class NullNetworkManager extends NetworkManager
{
	@SuppressWarnings("unused")
	private Channel k;
	@SuppressWarnings("unused")
	private SocketAddress l;
	
    @SuppressWarnings("serial")
	public NullNetworkManager(MinecraftServer server) throws IOException
    {
        super(false);

        try
        {
            Field channel = getOrRegisterNMSField("NetworkManager", "k");
            NullChannel nullchannel = new NullChannel(null);
            channel.set(this, nullchannel);
            k = nullchannel;

            Field address = getOrRegisterNMSField("NetworkManager", "l");
            SocketAddress socketaddress = new SocketAddress() {};
            address.set(this, socketaddress);
            l = socketaddress;
        }
        catch(Exception e)
        {
        }

    }

    /**
     * following by
     * @author https://github.com/kumpelblase2/Remote-Entities/blob/master/src/main/java/de/kumpelblase2/remoteentities/utilities/ReflectionUtil.java
     */

    /**
     * Gets a declared field of the given class and caches it.
     * If a field is not cached it will attempt to get it from the given class.
     *
     * @param inSource  The class which has the field
     * @param inField   The field name
     * @return          The field
     */
    private static final Map<String, Field> s_cachedFields = new HashMap<String, Field>();
    public static Field getOrRegisterField(Class<?> inSource, String inField)
    {
        Field field;
        try
        {
            String id = inSource.getName() + "_" + inField;
            if(s_cachedFields.containsKey(id))
                field = s_cachedFields.get(id);
            else
            {
                field = inSource.getDeclaredField(inField);
                field.setAccessible(true);
                s_cachedFields.put(id, field);
            }

            return field;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    static Field getOrRegisterNMSField(String inNMSClass, String inField)
    {
        Field field;
        try
        {
            String id = inNMSClass + "_" + inField;
            if(s_cachedFields.containsKey(id))
                field = s_cachedFields.get(id);
            else
            {
                field = getNMSClassByName(inNMSClass).getDeclaredField(inField);
                field.setAccessible(true);
                s_cachedFields.put(id, field);
            }

            return field;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Gets the current minecraft revision
     *
     * @return  The revision as string in the format "X.X_RX"
     */
    @SuppressWarnings("rawtypes")
	public static String getMinecraftRevision()
    {
        Class serverClass = Bukkit.getServer().getClass();
        String remaining = serverClass.getPackage().getName().replace("org.bukkit.craftbukkit.", "");
        return remaining.split("\\.")[0];
    }
    /**
     * Gets the nms class with the given name
     *
     * @param inName    The internal name of the class
     * @return          The class
     */
    public static Class<?> getNMSClassByName(String inName)
    {
        try
        {
            return Class.forName("net.minecraft.server." + getMinecraftRevision() + "." + inName);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }



}
