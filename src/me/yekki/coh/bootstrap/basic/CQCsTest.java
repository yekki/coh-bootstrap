package me.yekki.coh.bootstrap.basic;

import com.tangosol.net.NamedCache;
import com.tangosol.net.cache.ContinuousQueryCache;
import com.tangosol.util.Filter;
import com.tangosol.util.extractor.KeyExtractor;
import com.tangosol.util.filter.LikeFilter;
import me.yekki.coh.bootstrap.structures.dataobjects.SizableObjectFactory;
import me.yekki.coh.bootstrap.structures.framework.cluster.ClusterRunner;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CQCsTest extends ClusterRunner {

    @Test
    public void cqcShouldLoadSubsetToLocalProcess() throws IOException, InterruptedException {

        NamedCache backingCache = getExtendCache();

        addValuesToCache(backingCache, 10);

        Filter filter = new LikeFilter(new KeyExtractor("toString"), "%1%", '/', true);

        ContinuousQueryCache cqc = new ContinuousQueryCache(backingCache, filter);

        assertEquals(2, cqc.size());
        assertEquals(10, backingCache.size());
    }

    private void addValuesToCache(NamedCache cache, int numberToAdd) {
        for (int i = 1; i <= numberToAdd; i++) {
            cache.put("Key" + i, new SizableObjectFactory().buildObject(1000));
        }
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        startCoherenceProcess("config/basic-extend-enabled-cache-32001.xml");
        assertClusterStarted();
    }
}
