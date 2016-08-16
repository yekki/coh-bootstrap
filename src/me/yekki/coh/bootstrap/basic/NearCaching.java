package me.yekki.coh.bootstrap.basic;

import me.yekki.coh.bootstrap.structures.dataobjects.SizableObjectFactory;
import me.yekki.coh.bootstrap.structures.framework.cluster.ClusterRunner;
import com.tangosol.net.NamedCache;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class NearCaching extends ClusterRunner {

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
