package me.yekki.coh.bootstrap.basic;

import com.tangosol.net.NamedCache;
import me.yekki.coh.bootstrap.structures.dataobjects.LoggingPofObject;
import me.yekki.coh.bootstrap.structures.framework.cluster.ClusterRunner;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;


public class POFTest extends ClusterRunner {

    @Test
    public void putAndGetPofEncodedObject() throws IOException {

        NamedCache cache = getBasicPofCache();

        cache.put("key", new LoggingPofObject("some data"));

        LoggingPofObject object = (LoggingPofObject) cache.get("key");

        assertEquals("some data", object.getData());
    }
}
