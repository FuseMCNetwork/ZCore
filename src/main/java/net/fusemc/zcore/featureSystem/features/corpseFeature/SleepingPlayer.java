package net.fusemc.zcore.featureSystem.features.corpseFeature;

import org.bukkit.Location;

public class SleepingPlayer extends CustomPlayer{

	public SleepingPlayer(Location loc, String name) {
		super(loc, name);
		this.sleeping = true;
	}
	
	@Override
	public void a(boolean flag, boolean flag1, boolean flag2) {

	}

}
