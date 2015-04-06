package net.fusemc.zcore.featureSystem.features.jumppadFeature;

import fr.neatmonster.nocheatplus.checks.CheckType;
import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.featureSystem.Feature;
import net.fusemc.zcore.util.NCPUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class JumpPapFeature extends Feature {

    @Override
    public boolean enable(){
        return super.enable();
    }

    @Override
    public boolean disable(){
        return super.disable();
    }
 
    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent e)
    {
        if (e.getFrom().getBlock().equals(e.getTo().getBlock()))
        	return;
        Player p = e.getPlayer();
		Location loc = e.getTo().getBlock().getLocation().clone().add(0, -2, 0);
        if (!(loc.getBlock().getType().equals(Material.SIGN) || loc.getBlock().getType().equals(Material.WALL_SIGN) || loc.getBlock().getType().equals(Material.SIGN_POST)))
            return;

        BlockState state = loc.getBlock().getState();
        if (!(state instanceof Sign))
            return;

        Sign sign = (Sign) state;
        
        if(!sign.getLine(0).equalsIgnoreCase("[Jump]"))
        	return;
        
        int height = 5;
        int lenght = 15;
        
        try{
        	height = Integer.parseInt(sign.getLine(1));
        	lenght = Integer.parseInt(sign.getLine(2));
        }
        catch(NumberFormatException n){
        }
        
		double Height = height / 15.0D;
        double Lenght = lenght / 8.0D;
        
        JumpPadEvent jumpPadEvent = new JumpPadEvent(p, Lenght, Height);
        Bukkit.getPluginManager().callEvent(jumpPadEvent);
        
        if(!e.isCancelled())
        {
            if (!ZCore.OFFLINE) {
                NCPUtils.exemptPlayer(e.getPlayer(), 3, CheckType.MOVING);
            }

            p.setVelocity(new Vector(0, 1.25, 0));
            Bukkit.getScheduler().scheduleSyncDelayedTask(ZCore.getInstance(), new JumpPadScheduler(p, (float) jumpPadEvent.getHeight(), (float) jumpPadEvent.getLength()), 10L);
            //p.setVelocity(p.getLocation().getDirection().setY(jumpPadEvent.getHeight()).multiply(jumpPadEvent.getLength()));
            p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1,0);
        }
 
        e.setCancelled(jumpPadEvent.isCancelled());
    }
}