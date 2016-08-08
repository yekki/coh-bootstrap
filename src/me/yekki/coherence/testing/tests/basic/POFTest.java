package me.yekki.coherence.testing.tests.basic;

import com.benstopford.coherence.bootstrap.structures.dataobjects.LoggingPofObject;
import com.benstopford.coherence.bootstrap.structures.framework.cluster.ClusterRunner;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class POFTest extends ClusterRunner{

    @Test
    public void putAndGetPofEncodedObject() {

        NamedCache cache = getCache("config/basic-cache-with-pof.xml", "stuff");

        cache.put("key", new LoggingPofObject("some data"));

        LoggingPofObject object = (LoggingPofObject) cache.get("key");

        assertEquals("some data", object.getData());
    }
}
