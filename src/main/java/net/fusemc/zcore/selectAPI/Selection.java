package net.fusemc.zcore.selectAPI;

import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.util.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public abstract class Selection implements Listener {

	private String title;
	private ItemStack open;
	private SelectInventory inventory;

	private HashMap<HumanEntity, SelectItem> playerselect = new HashMap<HumanEntity, SelectItem>();

	public Selection(String title, ItemStack open, SelectItem[] parts) {
		if (parts.length == 0)
			throw new IllegalArgumentException("Not enough parts [have to >0]!");
		this.title = title;
		this.open = open;
		this.inventory = new SelectInventory(title, parts);
		Bukkit.getPluginManager().registerEvents(this, ZCore.getInstance());
	}

	public boolean select(ItemStack item, HumanEntity he) {
		SelectItem select = inventory.getSelectPart(item);
		if(select == null)
			return false;
		return select(select, he);
	}

	public boolean select(String name, HumanEntity he) {
		SelectItem select = inventory.getSelectPart(name);
		if(select == null)
			return false;
		return select(select, he);
	}
	
	private boolean select(SelectItem select, HumanEntity he) {
		SelectItem old = playerselect.get(he);
		if(old != null){
			if(old.equals(select))
				return false;
		}
		boolean cancel = call(title, he, select, old).isCancelled();
		if(!cancel)
			playerselect.put(he, select);
		return true;
	}
	
	public abstract SelectEvent call(String title, HumanEntity he, SelectItem select, SelectItem unselect);

	public void finish() {
		HandlerList.unregisterAll(this);
		for(Player p: Bukkit.getOnlinePlayers())
			p.getInventory().remove(open);
	}
	
	public String getTitle(){
		return title;
	}
	
	public Inventory getInventory(){
		return inventory.getInventory();
	}
	
	public ItemStack getOpen(){
		return open;
	}
	
	public SelectItem getSelected(HumanEntity he){
		SelectItem selected = playerselect.get(he);
		if(selected == null)
			selected = inventory.getSelectParts()[0];
		return selected;
	}

	// detects InventoryClick
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (!e.getInventory().getTitle().equals(this.getTitle()))
			return;
		e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();

		this.select(e.getCurrentItem(), p);
        Sounds.SELECT.play(p);
	}

	// detects click of a Player
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();

		if (e.getAction().equals(Action.PHYSICAL))
			return;

		if (p.getItemInHand() == null)
			return;

		if (p.getItemInHand().getItemMeta() == null)
			return;

		if (p.getItemInHand().getItemMeta().getDisplayName() == null)
			return;

		if (p.getItemInHand().getItemMeta().getDisplayName().equals(open.getItemMeta().getDisplayName())) {
			e.setCancelled(true);
			p.openInventory(getInventory());
            Sounds.OPEN.play(p);
		}
	}
}
