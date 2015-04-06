package net.fusemc.zcore.featureSystem.features.lobbyFeature;

import net.fusemc.zcore.featureSystem.Feature;
import net.fusemc.zcore.featureSystem.FeatureDisableException;

public class LobbyFeature extends Feature {

	private LobbyManager manager;

	public boolean enable(LobbySettings settings) {
        if(super.enable()){
            manager = new LobbyManager(settings);
            return true;
        }
        return false;
	}

	@Override
	public boolean disable() {
        try
        {
            throw new FeatureDisableException("Feature can't be disabled");
        }
        catch (FeatureDisableException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}