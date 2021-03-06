package me.yekki.coh.bootstrap.morecomplex;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.DistributedCacheService;
import com.tangosol.net.InvocationService;
import com.tangosol.net.NamedCache;
import me.yekki.coh.bootstrap.structures.framework.cluster.ClusterRunner;
import me.yekki.coh.bootstrap.structures.tools.PutAllWithErrorReporting;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PutAllThatReportsIndividualExceptions extends ClusterRunner {

    @Test
    public void shouldPut() throws InterruptedException {
        startCoherenceProcess("config/basic-invocation-service-pof-1.xml");
        startCoherenceProcess("config/basic-invocation-service-pof-1.xml");

        String cacheName = "regular-cache";
        String configPath = "config/basic-invocation-service-pof-1.xml";

        NamedCache cache = getCache(configPath, cacheName);

        InvocationService service = (InvocationService) CacheFactory.getCacheFactoryBuilder()
                .getConfigurableCacheFactory(configPath, classLoader)
                .ensureService("MyInvocationService1");

        TreeMap hashMap = new TreeMap();
        hashMap.put(1, 2);
        hashMap.put(2, 3);
        hashMap.put(3, 4);
        hashMap.put(4, 5);

        PutAllWithErrorReporting invoker = new PutAllWithErrorReporting(
                service,
                (DistributedCacheService) cache.getCacheService(),
                cache.getCacheName(),
                configPath
        );
        invoker.putAll(hashMap);

        Thread.sleep(1000);
        assertEquals(4, cache.size());
        assertEquals(2, cache.get(1));
        assertEquals(3, cache.get(2));
        assertEquals(4, cache.get(3));
        assertEquals(5, cache.get(4));
    }

    @Test
    public void shouldReportFailures() {
        startCoherenceProcess("config/basic-invocation-service-pof-1.xml");
        startCoherenceProcess("config/basic-invocation-service-pof-1.xml");

        String cacheName = "break-me";
        String configPath = "config/basic-invocation-service-pof-1.xml";
        NamedCache cache = CacheFactory.getCacheFactoryBuilder()
                .getConfigurableCacheFactory(configPath, classLoader)
                .ensureCache(cacheName, classLoader);

        InvocationService service = (InvocationService)CacheFactory.getCacheFactoryBuilder().getConfigurableCacheFactory(configPath, getClass().getClassLoader()).ensureService("MyInvocationService1");

        Map entries = new TreeMap();
        entries.put(1, 2);
        entries.put(2, 3);
        entries.put(3, 4);
        entries.put(4, 5);

        PutAllWithErrorReporting invoker = new PutAllWithErrorReporting(
                service,
                (DistributedCacheService) cache.getCacheService(),
                cache.getCacheName(),
                configPath
        );

        Map<Object, Throwable> keyToThrowableMap = invoker.putAll(entries);

        System.out.println("test ran and return-result size is " + keyToThrowableMap.size());

        assertEquals(4, keyToThrowableMap.size());
        assertTrue(keyToThrowableMap.get(1).getMessage().contains("Forced Error"));
        assertTrue(keyToThrowableMap.get(2).getMessage().contains("Forced Error"));
        assertTrue(keyToThrowableMap.get(3).getMessage().contains("Forced Error"));
        assertTrue(keyToThrowableMap.get(4).getMessage().contains("Forced Error"));
    }
}
