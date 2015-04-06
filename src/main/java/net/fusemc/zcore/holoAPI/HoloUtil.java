package net.fusemc.zcore.holoAPI;

import me.johnking.jlib.protocol.PacketType;
import me.johnking.jlib.protocol.event.ProtocolPacket;
import me.johnking.jlib.protocol.wrappers.WrappedDataWatcher;
import net.fusemc.zcore.util.ReflectionUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;

public class HoloUtil
{
	private static final Class<?> classEntity = ReflectionUtil.getMinecraftClass("Entity");
	private static final Field entityCount = ReflectionUtil.getField(classEntity, "entityCount");
	
    /**
     * @return the skull packet
     */
    public static Object getSkullPacket(Location location, int id)
    {
    	try {
    		ProtocolPacket packet = new ProtocolPacket(PacketType.Server.SPAWN_ENTITY);
    		packet.setInt(0, id);
    		packet.setInt(1, floor(location.getX() * 32));
    		packet.setInt(2, floor((location.getY() + 55) * 32));
    		packet.setInt(3, floor(location.getZ() * 32));
    		packet.setInt(9, 66);
    		return packet.getHandle();
    	} catch (Exception e){
    		e.printStackTrace();
    	}
        return null;
    }

    /**
     * @return the horse packet
     */
    public static Object getHorsePacket(Location location, int id, String message)
    {
    	try {
    		ProtocolPacket packet = new ProtocolPacket(PacketType.Server.SPAWN_ENTITY_LIVING);
            packet.setInt(0, id);
            packet.setInt(1, 100);
    		packet.setInt(2, floor(location.getX() * 32));
    		packet.setInt(3, floor((location.getY() + 55) * 32));
    		packet.setInt(4, floor(location.getZ() * 32));
            WrappedDataWatcher watcher = new WrappedDataWatcher();
            watcher.setObject(10, ChatColor.translateAlternateColorCodes('&', message));
            watcher.setObject(11, Byte.valueOf((byte) 1));
            watcher.setObject(12, Integer.valueOf(-1700000));
            packet.setObject(watcher.getHandle().getClass(), 0, watcher.getHandle());
            return packet.getHandle();
    	} catch (Exception e){
    		e.printStackTrace();
    	}
    	return null;
    }

    /**
     * @return the armor_stand_packet
     */
    public static Object getArmorStandPacket(Location location, int id, String message) {
        try {
            ProtocolPacket packet = new ProtocolPacket(PacketType.Server.SPAWN_ENTITY_LIVING);
            packet.setInt(0, id);
            packet.setInt(1, 30);
            packet.setInt(2, floor(location.getX() * 32));
            packet.setInt(3, floor((location.getY() - 2) * 32));
            packet.setInt(4, floor(location.getZ() * 32));

            WrappedDataWatcher watcher = new WrappedDataWatcher();
            watcher.setObject(0, (byte) 32);
            watcher.setObject(2, ChatColor.translateAlternateColorCodes('&', message));
            watcher.setObject(3, (byte) 1);

            packet.setObject(watcher.getHandleClass(), 0, watcher.getHandle());
            return packet.getHandle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static int floor(double value){
    	int i = (int) value;
    	return value < i ? i - 1 : i;
    }

    /**
     * @return the attach packet for two entities
     */
    public static Object getAttachPacket(int entity, int vehicle)
    {
        try{
        	ProtocolPacket packet = new ProtocolPacket(PacketType.Server.ATTACH_ENTITY);
        	packet.setInt(1, vehicle);
        	packet.setInt(2, entity);
        	
        	return packet.getHandle();
        } catch (Exception e){
        	e.printStackTrace();
        }
        return null;
    }

    /**
     * @return a packet that gives a entity a velocity
     */
    public static Object getVelocityPacket(int entity, Vector velocity)
    {
        try {
        	ProtocolPacket packet = new ProtocolPacket(PacketType.Server.ENTITY_VELOCITY);
        	packet.setInt(0, entity);
        	packet.setInt(1, (int) (velocity.getX() * 8000));
        	packet.setInt(2, (int) (velocity.getY() * 8000));
        	packet.setInt(3, (int) (velocity.getZ() * 8000));
        	
        	return packet.getHandle();
        } catch (Exception e){
        	e.printStackTrace();
        }
        return null;
    }

    /**
     * @return a packet to destroy the entities
     */
    public static Object getDestroyPacket(int... entity)
    {
    	try {
    		Object packet = PacketType.Server.ENTITY_DESTROY.getPacketClass().getConstructors()[1].newInstance(entity);
    		return packet;
    	} catch (Exception e){
    		e.printStackTrace();
    	}

        return null;
    }
    
    /**
     * @return a centered location
     */
    public static Location centerLocation(Location loc)
    {
        return new Location(loc.getWorld(), loc.getBlockX() + 0.5D, loc.getBlockY(), loc.getBlockZ() + 0.5D);
    }

    /**
     * Register a free EntityID
     *
     * @return the free EntityID
     */
	public static int getFreeEID() {
		try {
			int entityId = entityCount.getInt(null) + 1;
			entityCount.setInt(null, entityId + 1);
			return entityId;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
        return -1;
    }
}
