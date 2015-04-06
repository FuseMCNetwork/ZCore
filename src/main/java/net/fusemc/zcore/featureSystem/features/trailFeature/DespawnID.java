package net.fusemc.zcore.featureSystem.features.trailFeature;

/**
 * Created by Marco on 10.10.2014.
 */
public class DespawnID {

    public int entityID;
    public int delay;

    public DespawnID (int entityID, int delay) {
        this.entityID = entityID;
        this.delay = delay;
    }

    public int deIncDelay() {
        return (delay--);
    }

    public int getEntityID() {
        return entityID;
    }
}
