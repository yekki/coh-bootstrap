package me.yekki.coh.bootstrap.basic;

import com.tangosol.net.NamedCache;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.NotFilter;
import me.yekki.coh.bootstrap.structures.framework.PerformanceTimer;
import me.yekki.coh.bootstrap.structures.framework.cluster.ClusterRunner;
import me.yekki.coh.bootstrap.structures.tools.ParallelSumAggregator;
import me.yekki.coh.bootstrap.structures.tools.SumAggregator;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Aggregators extends ClusterRunner {

	@Test
    public void simpleAggregation() throws InterruptedException {

        NotFilter grabEverything = new NotFilter(new EqualsFilter("toString", "it won't be this"));
        NamedCache cache = getBasicCache();

        //add simple data
        IntStream.range(0, 1000).forEach(i->cache.put("key:" + i, 10));

        //create a simple (non-parallel) aggregator
        PerformanceTimer.start();
        Integer value = (Integer) cache.aggregate(grabEverything, new SumAggregator());
        PerformanceTimer.Took regular = PerformanceTimer.end().printMs("EntryAggregator took %");

        //run again with a parallel aggregator
        PerformanceTimer.start();
        Integer value2 = (Integer) cache.aggregate(grabEverything, new ParallelSumAggregator(new SumAggregator()));
        PerformanceTimer.Took parallel = PerformanceTimer.end().printMs("ParallelAggregator took %");

        assertEquals(value.intValue(), 10000);
        assertEquals(value2.intValue(), 10000);
        assertTrue("parallel aggregator should be faster", parallel.ns() < regular.ns());
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        startCoherenceProcess();
        startCoherenceProcess();
    }
}