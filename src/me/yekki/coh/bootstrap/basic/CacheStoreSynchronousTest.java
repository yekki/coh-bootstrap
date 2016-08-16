package me.yekki.coh.bootstrap.basic;

import com.tangosol.net.NamedCache;
import me.yekki.coh.bootstrap.structures.framework.cluster.ClusterRunner;
import me.yekki.coh.bootstrap.structures.tools.TestCacheStore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CacheStoreSynchronousTest extends ClusterRunner {

    @Test
    public void cacheStore() {

        NamedCache cache = getCache("config/synch-cachestore.xml");

        cache.put("Key1", "Value");

        assertTrue(TestCacheStore.WAS_CALLED);
    }
}
