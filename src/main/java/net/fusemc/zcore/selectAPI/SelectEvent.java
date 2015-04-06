package net.fusemc.zcore.selectAPI;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public abstract class SelectEvent extends Event implements Cancellable{

	private boolean cancel = false;
	
	private String title;
	private HumanEntity who;
	protected SelectItem selected;
	protected SelectItem unselected;
	
	public SelectEvent(String title, HumanEntity who, SelectItem selected, SelectItem unselected){
		this.title = title;
		this.who = who;
		this.selected = selected;
		this.unselected = unselected;
	}
	
	public String getSelectionTitle(){
		return title;
	}
	
	public HumanEntity getWhoSelect(){
		return who;
	}
	
	public abstract SelectItem getSelected();
	
	public abstract SelectItem getUnselected();

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
}
