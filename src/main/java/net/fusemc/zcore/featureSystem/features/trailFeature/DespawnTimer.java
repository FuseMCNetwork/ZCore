package net.fusemc.zcore.featureSystem.features.trailFeature;

import me.johnking.jlib.JLib;
import me.johnking.jlib.protocol.PacketType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Niklas on 22.09.2014.
 */
public class DespawnTimer implements Runnable {

    private LinkedList<DespawnID> items;
    //private Map<Long, Integer> items;
    private EntityIDPool pool;
    private final int delay;

    public DespawnTimer(int delay, EntityIDPool pool) {
        this.delay = delay;
        this.pool = pool;
        //this.items = new HashMap<>();
        this.items = new LinkedList<>();
    }

    public void addEntity(int entityID) {
        //items.put(System.nanoTime(), entityID);
        items.add(new DespawnID(entityID, delay));
    }

    @Override
    public void run() {
        List<DespawnID> removes = new ArrayList<>();
        for(DespawnID id : items) {
            int i = id.deIncDelay();
            if(i == 0) {
                removes.add(id);
            }
        }
        int[] entityIDs = new int[removes.size()];
        for(int i = 0; i < removes.size(); i++) {
            DespawnID id = removes.get(i);
            items.remove(id);
            pool.releaseInteger(id.getEntityID());
            entityIDs[i] = id.getEntityID();
        }
        JLib.getProtocolManager().sendPacket(getDestroyPacket(entityIDs));
        /*
        List<Integer> ids = new ArrayList<>();
        Iterator<Map.Entry<Long, Integer>> iterator = items.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Integer> entry = iterator.next();
            if (entry.getKey() + delay*1000000000 <= System.nanoTime()) {
                ids.add(entry.getValue());
                iterator.remove();
            }
        }
        int[] entityIDs = new int[ids.size()];
        for(int i = 0; i < ids.size(); i++) {
            int entityID = ids.get(i);
            entityIDs[i] = entityID;
            pool.releaseInteger(entityID);
        }
        JLib.getProtocolManager().sendPacket(getDestroyPacket(entityIDs));
        */
    }

    public static Object getDestroyPacket(int... entity) {
        try {
            Object packet = PacketType.Server.ENTITY_DESTROY.getPacketClass().getConstructor(int[].class).newInstance(entity);
            return packet;
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
