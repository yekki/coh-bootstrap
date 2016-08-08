package me.yekki.coherence.testing.tests.basic;

import com.benstopford.coherence.bootstrap.structures.framework.cluster.ClusterRunner;
import com.benstopford.coherence.bootstrap.structures.tools.FakeDatabaseCacheStore;
import com.tangosol.net.NamedCache;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static java.lang.System.out;

import java.util.Date;
import java.util.stream.IntStream;

public class CacheStoreAsyncTest extends ClusterRunner{

    @Test
    public void demoOfCacheStoreRetryCapabilityWithAsyncCacheStore() throws InterruptedException {

        NamedCache cache = getCache("config/async-cachestore.xml", "foo");

        IntStream.range(0, 6).forEach(i->cache.put("key" + i, "value" + i));

        assertThat(FakeDatabaseCacheStore.keysCalled.size(), is(0));

        out.println("Calls to add 6 items returned to client at " + new Date());

        Thread.sleep(4*1000);

        assertThat(FakeDatabaseCacheStore.keysCalled.size(), is(6));
    }
}
