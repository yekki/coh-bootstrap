package me.yekki.coherence.testing.tests.basic;

import com.benstopford.coherence.bootstrap.structures.framework.cluster.ClusterRunner;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import org.junit.Test;

import static org.junit.Assert.fail;

public class TriggersTest extends ClusterRunner {

    @Test
    public void shouldPut() throws InterruptedException {

        NamedCache cache = getCache("config/basic-invocation-service-pof-1.xml", "break-me");

        try {
            cache.put("key", "value");
            fail("trigger was supposed to deny update no break-me cache");
        } catch (Exception expected) {
            System.out.println("Got what we were looking for " + expected.getMessage());
        }
    }
}
