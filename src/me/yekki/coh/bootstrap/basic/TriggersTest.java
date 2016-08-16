package me.yekki.coh.bootstrap.basic;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import me.yekki.coh.bootstrap.structures.framework.cluster.ClusterRunner;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

public class TriggersTest extends ClusterRunner {

    @Test
    public void shouldPut() throws InterruptedException {

        String cacheName = "break-me";
        NamedCache cache = getCache("config/basic-invocation-service-pof-1.xml", cacheName);

        try {
            cache.put("key", "value");
            fail("trigger was supposed to deny update no break-me cache");
        } catch (Exception expected) {
            System.out.println("Got what we were looking for " + expected.getMessage());
        }
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        startCoherenceProcess("config/basic-invocation-service-pof-1.xml");
        startCoherenceProcess("config/basic-invocation-service-pof-1.xml");
        System.out.println(CacheFactory.ensureCluster().getMemberSet());
    }
}
