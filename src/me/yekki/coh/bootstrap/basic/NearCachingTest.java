package me.yekki.coh.bootstrap.basic;

import com.tangosol.net.NamedCache;
import me.yekki.coh.bootstrap.structures.dataobjects.SizableObjectFactory;
import me.yekki.coh.bootstrap.structures.framework.cluster.ClusterRunner;
import org.junit.Test;

import java.io.IOException;

public class NearCachingTest extends ClusterRunner {

    private static void getFromCacheAndPrintTimings(NamedCache cache) {
        long start;
        start = System.currentTimeMillis();
        cache.get("x");
        System.out.println("took " + (System.currentTimeMillis() - start) + " ms");
    }

    @Test
    public void shouldBeAbleToNearCacheDataForInProcessReRetrieval() throws IOException, InterruptedException {

        startCoherenceProcess("config/basic-extend-enabled-cache-32001.xml");
        assertClusterStarted();
        Thread.sleep(5000);

        NamedCache cacheWithNoNearScheme = getExtendCache();

        cacheWithNoNearScheme.put("x", new SizableObjectFactory().buildObject(1000));

        System.out.println("Get times without near cache:");
        getFromCacheAndPrintTimings(cacheWithNoNearScheme);
        getFromCacheAndPrintTimings(cacheWithNoNearScheme);
        getFromCacheAndPrintTimings(cacheWithNoNearScheme);
        getFromCacheAndPrintTimings(cacheWithNoNearScheme);

        NamedCache cachWithNearScheme = getNearCache();

        System.out.println("Get times with near cache:");
        getFromCacheAndPrintTimings(cachWithNearScheme);
        getFromCacheAndPrintTimings(cachWithNearScheme);
        getFromCacheAndPrintTimings(cachWithNearScheme);
        getFromCacheAndPrintTimings(cachWithNearScheme);
    }
}
