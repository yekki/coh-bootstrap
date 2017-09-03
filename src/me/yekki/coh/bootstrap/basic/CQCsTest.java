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
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class CQCsTest extends ClusterRunner {

    @Test
    public void cqcShouldLoadSubsetToLocalProcess() throws IOException, InterruptedException {

        NamedCache backingCache = getExtendCache();

        IntStream.range(1, 11).forEach(i -> backingCache.put("Key" + i, new SizableObjectFactory().buildObject(1000)));

        Filter filter = new LikeFilter(new KeyExtractor("toString"), "%1%", '/', true);

        ContinuousQueryCache cqc = new ContinuousQueryCache(backingCache, filter);

        assertEquals(2, cqc.size());
        assertEquals(10, backingCache.size());
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        startCoherenceProcess("config/basic-extend-enabled-cache-32001.xml");
    }
}
