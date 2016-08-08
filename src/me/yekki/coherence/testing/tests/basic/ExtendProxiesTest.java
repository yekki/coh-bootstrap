package me.yekki.coherence.testing.tests.basic;

import com.benstopford.coherence.bootstrap.structures.framework.cluster.ClusterRunner;
import com.tangosol.net.NamedCache;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ExtendProxiesTest extends ClusterRunner {

    @Test
    public void useExtendProxy() throws IOException, InterruptedException {

        //start data enabled node
        startBasicCacheProcess();

        //start data disabled node as extend proxy
        startDataDisabledExtendProxy();

        //use extend config for this client
        NamedCache cache = getCacheViaExtend();

        //write
        cache.put("Foo", "Bar");

        //read
        assertEquals("Bar", cache.get("Foo"));
    }
}
