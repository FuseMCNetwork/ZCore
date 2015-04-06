package net.fusemc.zcore.projectileAPI;

import net.fusemc.zcore.projectileAPI.projectiles.ItemProjectile;
import net.minecraft.server.v1_7_R4.EntityTypes;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by Marco on 20.07.2014.
 */

public class ProjectileRegistrator {

    protected static Field mapStringToClassField;
    protected static Field mapClassToStringField;
    protected static Field mapClassToIdField;
    protected static Field mapStringToIdField;

    static {
        try {
            mapStringToClassField = EntityTypes.class.getDeclaredField("c");

            mapClassToStringField = EntityTypes.class.getDeclaredField("d");

            mapClassToIdField = EntityTypes.class.getDeclaredField("f");

            mapStringToIdField = EntityTypes.class.getDeclaredField("g");

            mapStringToClassField.setAccessible(true);
            mapClassToStringField.setAccessible(true);

            mapClassToIdField.setAccessible(true);
            mapStringToIdField.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registeProjectiles(){
        registerCustomEntity(ProjectileHorse.class, "test.horse", 100);
        registerCustomEntity(ItemProjectile.class, "test.witherskull", 19);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void registerCustomEntity(Class entityClass, String name, int id) {
        if ((mapStringToClassField == null) || (mapStringToIdField == null) || (mapClassToStringField == null) || (mapClassToIdField == null)) {
            return;
        }
        try {
            Map mapStringToClass = (Map) mapStringToClassField.get(null);
            Map mapStringToId = (Map) mapStringToIdField.get(null);
            Map mapClasstoString = (Map) mapClassToStringField.get(null);
            Map mapClassToId = (Map) mapClassToIdField.get(null);

            mapStringToClass.put(name, entityClass);
            mapStringToId.put(name, Integer.valueOf(id));
            mapClasstoString.put(entityClass, name);
            mapClassToId.put(entityClass, Integer.valueOf(id));

            mapStringToClassField.set(null, mapStringToClass);
            mapStringToIdField.set(null, mapStringToId);
            mapClassToStringField.set(null, mapClasstoString);
            mapClassToIdField.set(null, mapClassToId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
