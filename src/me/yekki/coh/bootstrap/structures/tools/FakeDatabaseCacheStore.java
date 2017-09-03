package me.yekki.coh.bootstrap.structures.tools;

import com.tangosol.net.cache.AbstractCacheStore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FakeDatabaseCacheStore extends AbstractCacheStore {
    public static final Map<Object, Integer> keysCalled = new HashMap<Object, Integer>();


    public FakeDatabaseCacheStore() {
        System.out.println("constructing FakeDatabaseCacheStore");
    }

    public void store(Object key, Object val) {
        System.out.printf("Trying to write %s to database\n", key);


        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("CacheStore running with key %s at time %s.\n", key, new Date());

        recordCall(key);

    }

    private synchronized void recordCall(Object key) {
        int lastCall = keysCalled.get(key) == null ? 0 : keysCalled.get(key);
        keysCalled.put(key, ++lastCall);
    }


    @Override
    public void storeAll(Map mapEntries) {
        for (Object o : mapEntries.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            store(entry.getKey(), entry.getValue());
        }
    }

    public Object load(Object object) {
        return null;
    }
}
