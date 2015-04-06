package net.fusemc.zcore.featureSystem.features.corpseFeature;

import net.fusemc.zcore.featureSystem.features.corpseFeature.connection.NullConnection;
import net.fusemc.zcore.featureSystem.features.corpseFeature.connection.NullNetworkManager;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.server.v1_7_R4.NetworkManager;
import net.minecraft.server.v1_7_R4.PlayerInteractManager;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;

import java.io.IOException;
import java.util.UUID;

public class CustomPlayer extends EntityPlayer
{


	public CustomPlayer(Location loc, String name) {
		super(MinecraftServer.getServer(), ((CraftWorld) loc.getWorld()).getHandle(), new GameProfile(UUID.randomUUID(), name), new PlayerInteractManager(((CraftWorld) loc.getWorld()).getHandle()));
		setPosition(loc.getX(), loc.getY(), loc.getZ());
		try {
			NetworkManager manager = new NullNetworkManager(MinecraftServer.getServer());
			this.playerConnection = new NullConnection(MinecraftServer.getServer(), manager, this);
			manager.a(this.playerConnection);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
