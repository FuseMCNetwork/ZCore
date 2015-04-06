package net.fusemc.zcore.util;

import java.util.LinkedList;

/**
 * Created by Marco on 28.09.2014.
 */
public abstract class IntegerPool {

    private final LinkedList<Integer> pool = new LinkedList<Integer>();

    protected abstract int generateInteger();

    public int getInteger() {
        if (pool.size() == 0) {
            return generateInteger();
        }
        return pool.removeFirst();
    }

    public void releaseInteger(int i) {
        pool.add(i);
    }
}
