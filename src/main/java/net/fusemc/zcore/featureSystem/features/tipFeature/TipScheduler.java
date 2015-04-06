package net.fusemc.zcore.featureSystem.features.tipFeature;

import net.fusemc.zcore.ZCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;

public class TipScheduler implements Runnable {

    private TipType type = TipType.ENDERBAR;
	private List<String> tips;
    private boolean showPrefix;
	private int tip = 0;
	
	public TipScheduler(TipType type, List<String> tips, boolean showPrefix){
        this.type = type;
		this.tips = tips;
        this.showPrefix = showPrefix;
	}
	
	@Override
	public void run() {
        String str = "";
        if (showPrefix) str = "\u00A78\u00A7l[\u00A79\u00A7li\u00A78\u00A7l] \u00A7a" + ChatColor.GRAY;
        str = str + getCurrentTip();

        if (tips.size() <= 1)
        {
            displayMessage(str, 100);
        }
        else    //"TIP [" + (tip + 1) + "/" + tips.size() + "] "
        {
            displayMessage(str, ((float) (tip + 1)) * 100 / tips.size());

            tip++;
            if(tips.size() == tip)
                tip = 0;
        }

    }

    public void displayMessage(String message, float percent) {
        if (type == TipType.ENDERBAR) {

            ZCore.getBarAPI().setMessage(message, percent);

        } else {
            Bukkit.broadcastMessage(message);
        }
    }

    public String getCurrentTip()
    {
        return tips.get(tip);
    }

}
