package net.fusemc.zcore.featureSystem.features.messageFeature;

import net.fusemc.zcore.featureSystem.Feature;
import net.fusemc.zcore.util.MC1_8Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathFeature extends Feature{

    private static String VICTIM_MESSAGE = "\u00A78\u00A7l[\u00A74\u2694\u00A78\u00A7l] \u00A73%s \u00A7ahat dich get\u00F6tet!";
    private static String KILLER_MESSAGE = "\u00A78\u00A7l[\u00A72\u2694\u00A78\u00A7l] \u00A7aDu hast \u00A73%s \u00A7aget\u00F6tet!";
    private static String DIED = "\u00A78\u00A7l[\u00A74\u2694\u00A78\u00A7l] \u00A7aDu bist gestorben!";

    @Override
    public boolean enable(){
        return super.enable();
    }

    @Override
    public boolean disable(){
        return super.disable();
    }

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDeath(PlayerDeathEvent e){
		e.setDeathMessage(null);
		Player p = e.getEntity();

		if(p.getKiller() instanceof Player)
        {
            Player killer = p.getKiller();
            p.sendMessage(String.format(VICTIM_MESSAGE, killer.getDisplayName()));

            if (MC1_8Utils.is1_8(killer)) {
                MC1_8Utils.displayActionbarMessage(killer, String.format(KILLER_MESSAGE, p.getDisplayName()));
            } else {
                killer.sendMessage(String.format(KILLER_MESSAGE, p.getDisplayName()));
            }
        }
        else
        {
            p.sendMessage(DIED);
        }
	}
}
