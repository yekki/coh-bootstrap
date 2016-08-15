package me.yekki.coh.bootstrap.basic;

import me.yekki.coh.bootstrap.structures.dataobjects.PofObject;
import me.yekki.coh.bootstrap.structures.framework.cluster.ClusterRunner;
import com.tangosol.io.pof.reflect.SimplePofPath;
import com.tangosol.net.*;
import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.KeyExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.filter.LikeFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class FiltersTest extends ClusterRunner {


    @Test
    public void shouldFilterResults() {

        NamedCache cache = getBasicCache();

        IntStream.range(1, 101).forEach(i->cache.put("Key" + i, "Value" + i));

        Filter filter = new LikeFilter(new KeyExtractor("toString"), "%1%", '/', true);

        Set set = cache.entrySet(filter);
        assertEquals(20, set.size());
    }

    @Test
    public void shouldFilterResultsUsingPofExtractor() {

        NamedCache cache = getCache("config/basic-cache-with-pof.xml");

        IntStream.range(1, 101).forEach(i->cache.put("Key" + i, new PofObject("Value" + i)));

        ValueExtractor pofExtractor = new PofExtractor(null, new SimplePofPath(1));
        Filter filter = new LikeFilter(pofExtractor, "%1%", '/', true);

        Set set = cache.entrySet(filter);
        assertEquals(20, set.size());
    }

    @Test
    public void shouldFilterResultsUsingTwoLevelPofExtractor() {

        NamedCache cache = getCache("config/basic-cache-with-pof.xml");

        IntStream.range(1, 101).forEach(i->{

            PofObject child = new PofObject("Value" + i);
            PofObject parent = new PofObject(child);
            cache.put("Key" + i, parent);
        });

        ValueExtractor pofExtractor = new PofExtractor(null, new SimplePofPath(new int[]{1, 1}));
        Filter filter = new LikeFilter(pofExtractor, "%1%", '/', true);

        Set set = cache.entrySet(filter);
        assertEquals(20, set.size());
    }
}
