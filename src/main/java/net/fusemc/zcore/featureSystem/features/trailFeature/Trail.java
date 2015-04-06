package net.fusemc.zcore.featureSystem.features.trailFeature;

import me.johnking.jlib.JLib;
import me.johnking.jlib.protocol.PacketType;
import me.johnking.jlib.protocol.event.ProtocolPacket;
import me.johnking.jlib.protocol.wrappers.WrappedDataWatcher;
import me.johnking.jlib.reflection.ReflectionUtil;
import me.michidk.DKLib.effects.ParticleEffect;
import net.fusemc.zcore.ZCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;

public enum Trail {

	PURPLE("Purple", ParticleEffect.PORTAL, 5, 0.05F, 0),
	BLACK("Black", ParticleEffect.LARGE_SMOKE, 2, 0, 0),
	WHITE("White", ParticleEffect.FIREWORKS_SPARK, 2, 0.05F, 0.05F),
	FUTURISTIC("Futuristic", ParticleEffect.ENCHANTMENT_TABLE, 5, 0.05F, 1F),
    REDSTONE("Redstone", ParticleEffect.RED_DUST, 5, 0.05F, 0),
    HEART("Heart", ParticleEffect.HEART, 1, 0.05F, 0),
    FLAME("Flame", ParticleEffect.FLAME, 2, 0.05F, 0.05F),
    LAVA("Lava", ParticleEffect.DRIP_LAVA, 3, 0.05F, 0.05F),
    SNOW("Snow", ParticleEffect.SNOW_SHOVEL, 3, 0.05F, 0.05F),
    WATER("Water", ParticleEffect.DRIP_WATER, 3, 0.05F, 0.05F),
    SLIME("Slime", ParticleEffect.SLIME, 3, 0.05F, 0.05F),
    HAPPY("Happy", ParticleEffect.HAPPY_VILLAGER, 1, 0.05F, 0),
    MUSIC("Music", ParticleEffect.NOTE, 1, 0.5F, 1F),
    DIAMOND("Diamond", new ItemStack(Material.DIAMOND), 0, 0, 0),
    GOLD("Gold", new ItemStack(Material.GOLD_INGOT), 0, 0, 0),
    IRON("Iron", new ItemStack(Material.IRON_INGOT), 0, 0, 0),
    COCOA("Cocoa", new ItemStack(Material.INK_SACK, 1, (short)0, (byte)3), 0, 0, 0);

    private static final Constructor<?> METADATA_PACKET = ReflectionUtil.getConstructor(PacketType.Server.ENTITY_METADATA.getPacketClass(), int.class, ReflectionUtil.getMinecraftClass("DataWatcher"), boolean.class);
	
	private String name;
	private ParticleEffect effect;
    private WrappedDataWatcher dataWatcher;
	private int amount;
	private float offset;
    private float speed;
	
	private Trail(String name, ParticleEffect effect, int amount, float offset, float speed){
		this.name = name;
		this.effect = effect;
		this.amount = amount;
		this.offset = offset;
        this.speed = speed;
	}

    private Trail(String name, ItemStack item, int amount, float offset, float speed){
        this.name = name;
        this.dataWatcher = new WrappedDataWatcher();
        this.dataWatcher.setObject(10, CraftItemStack.asNMSCopy(item));
        this.amount = amount;
        this.offset = offset;
        this.speed = speed;
    }
	
	public void playEffect(Player target, Location loc) {
        if (effect == null) return;
        loc = loc.clone().add(0, 0.5, 0);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.canSee(target)) {
                effect.play(player, loc, offset, offset, offset, speed, amount);
            }
        }
    }

    public void playItem(Player target, Location loc) {
        if (dataWatcher == null) return;
        loc = loc.clone().add(0, 0.5, 0);
        TrailFeature feature = ZCore.getFeatureManager().getFeature(TrailFeature.class);
        int entityID = feature.getEntityIDPool().getInteger();
        try {
            ProtocolPacket spawnPacket = new ProtocolPacket(PacketType.Server.SPAWN_ENTITY);
            ProtocolPacket metadataPacket = new ProtocolPacket(PacketType.Server.ENTITY_METADATA, METADATA_PACKET.newInstance(entityID, this.dataWatcher.getHandle(), true));

            spawnPacket.setInt(0, entityID);
            spawnPacket.setInt(1, floor(loc.getX() * 32.0D));
            spawnPacket.setInt(2, floor(loc.getY() * 32.0D));
            spawnPacket.setInt(3, floor(loc.getZ() * 32.0D));
            spawnPacket.setInt(5, 1600);
            spawnPacket.setInt(9, 2);
            spawnPacket.setInt(10, 1);

            for(Player player : Bukkit.getOnlinePlayers()) {
                if(player.canSee(target)) {
                    JLib.getProtocolManager().sendPacket(new Object[]{spawnPacket, metadataPacket}, player);
                }
            }
            feature.getTimer().addEntity(entityID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
        EntityItem itemEntity = new EntityItem(((CraftWorld)loc.getWorld()).getHandle(), loc.getX(), loc.getY(), loc.getZ(), CraftItemStack.asNMSCopy(item));
        itemEntity.motX = 0;
        itemEntity.motY = 0.2;
        itemEntity.motZ = 0;
        PacketPlayOutSpawnEntity spawnPacket = new PacketPlayOutSpawnEntity(itemEntity, 2, 1);
        PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(itemEntity.getId(), itemEntity.getDataWatcher(), true);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.canSee(target)) {
                ((CraftPlayer)player).getHandle().playerConnection.sendPacket(spawnPacket);
                ((CraftPlayer)player).getHandle().playerConnection.sendPacket(metadataPacket);
            }
        }
        ZCore.getFeatureManager().getFeature(TrailFeature.class).getTimer().addEntity(itemEntity);
        */
    }

    private static int floor(double paramDouble) {
        int i = (int)paramDouble;
        return paramDouble < i ? i - 1 : i;
    }
	
	public String getName(){
		return name;
	}
}
