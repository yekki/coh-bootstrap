package me.yekki.coh.bootstrap.structures.framework.cluster;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.CacheService;
import com.tangosol.net.InvocationService;
import com.tangosol.net.NamedCache;
import org.junit.After;
import org.junit.Before;

import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.HashSet;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


public abstract class ClusterRunner extends ClusterCommon {

    private ProcessExecutor executor = new ProcessExecutor(getProperties());
    private HashSet<CacheService> services = new HashSet<>();
    private HashSet<NamedCache> caches = new HashSet<>();
    private boolean skipWitForProcessTearDown = false;

    @Before
    public void setUp() throws Exception {
        executor.countOfStartedNodes = 0;
        skipWitForProcessTearDown = false;
        deleteContentsOfLogDir();
        ProcessLogger.switchStdErrToFile();
        new PersistentPortTracker().incrementExtendPort();
        System.getProperties().putAll(getProperties());
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


    protected void releaseLocalCacheResources() {

        caches.forEach(c->c.release());

        caches.clear();
    }

    protected void shutdownServices() {

        services.forEach(s->s.shutdown());

        services.clear();
    }

    protected NamedCache getCache(String configLocation, String cacheName) {
        NamedCache cache = CacheFactory.getCacheFactoryBuilder()
                .getConfigurableCacheFactory(configLocation, classLoader)
                .ensureCache(cacheName, classLoader);
        addToShutdownList(cache);
        cache.size();//initialise it
        return cache;
    }

    protected NamedCache getCache(String configLocation) {

        return getCache(configLocation, "foo");
    }

    protected InvocationService getService(String configLocation, String serviceName) {
        return (InvocationService) CacheFactory.getCacheFactoryBuilder()
                .getConfigurableCacheFactory(configLocation, classLoader)
                .ensureService(serviceName);
    }

    protected InvocationService getService() {
        return getService("config/basic-invocation-service-1.xml", "MyInvocationService1");
    }

    protected NamedCache getBasicCache() {

        return getCache("config/basic-cache.xml", "foo");
    }

    public NamedCache getBasicCache(String cacheName) {

        return getCache("config/basic-cache.xml", cacheName);
    }

    protected NamedCache getExtendCache() {
        return getCache("config/extend-client-32001.xml", "foo");
    }

    protected NamedCache getExtendCache(String cacheName) {
        return getCache("config/extend-client-32001.xml", cacheName);
    }

    protected void startOutOfProcessWithJMX(String config) {
        executor.startOutOfProcess(config, getJMXProperties(3000));
    }

    protected void startCoherenceProcess() throws IOException, InterruptedException {
        executor.startOutOfProcess("config/basic-cache.xml", "");
    }

    protected Process startCoherenceProcess(String config) {

        return executor.startOutOfProcess(config, "");
    }

    protected Process startCoherenceProcess(String config, String properties) {
        return executor.startOutOfProcess(config, properties);
    }

    protected int startCoherenceProcessWithJmx(String config) {
        int port = 4100;
        executor.startOutOfProcess(config, getJMXProperties(port));
        return port;
    }

    protected void startCoherenceProcessWithJMX(int port) throws IOException, InterruptedException {
        executor.startOutOfProcess("config/basic-cache.xml", getJMXProperties(port));
    }

    protected void startCoherenceProcessWithJMX(String config, int port) throws IOException, InterruptedException {
        executor.startOutOfProcess(config, getJMXProperties(port));
    }

    protected void startDataDisabledExtendProxy() throws IOException, InterruptedException {
        executor.startOutOfProcess("config/basic-extend-enabled-cache-32001.xml", LOCAL_STORAGE_FALSE);
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

    protected void waitForAllKilledMembersToTimeOut() throws InterruptedException {
        while (CacheFactory.ensureCluster().getMemberSet().size() > 1) {
            System.out.println("Waiting for " + CacheFactory.ensureCluster().getMemberSet().size() + " active cluster members to be deregistered");
            System.out.println(CacheFactory.ensureCluster().getMemberSet());
            Thread.sleep(1000);
        }
    }

    protected void deleteContentsOfLogDir() {
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

    public static boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (null != files) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        return (directory.delete());
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

    public void startLocalJMXServer(int port) throws IOException {

        try {
            LocateRegistry.createRegistry(port);
            String url = String.format("service:jmx:rmi:///jndi/rmi://localhost:%s/jmxrmi", port);

            JMXConnectorServerFactory.newJMXConnectorServer(
                    new JMXServiceURL(url),
                    null,
                    ManagementFactory.getPlatformMBeanServer()
            ).start();

        } catch (RemoteException justMeansItWasCreatedAlready) {
            return;
        }
    }
}
