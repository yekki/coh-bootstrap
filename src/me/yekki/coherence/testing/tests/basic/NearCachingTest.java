package me.yekki.coherence.testing.tests.basic;

import com.benstopford.coherence.bootstrap.structures.dataobjects.SizableObjectFactory;
import com.benstopford.coherence.bootstrap.structures.framework.cluster.ClusterRunner;
import com.tangosol.net.NamedCache;
import org.junit.Test;

import java.io.IOException;

public class NearCachingTest extends ClusterRunner {

    @Test
    public void shouldBeAbleToNearCacheDataForInProcessReRetrieval() throws IOException, InterruptedException {
        startCoherenceProcess("config/basic-extend-enabled-cache-32001.xml");
        Thread.sleep(5000);

        NamedCache cacheWithNoNearScheme = getCacheViaExtend();

        cacheWithNoNearScheme.put("x", new SizableObjectFactory().buildObject(1000));

        System.out.println("Get times without near cache:");
        getFromCacheAndPrintTimings(cacheWithNoNearScheme);
        getFromCacheAndPrintTimings(cacheWithNoNearScheme);
        getFromCacheAndPrintTimings(cacheWithNoNearScheme);
        getFromCacheAndPrintTimings(cacheWithNoNearScheme);

        NamedCache cachWithNearScheme = getCache("config/extend-client-with-near-cache.xml", "foo");

        System.out.println("Get times with near cache:");
        getFromCacheAndPrintTimings(cachWithNearScheme);
        getFromCacheAndPrintTimings(cachWithNearScheme);
        getFromCacheAndPrintTimings(cachWithNearScheme);
        getFromCacheAndPrintTimings(cachWithNearScheme);
    }

    private static void getFromCacheAndPrintTimings(NamedCache cache) {
        long start;
        start = System.currentTimeMillis();
        cache.get("x");
        System.out.println("took " + (System.currentTimeMillis() - start) + " ms");
    }
}