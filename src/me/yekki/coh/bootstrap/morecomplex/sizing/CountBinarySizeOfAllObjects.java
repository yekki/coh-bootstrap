package me.yekki.coh.bootstrap.morecomplex.sizing;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import me.yekki.coh.bootstrap.structures.dataobjects.PoJo;
import me.yekki.coh.bootstrap.structures.dataobjects.PofObject;
import me.yekki.coh.bootstrap.structures.framework.cluster.ClusterRunner;
import me.yekki.coh.bootstrap.structures.tools.jmx.BinaryCacheSizeCounter;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CountBinarySizeOfAllObjects extends ClusterRunner {

    @Test
    public void shouldCountSizeOfBinaryCachesAccuratelyInProcess() throws Exception {
        int jmxPort = 10001;

        //Given two caches, clustered in this jvm
        startLocalJMXServer(jmxPort);
        NamedCache foo = getCache("foo");
        NamedCache bar = getCache("bar");
        assertThat(CacheFactory.getCluster().getMemberSet().size(), is(1));

        //When we add 10MB to each cache
        addValuesToCache(foo, 1024, new PofObject(new byte[10 * 1024]));//10MB
        addValuesToCache(bar, 1024, new PofObject(new byte[10 * 1024]));//10MB

        //call the utility
        long measured = new BinaryCacheSizeCounter().sumClusterStorageSize(jmxPort);

        //Then the JMX Units Sizer utility should be within 5%. Should be within 10% of memory consumption
        assertWithinTolerance(20 * MB, measured, 0.05);
    }

    @Test
    public void shouldCountSizeOfBinaryCachesAccuratelyAcrossMultipleProcesses() throws Exception {
        int port = 40001;

        //Given a cluster in three JVMs
        startCoherenceProcessWithJMX(port);

        startCoherenceProcess();
        startDataDisabledExtendProxy();

        //with two caches
        NamedCache foo = getCache("config/extend-client-32001.xml", "foo");
        NamedCache bar = getCache("config/extend-client-32001.xml", "bar");

        //When we add 20MB of data
        addValuesToCache(foo, 10 * 1024, new PoJo(new byte[1024])); //10MB
        addValuesToCache(bar, 10 * 1024, new PoJo(new byte[1024])); //10MB

        //call the utility
        long size = new BinaryCacheSizeCounter().sumClusterStorageSize(port);

        //Then our sizing utility should get the size within 10%
        double fudgeFactorForObjectWrappers = 1.3;
        assertWithinTolerance((long)(20 * MB * fudgeFactorForObjectWrappers), size, 0.10);
    }

    private NamedCache getCacheByName(String name) {
        NamedCache cache = CacheFactory.getCacheFactoryBuilder()
                .getConfigurableCacheFactory("config/basic-cache-with-pof.xml", this.getClass().getClassLoader())
                .ensureCache(name, this.getClass().getClassLoader());
        return cache;
    }

    private void addValuesToCache(NamedCache cache, int numberToAdd, Object o) {
        Map all = new HashMap();
        for (int i = 0; i < numberToAdd; i++) {
            all.put("Key" + i, o);
        }
        cache.putAll(all);
    }
}
