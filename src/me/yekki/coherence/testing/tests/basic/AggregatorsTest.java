package me.yekki.coherence.testing.tests.basic;

import com.benstopford.coherence.bootstrap.structures.framework.PerformanceTimer;
import com.benstopford.coherence.bootstrap.structures.framework.cluster.ClusterRunner;
import com.benstopford.coherence.bootstrap.structures.tools.ParallelSumAggregator;
import com.benstopford.coherence.bootstrap.structures.tools.SumAggregator;
import com.tangosol.net.NamedCache;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.NotFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Serializable;
import java.util.stream.IntStream;

import static com.benstopford.coherence.bootstrap.structures.framework.PerformanceTimer.end;
import static com.benstopford.coherence.bootstrap.structures.framework.PerformanceTimer.start;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class AggregatorsTest extends ClusterRunner {

    @Test
    public void simpleAggregation() throws InterruptedException, IOException {

        startBasicCacheProcess();
        startBasicCacheProcess();

        NotFilter grabEverything = new NotFilter(new EqualsFilter("toString", "it won't be this"));
        NamedCache cache = getBasicCache();

        //add simple data
        IntStream.range(0, 1000).forEach(i->cache.put("key:" + i, 10));

        //create a simple (non-parallel) aggregator
        start();
        Integer value = (Integer) cache.aggregate(grabEverything, new SumAggregator());
        PerformanceTimer.Took regular = end().printMs("EntryAggregator took %");


        //run again with a parallel aggregator
        start();
        Integer value2 = (Integer) cache.aggregate(grabEverything, new ParallelSumAggregator(new SumAggregator()));
        PerformanceTimer.Took parallel = end().printMs("ParallelAggregator took %");

        assertEquals(value.intValue(), 10000);
        assertEquals(value2.intValue(), 10000);
        assertTrue("parallel aggregator should be faster", parallel.ns() < regular.ns());
    }
}