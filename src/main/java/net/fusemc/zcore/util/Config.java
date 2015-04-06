package net.fusemc.zcore.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Config{
	
	private JavaPlugin plugin;

	private FileConfiguration fileConfiguration;
	private String fileName;
	 
    public Config(String fileName, JavaPlugin plugin) {
    	this.plugin = plugin;
    	this.fileName = fileName;
    	if (!this.fileName.endsWith(".yml"))
    		this.fileName = fileName + ".yml";
    	this.fileConfiguration = this.reload();
    }
    
    public FileConfiguration getConfig(){
    	return fileConfiguration;
    }
    
    public FileConfiguration reload() {
    	File file = new File(plugin.getDataFolder(),fileName);
    	if (!file.exists()) {
    		plugin.getDataFolder().mkdir();
    		try {
    			file.createNewFile();
    			}
    		catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
        return YamlConfiguration.loadConfiguration(file); // file found, load into config and return it.
    }

    public void save() {
    	File file = new File(plugin.getDataFolder(),fileName);
    	try {
    		fileConfiguration.save(file);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
}
