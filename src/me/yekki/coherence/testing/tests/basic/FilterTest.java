package me.yekki.coherence.testing.tests.basic;

import com.benstopford.coherence.bootstrap.structures.dataobjects.PofObject;
import com.benstopford.coherence.bootstrap.structures.framework.cluster.ClusterRunner;
import com.tangosol.io.pof.reflect.SimplePofPath;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.KeyExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.filter.LikeFilter;
import org.junit.Test;

import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class FilterTest extends ClusterRunner {

    @Test
    public void shouldFilterResults() {

        NamedCache cache = getBasicCache();

        IntStream.range(0, 100).forEach(i->cache.put("key" + i, "value" + i));

        Filter filter = new LikeFilter( new KeyExtractor("toString"), "%1%", '/', true);

        Set<?> set = cache.entrySet(filter);

        assertEquals(19, set.size());
    }

    @Test
    public void shouldFilterResultsUsingPofExtractor() {

        NamedCache cache = getCache("config/basic-cache-with-pof.xml");

        IntStream.range(0, 100).forEach(i->cache.put("key" + i, new PofObject("value" + i)));

        ValueExtractor pofExtractor = new PofExtractor(null, new SimplePofPath(1));

        Filter filter = new LikeFilter(pofExtractor, "%1%", '/', true);

        Set set = cache.entrySet(filter);

        assertEquals(19, set.size());
    }

    @Test
    public void shouldFilterResultsUsingTwoLevelPofExtractor() {

        NamedCache cache = getCache("config/basic-cache-with-pof.xml");

        IntStream.range(0, 100).forEach(i-> {
            PofObject child = new PofObject("Value" + i);
            PofObject parent = new PofObject(child);
            cache.put("Key" + i, parent); }
        );

        ValueExtractor pofExtractor = new PofExtractor(null, new SimplePofPath(new int[]{1, 1}));
        Filter filter = new LikeFilter(pofExtractor, "%1%", '/', true);

        Set set = cache.entrySet(filter);
        assertEquals(19, set.size());
    }
}
