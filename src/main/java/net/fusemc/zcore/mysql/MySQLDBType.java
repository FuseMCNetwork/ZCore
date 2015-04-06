package net.fusemc.zcore.mysql;

import me.michidk.DKLib.FileHelper;
import net.fusemc.zcore.ZCore;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder;

import java.io.File;

public enum MySQLDBType
{

    STATS("statistics"),
    LANGUAGE("language"),
    PLAYER("player"),
    NETWORK("network"),
    ANTICHEAT("anticheat");

    private MySQLData data;
    private final String name;

    MySQLDBType(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public MySQLData getMySQLData(){
        return data;
    }

    static {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(ZCore.getInstance().getDataFolder(), "mysql.json");
        if (file.exists()) {
            MySQLData[] sqlDataSet = gson.fromJson(FileHelper.stringFromFile(file), MySQLData[].class);
            for (MySQLData data : sqlDataSet) {
                data.setPassword(data.getPassword().replaceAll("<quotation>", "\""));
                for (MySQLDBType type : MySQLDBType.values()) {
                    if (data.getId().equals(type.getName())) {
                        type.data = data;
                    }
                }
            }
        }
    }
}
