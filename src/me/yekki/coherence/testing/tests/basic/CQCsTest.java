package me.yekki.coherence.testing.tests.basic;

import com.benstopford.coherence.bootstrap.structures.dataobjects.SizableObjectFactory;
import com.benstopford.coherence.bootstrap.structures.framework.cluster.ClusterRunner;
import com.tangosol.net.NamedCache;
import com.tangosol.net.cache.ContinuousQueryCache;
import com.tangosol.util.Filter;
import com.tangosol.util.extractor.KeyExtractor;
import com.tangosol.util.filter.LikeFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class CQCsTest extends ClusterRunner {

    @Test
    public void cqcShouldLoadSubsetToLocalProcess() throws InterruptedException, IOException {
        startCoherenceProcess("config/basic-extend-enabled-cache-32001.xml");
        NamedCache backingCache = getCacheViaExtend();
        IntStream.range(0, 10).forEach(i-> backingCache.put("key" + i, new SizableObjectFactory().buildObject(1000)));
        Filter filter = new LikeFilter(new KeyExtractor("toString"), "%1%", '/', true);
        ContinuousQueryCache cqc = new ContinuousQueryCache(backingCache, filter);
        assertEquals(1, cqc.size());
        assertEquals(10, backingCache.size());
    }
}
