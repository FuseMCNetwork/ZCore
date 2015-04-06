package net.fusemc.zcore.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author johnking
 */
public class NamedItem extends ItemStack{

	protected String name;
    protected String prefix = "";
    protected String suffix = "";
	
	public NamedItem(Material type, String name){
		super(type);
		setName(name);
	}
	
	public NamedItem(Material type, int amount, String name){
		super(type, amount);
		setName(name);
	}
	
	public NamedItem(Material type, int amount, short damage, String name){
		super(type, amount, damage);
		setName(name);
	}
	
	@Deprecated
	public NamedItem(Material type, int amount, short damage, Byte data, String name){
		super(type, amount, damage, data);
		setName(name);
	}
	
	private void set(){
		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName(this.prefix + this.name + this.suffix);
		this.setItemMeta(meta);
	}
	
	public NamedItem setName(String name){
		this.name = name;
		set();
		return this;
	}
	
	public NamedItem setPrefix(String prefix){
		this.prefix = prefix;
		set();
		return this;
	}
	
	public NamedItem setSuffix(String suffix){
		this.suffix = suffix;
		set();
		return this;
	}
	
	public String getDisplayName(){
		return this.getItemMeta().getDisplayName();
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getPrefix(){
		return this.prefix;
	}
	
	public String getSuffix(){
		return this.suffix;
	}
	
	public NamedItem setLore(int line, String text){
        ItemMeta meta = this.getItemMeta();

        if (meta.getLore() == null)
            meta.setLore(new ArrayList<String>());
        
        List<String> list = meta.getLore();
        for(int i = list.size(); i <= line; i++)
            list.add("");
        list.set(line, text);
        meta.setLore(list);

        this.setItemMeta(meta);
        return this;
	}
	
	public List<String> getLore(){
		return this.getItemMeta().getLore();
	}
}
