package net.fusemc.zcore.featureSystem.features.trailFeature;

import net.fusemc.zcore.util.IntegerPool;
import net.fusemc.zcore.util.ReflectionUtil;

import java.lang.reflect.Field;

/**
 * Created by Marco on 28.09.2014.
 */
public class EntityIDPool extends IntegerPool {

    private static final Class<?> classEntity = ReflectionUtil.getMinecraftClass("Entity");
    private static final Field entityCount = ReflectionUtil.getField(classEntity, "entityCount");

    @Override
    protected int generateInteger() {
        try {
            int entityId = entityCount.getInt(null);
            entityCount.setInt(null, entityId + 1);
            return entityId;
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
