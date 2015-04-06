package net.fusemc.zcore.selectAPI;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class SelectInventory {

	private Inventory inventory;
	private List<SelectItem> selectparts;
	
	public SelectInventory(String title, SelectItem[] selectparts){
		int size = (((selectparts.length -1)/5) * 2) + 1;
		this.inventory = Bukkit.createInventory(null, size*9, title);
		this.selectparts = Arrays.asList(selectparts);
		for(SelectItem select: this.selectparts){
			this.inventory.setItem(getSlot(this.selectparts.indexOf(select), this.selectparts.size()), select.getItemStack());
		}
	}
	
	public Inventory getInventory(){
		return inventory;
	}
	
	public SelectItem[] getSelectParts(){
		return selectparts.toArray(new SelectItem[selectparts.size()]);
	}
	
	public SelectItem getSelectPart(String name){
		for(SelectItem si: selectparts){
			if(si.getItemStack().getDisplayName().equals(name))
				return si;
		}
		return null;
	}
	
	public SelectItem getSelectPart(ItemStack item){
		for(SelectItem si: selectparts){
			if(si.getItemStack().equals(item))
				return si;
		}
		return null;
	}
	
	//slot algorithm
	public int getSlot(int pos, int max){
		int size = ((max-1) / 5) + 1;
	    int per = (max+size-1)/size;
	    int y = (pos/per);
	    int row = pos - (y*per);
	    int x = (5 - per) + (row * 2);
	    return (y*18) + x;
	}
}
