package me.yekki.coh.bootstrap.basic;

import com.tangosol.net.NamedCache;
import com.tangosol.util.AbstractMapListener;
import me.yekki.coh.bootstrap.structures.framework.cluster.ClusterRunner;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MapListenersTest extends ClusterRunner {
    int notificationCount = 0;

    @Test
    public void mapListenerShouldBeCalledWhenEntryInserted() throws IOException, InterruptedException {

        NamedCache cache = getExtendCache();

        cache.addMapListener(new AbstractMapListener() {
            public void entryInserted(com.tangosol.util.MapEvent mapEvent) {
                System.out.println("Entry was inserted " + mapEvent.getNewValue());
                notificationCount++;
            }

            public void entryUpdated(com.tangosol.util.MapEvent mapEvent) {
                System.out.println("Entry was udpated to " + mapEvent.getNewValue());
                notificationCount++;
            }
        });

        cache.put("Foo", "1");
        cache.put("Foo", "2");

        Thread.sleep(2000); //it's async

        assertEquals(2, notificationCount);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        startCoherenceProcess();
        startDataDisabledExtendProxy();
        assertClusterStarted();
    }


}
