package me.yekki.coherence.testing.tests.basic;

import com.benstopford.coherence.bootstrap.structures.framework.cluster.ClusterRunner;
import com.benstopford.coherence.bootstrap.structures.tools.TestCacheStore;
import com.tangosol.net.NamedCache;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class CacheStoreSynchronousTest extends ClusterRunner{

    @Test
    public void cacheStore() {
        NamedCache cache = getCache("config/synch-cachestore.xml", "foo");
        cache.put("key", "value");
        assertTrue(TestCacheStore.WAS_CALLED);
    }
}
