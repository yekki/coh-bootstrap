package com.benstopford.coherence.bootstrap.structures.framework.cluster;

import com.benstopford.coherence.bootstrap.structures.dataobjects.SizableObject;
import com.benstopford.coherence.bootstrap.structures.dataobjects.SizableObjectFactory;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.CacheService;
import com.tangosol.net.NamedCache;
import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Properties;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * BTS, 07-Dec-2007
 * <p/>
 * EDIT 2014: This test framework predates the work done by the Coherence team to internalise this kind of testing
 * into a single JVM and multiple classloaders.Credit for this excellent pattern goes to Andrew (Orange Pheonix) Wilson
 * <p/>
 * I strongly suggest that, in your own code, you use the LittleGrid or the Oracle Tools runner. Both of these are much better
 * than this slightly antiquated tool I use which simply predates them by a few years. See JK's post here:
 * http://thegridman.com/coherence/oracle-coherence-testing-with-oracle-tools/
 */
public abstract class ClusterRunner extends TestUtils {
    protected ClassLoader classLoader = getClass().getClassLoader();
    public static final String LOCAL_STORAGE_FALSE = "-Dtangosol.coherence.distributed.localstorage=false";

    private ProcessExecutor executor = new ProcessExecutor(defaultProperties());
    private HashSet<CacheService> services = new HashSet<>();
    private HashSet<NamedCache> caches = new HashSet<>();


    private static Properties defaultProperties() {
        Properties properties = new Properties();
        properties.put("tangosol.coherence.clusteraddress", "239.255.12.30");
        properties.put("tangosol.coherence.clusterport", "1234");
        properties.put("tangosol.coherence.cluster", "benstopford.coherence.bootstrap");
        properties.put("tangosol.coherence.cacheconfig", "config/basic-cache.xml");
        properties.put("tangosol.coherence.log.level", "9");
        properties.put("tangosol.coherence.ttl", "0");
        properties.put("tangosol.coherence.management", "all");
        properties.put("tangosol.coherence.management.remote", "true");
        return properties;
    }

    public static boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (null != files) {
                for ( File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    }
                    else {
                        file.delete();
                    }
                }
            }
        }
        return (directory.delete());
    }

    @Before
    public void setUp() throws Exception {
        executor.countOfStartedNodes = 0;
        skipWitForProcessTearDown = false;
        deleteContentsOfLogDir();
        ProcessLogger.switchStdErrToFile();
        new PersistentPortTracker().incrementExtendPort();
        System.getProperties().putAll(defaultProperties());
    }

    @After
    public void tearDown() throws Exception {
        shutdownServices();
        releaseLocalCacheResources();
        CacheFactory.getCluster().shutdown();
        CacheFactory.shutdown();
        executor.killOpenCoherenceProcesses();
        if (!skipWitForProcessTearDown)
            waitForAllKilledMembersToTimeOut();
    }

    private void deleteContentsOfLogDir() {
        File logDir = new File("log");
        if (!logDir.exists()) {
            logDir.mkdir();
        }

        File[] logs = logDir.listFiles();
        if (logs != null) {
            for (File log : logs) {
                log.delete();
            }
        }
    }

    private void waitForAllKilledMembersToTimeOut() throws InterruptedException {
        while (CacheFactory.ensureCluster().getMemberSet().size() > 1) {
            System.out.println("Waiting for " + CacheFactory.ensureCluster().getMemberSet().size() + " active cluster members to be deregistered");
            System.out.println(CacheFactory.ensureCluster().getMemberSet());
            Thread.sleep(1000);
        }
    }

    private void releaseLocalCacheResources() {

        caches.forEach(c->c.release());

        caches.clear();
    }

    private void shutdownServices() {

        services.forEach(s->s.shutdown());

        services.clear();
    }

    protected NamedCache getBasicCache(String name) {
        return getCache("config/basic-cache.xml", name);
    }

    protected NamedCache getBasicCache() {
        return getCache("config/basic-cache.xml", "foo");
    }

    protected void startOutOfProcessWithJMX(String config) {
        executor.startOutOfProcess(config, getJMXProperties(3000));
    }

    private static String getJMXProperties(int jmx_port) {
        return getSunJMXProperties(jmx_port);
    }

    private static String getSunJMXProperties(int jmx_port) {
        return "" +
                "-Dcom.sun.management.jmxremote.ssl=false " +
                "-Dcom.sun.management.jmxremote " +
                "-Dcom.sun.management.jmxremote.authenticate=false " +
                "-Dcom.sun.management.jmxremote.port=" + jmx_port + " ";
    }


    private static int prefix = 0;

    protected void addData(NamedCache<String, SizableObject> cache, int mbToAdd) {
        prefix++;

        IntStream.range(0, mbToAdd).forEach(i-> {
            SizableObject sizableObject = new SizableObjectFactory().buildObject(1000);
            cache.put(prefix + Integer.toString(i), sizableObject);
        });

        System.out.println("added " + mbToAdd + "MB");
    }

    protected NamedCache getRemoteCache() {
        return getRemoteCache("foo");
    }

    protected NamedCache getRemoteCache(String name) {
        return CacheFactory.getCacheFactoryBuilder()
                .getConfigurableCacheFactory("config/extend-client-32001.xml", classLoader)
                .ensureCache(name, classLoader);
    }

    protected void startBasicCacheProcess() throws IOException, InterruptedException {
        executor.startOutOfProcess("config/basic-cache.xml", "");
    }

    protected void startBasicCacheProcessWithJMX(int port) throws IOException, InterruptedException {
        executor.startOutOfProcess("config/basic-cache.xml", getJMXProperties(port));
    }

    protected void startBasicCacheProcessWithJMX(String config, int port) throws IOException, InterruptedException {
        executor.startOutOfProcess(config, getJMXProperties(port));
    }

    protected void startDataDisabledExtendProxy() throws IOException, InterruptedException {
        executor.startOutOfProcess("config/basic-extend-enabled-cache-32001.xml", LOCAL_STORAGE_FALSE);
    }

    protected Process startCoherenceProcess(String config) {
        return executor.startOutOfProcess(config, "");
    }

    protected int startCoherenceProcessWithJmx(String config) {
        int port = 4100;
        executor.startOutOfProcess(config, getJMXProperties(port));
        return port;
    }

    protected Process startCoherenceProcess(String config, String properties) {
        return executor.startOutOfProcess(config, properties);
    }

    protected NamedCache getCacheViaExtend() {
        return getCache("config/extend-client-32001.xml", "foo");
    }

    protected NamedCache getCache(String configLocation) {
        return getCache(configLocation, "foo");
    }

    protected NamedCache getCache(String configLocation, String cacheName) {
        NamedCache cache = CacheFactory.getCacheFactoryBuilder()
                .getConfigurableCacheFactory(configLocation, classLoader)
                .ensureCache(cacheName, classLoader);
        addToShutdownList(cache);
        cache.size();//initialise it
        return cache;
    }

    private void addToShutdownList(NamedCache cache) {
        services.add(cache.getCacheService());
        caches.add(cache);
    }

    protected NamedCache getRemotePofCache(String name) {
        return CacheFactory.getCacheFactoryBuilder()
                .getConfigurableCacheFactory("config/extend-client-32001-pof.xml", classLoader)
                .ensureCache(name, classLoader);
    }


    protected void assertWithinTolerance(long expected, long actual, double toleranceFraction) {
        String message = String.format("Expected:%,d, Actual: %,d, %s \n", expected, actual, toleranceFraction);
        assertTrue(message, expected + (toleranceFraction * expected) > actual);
        assertTrue(message, expected - (toleranceFraction * expected) < actual);
    }

    protected void clearDataDirectories() {

        try {
            Path directory = Paths.get("data");
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }

            });

            Files.createDirectories(Paths.get("data"));
            Files.createDirectories(Paths.get("data/journalling"));
            Files.createDirectories(Paths.get("data/bdb"));

        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private boolean skipWitForProcessTearDown = false;

    protected void assertClusterStarted() {
        if (CacheFactory.getCluster().getMemberSet().size() < executor.countOfStartedNodes + 1) {
            skipWitForProcessTearDown = true;
            String format = String.format("Started %s nodes but the cluster contains %s. This is likely a problem" +
                    " with clustering. Make sure you are using the same cache config in different nodes. " +
                    (ProcessLogger.loggingProfile == ProcessLogger.LogTo.fileOnly ? "Your logging is set to file only so change: ProcessLogger.loggingProfile to see Coherence logging in the console. " : ""), executor.countOfStartedNodes, CacheFactory.getCluster().getMemberSet().size());
            fail(format);
        } else if (CacheFactory.getCluster().getMemberSet().size() > executor.countOfStartedNodes + 1) {
            skipWitForProcessTearDown = true;
            String format = String.format("Started %s nodes but the cluster contains %s. \nThe most likely cause is " +
                    "that there are orphaned processes from a previous run clustering with this test. " +
                    "\nIf you are on a unix system you can fix this by running the below command in your terminal: \n" +
                    "kill $(ps aux | grep 'DefaultCacheServer' | awk '{print $2}')", executor.countOfStartedNodes, CacheFactory.getCluster().getMemberSet().size());
            fail(format);
        }
    }
}
