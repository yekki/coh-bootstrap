package me.yekki.coh.bootstrap.basic;

import com.tangosol.net.NamedCache;
import me.yekki.coh.bootstrap.structures.framework.PerformanceTimer;
import me.yekki.coh.bootstrap.structures.framework.cluster.ClusterRunner;
import me.yekki.coh.bootstrap.structures.tools.ParallelSumAggregator;
import me.yekki.coh.bootstrap.structures.tools.SumAggregator;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AggregatorsTest extends ClusterRunner {

    @Test
    public void simpleAggregation() throws InterruptedException {

        NamedCache<String, Integer> cache = getBasicCache();

        //add simple data
        IntStream.range(0, 1000).forEach(i -> cache.put(Integer.toString(i),10));

        //create a simple (non-parallel) aggregator
        PerformanceTimer.start();
        Integer value = (Integer) cache.aggregate(new SumAggregator());
        PerformanceTimer.Took regular = PerformanceTimer.end().printMs("EntryAggregator took %");

        //run again with a parallel aggregator
        PerformanceTimer.start();

        // for version 3.x.x
        Integer value2 = (Integer) cache.aggregate(new ParallelSumAggregator(new SumAggregator()));

        // for version 12.2.x
        //Integer value2 = cache.stream().parallel().mapToInt(entry->entry.getValue()).sum();
        PerformanceTimer.Took parallel = PerformanceTimer.end().printMs("ParallelAggregator took %");

        assertEquals(value.intValue(), 10000);
        assertEquals(value2.intValue(), 10000);

        // the stream version is poor performance compared with the old one(ParallelSumAggregator). So, you should add more workload if test failed.
        assertTrue("parallel aggregator should be faster", parallel.ns() < regular.ns());
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        startCoherenceProcess();
        startCoherenceProcess();
    }
}