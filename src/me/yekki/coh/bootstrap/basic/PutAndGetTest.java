package me.yekki.coh.bootstrap.basic;

import com.tangosol.net.NamedCache;
import me.yekki.coh.bootstrap.structures.framework.cluster.ClusterRunner;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PutAndGetTest extends ClusterRunner {

    @Test
    public void putAndGetFromCache() {

        NamedCache cache = getBasicCache();

        cache.put("Key", "Value");

        assertEquals("Value", cache.get("Key"));
    }
}
