package net.fusemc.zcore.barAPI;

import me.johnking.jlib.protocol.PacketType;
import me.johnking.jlib.protocol.event.ProtocolPacket;
import me.johnking.jlib.protocol.wrappers.WrappedDataWatcher;
import me.johnking.jlib.reflection.ReflectionUtil;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.Constructor;

/**
 * Created by Marco on 13.10.2014.
 */
public abstract class EntityBar {

    protected final Constructor<?> METADATA_PACKET = ReflectionUtil.getConstructor(PacketType.Server.ENTITY_METADATA.getPacketClass(), int.class, ReflectionUtil.getMinecraftClass("DataWatcher"), boolean.class);

    protected int entityID;
    protected Vector location;
    protected WrappedDataWatcher dataWatcher;
    protected boolean changed;
    private boolean dead = true;
    private boolean action;

    public EntityBar(int entityID) {
        this.entityID = entityID;
        this.location = new Vector(0, 0, 0);
        this.dataWatcher = new WrappedDataWatcher();

        this.dataWatcher.setObject(6, (float) getMaxHealth());
    }

    public void setMessage(String message) {
        this.dataWatcher.setObject(2, message);
        this.changed = true;
        if(isDead()) {
            spawn();
        }
    }

    public void setPercent(float percent) {
        float health = getMaxHealth() * percent / 100;
        if(health < 1.0F) {
            health = 1.0F;
        }
        this.dataWatcher.setObject(6, health);
        this.changed = true;
        if(isDead()) {
            spawn();
        }
    }

    public boolean hasChanged() {
        boolean result = changed;
        changed = false;
        return result;
    }

    public void spawn() {
        if(!this.isDead()) {
            return;
        }
        this.dead = false;
        this.action = true;
    }

    public void die() {
        if(this.isDead()) {
            return;
        }
        this.dead = true;
        this.action = true;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean isAction() {
        boolean result = action;
        action = false;
        return result;
    }

    public abstract int getMaxHealth();

    public abstract int getTypeID();

    public abstract void updateLocation(Player player);

    public ProtocolPacket getSpawnPacket() {
        try {
            ProtocolPacket packet = new ProtocolPacket(PacketType.Server.SPAWN_ENTITY_LIVING);
            packet.setInt(0, entityID);
            packet.setInt(1, getTypeID());
            packet.setInt(2, floor(this.location.getX() * 32.0D));
            packet.setInt(3, floor(this.location.getY() * 32.0D));
            packet.setInt(4, floor(this.location.getZ() * 32.0D));

            packet.setObject(this.dataWatcher.getHandleClass(), 0, this.dataWatcher.getHandle());
            return packet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ProtocolPacket getMetaDataPacket() {
        try {
            ProtocolPacket packet = new ProtocolPacket(PacketType.Server.ENTITY_METADATA, METADATA_PACKET.newInstance(this.entityID, this.dataWatcher.getHandle(), true));
            return packet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ProtocolPacket getDestroyPacket() {
        try {
            ProtocolPacket packet = new ProtocolPacket(PacketType.Server.ENTITY_DESTROY);
            packet.setObject(int[].class, 0, new int[]{entityID});
            return packet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ProtocolPacket getTeleportPacket() {
        try {
            ProtocolPacket packet = new ProtocolPacket(PacketType.Server.ENTITY_TELEPORT);
            packet.setInt(0, entityID);
            packet.setInt(1, floor(location.getX() * 32.0D));
            packet.setInt(2, floor(location.getY() * 32.0D));
            packet.setInt(3, floor(location.getZ() * 32.0D));
            return packet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int floor(double value){
        int i = (int) value;
        return value < i ? i - 1 : i;
    }
}
