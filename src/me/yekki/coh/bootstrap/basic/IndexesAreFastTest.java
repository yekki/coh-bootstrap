package me.yekki.coh.bootstrap.basic;

import me.yekki.coh.bootstrap.structures.dataobjects.PoJo;
import me.yekki.coh.bootstrap.structures.framework.cluster.ClusterRunner;
import com.tangosol.net.NamedCache;
import com.tangosol.util.extractor.ReflectionExtractor;
import com.tangosol.util.filter.EqualsFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.System.*;

public final class IndexesAreFastTest extends ClusterRunner {
    private final List<Object> valuesSentToClient1 = new ArrayList<Object>();
    private final List<Object> valuesSentToClient2 = new ArrayList<Object>();

    @Test
    public void isUsingAnEntrySetWithAnIndexFasterAndDoesItScaleBetterAsTheClusterSizeIncreases() throws IOException, InterruptedException {

        startCoherenceProcess();
        startCoherenceProcess();
        startCoherenceProcess();
        startCoherenceProcess();
        startCoherenceProcess();

        out.println("\n\n******* Timings without index *******");
        NamedCache cache = getBasicCache();

        addValuesToCache(cache, 10);
        getFromCacheAndPrintTimings(cache, 100);

        addValuesToCache(cache, 100);
        getFromCacheAndPrintTimings(cache, 100);

        addValuesToCache(cache, 1000);
        getFromCacheAndPrintTimings(cache, 100);

        addValuesToCache(cache, 10000);
        getFromCacheAndPrintTimings(cache, 100);

        cache.clear();
        cache.addIndex(new ReflectionExtractor("getData"), false, null);

        out.println("\n\n******* Timings with index *******");
        addValuesToCache(cache, 10);
        getFromCacheAndPrintTimings(cache, 100);

        addValuesToCache(cache, 100);
        getFromCacheAndPrintTimings(cache, 100);

        addValuesToCache(cache, 1000);
        getFromCacheAndPrintTimings(cache, 100);

        addValuesToCache(cache, 10000);
        getFromCacheAndPrintTimings(cache, 100);

        out.println("\n\n");
    }

    private static void getFromCacheAndPrintTimings(NamedCache cache, int number) {
        long start = nanoTime();
        for (int i = 0; i < number; i++) {
            cache.entrySet(new EqualsFilter("getData", i));
        }
        long took = nanoTime() - start;

        out.printf("Selecting from %,d objects took %,dus\n", cache.size(), took / 1000);
    }

    private void addValuesToCache(NamedCache cache, int numberToAdd) {
        for (int i = 0; i < numberToAdd; i++) {
            PoJo valueObject = new PoJo(i);
            cache.put(i, valueObject);
        }
    }

    @Before
    public void setUp() throws Exception {
        valuesSentToClient1.clear();
        valuesSentToClient2.clear();
        super.setUp();
    }
}
