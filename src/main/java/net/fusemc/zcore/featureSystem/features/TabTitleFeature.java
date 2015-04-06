package net.fusemc.zcore.featureSystem.features;

import net.fusemc.zcore.featureSystem.Feature;
import net.fusemc.zcore.util.MC1_8Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Niklas on 06.10.2014.
 */
public class TabTitleFeature extends Feature {

    private static String HEADER = "\u00a77\u00BB \u00a76FuseMC \u00a77- \u00a76%s \u00a77\u00AB\n";
    private static String FOOTER = "\n\u00a77Website: \u00a76www.FuseMC.net\n\u00a77Teamspeak: \u00a76ts.FuseMC.net\n\u00a77Shop: \u00a76shop.FuseMC.net";
    private String name;

    public boolean enable(String name) {
        if (super.enable()) {
            this.name = name;
            return true;
        }
        return false;
    }

    @Override
    protected boolean disable() {
        return super.disable();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        MC1_8Utils.setTabTitle(event.getPlayer(), String.format(HEADER, name), FOOTER);
    }
}
