package me.yekki.coh.bootstrap.basic;

import com.tangosol.net.NamedCache;
import me.yekki.coh.bootstrap.structures.framework.cluster.ClusterRunner;
import me.yekki.coh.bootstrap.structures.tools.FakeDatabaseCacheStore;
import org.junit.Test;

import java.util.Date;
import java.util.stream.IntStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CacheStoreAsyncTest extends ClusterRunner {

    @Test
    public void demoOfCacheStoreRetryCapabilityWithAsyncCacheStore() throws InterruptedException {
        NamedCache cache = getCache("config/async-cachestore.xml");

        IntStream.range(0, 6).forEach(i->cache.put("key" + i, "value" + i));

        //CacheStore should not have fired yet as async (and has artificial delay - see FakeDatabaseCacheStore.java)
        assertThat(FakeDatabaseCacheStore.keysCalled.size(),is(0));

        System.out.println("Calls to add 6 items returned to client at " + new Date());
        Thread.sleep(4 * 1000);

        //CacheStore should have completed all async calls
        assertThat(FakeDatabaseCacheStore.keysCalled.size(), is(6));
    }
}
