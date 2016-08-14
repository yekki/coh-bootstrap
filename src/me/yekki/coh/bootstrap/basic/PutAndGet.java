package me.yekki.coh.bootstrap.basic;

import me.yekki.coh.bootstrap.structures.framework.cluster.ClusterRunner;
import com.tangosol.net.NamedCache;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * BTS, 07-Dec-2007
 */
public class PutAndGet extends ClusterRunner {

    @Test
    public void putAndGetFromCache() {
        NamedCache cache = getCache("foo");

        cache.put("Key", "Value");

        assertEquals("Value", cache.get("Key"));
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
