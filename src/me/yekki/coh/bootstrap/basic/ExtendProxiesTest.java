package me.yekki.coh.bootstrap.basic;

import com.tangosol.net.NamedCache;
import me.yekki.coh.bootstrap.structures.framework.cluster.ClusterRunner;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ExtendProxiesTest extends ClusterRunner {

    @Test
    public void useExtendProxy() throws IOException, InterruptedException {

        NamedCache cache = getExtendCache();

        //write
        cache.put("Foo", "Bar");

        //read
        assertEquals("Bar", cache.get("Foo"));
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        //start data enabled node
        startCoherenceProcess();

        //start data disabled node as extend proxy
        startCoherenceProcess("config/basic-extend-enabled-cache-32001.xml", LOCAL_STORAGE_FALSE);

        assertClusterStarted();
    }
}
