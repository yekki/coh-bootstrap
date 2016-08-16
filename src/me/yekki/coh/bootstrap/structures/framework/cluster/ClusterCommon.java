package me.yekki.coh.bootstrap.structures.framework.cluster;

import me.yekki.coh.bootstrap.structures.dataobjects.SizableObject;
import me.yekki.coh.bootstrap.structures.dataobjects.SizableObjectFactory;
import com.tangosol.net.NamedCache;

import java.util.Properties;
import java.util.stream.IntStream;

public class ClusterCommon {

    static {
        System.setProperty("coherence.override", "config/tangosol-coherence-override.xml");
        System.setProperty("tangosol.coherence.override", "config/tangosol-coherence-override.xml");
    }

    public static final long MB = 1024 * 1024;
    public static final int KB = 1024;
    public static final String LOCAL_STORAGE_FALSE = "-Dcoherence.distributed.localstorage=false  -Dtangosol.coherence.distributed.localstorage=false";
    private static Properties props;
    private static int DEFAULT_COHERENCE_VERSION = 1221;

    private static int prefix = 0;

    protected static Properties getProperties() {

        return getProperties(DEFAULT_COHERENCE_VERSION);
    }

    protected ClassLoader classLoader = getClass().getClassLoader();

    protected static Properties getProperties(int version) {

        if (props == null) props = new Properties();

        switch (version) {
            case 1221:
                props.put("coherence.clusteraddress", "239.255.12.30");
                props.put("coherence.clusterport", "1234");
                props.put("coherence.cluster", "yekki_cluster");
                props.put("coherence.log.level", "9");
                props.put("coherence.ttl", "0");
                props.put("coherence.management", "all");
                props.put("coherence.management.remote", "true");
                break;
            case 1213:
                props.put("tangosol.coherence.clusteraddress", "239.255.12.30");
                props.put("tangosol.coherence.clusterport", "1234");
                props.put("tangosol.coherence.cluster", "yekki_cluster");
                props.put("tangosol.coherence.log.level", "9");
                props.put("tangosol.coherence.ttl", "0");
                props.put("tangosol.coherence.management", "all");
                props.put("tangosol.coherence.management.remote", "true");
                break;
            default:
                throw new RuntimeException("unsupported coherence version" + version);
        }

        return props;
    }

    protected static String getJMXProperties(int jmx_port) {

        return "" +
                "-Dcom.sun.management.jmxremote.ssl=false " +
                "-Dcom.sun.management.jmxremote " +
                "-Dcom.sun.management.jmxremote.authenticate=false " +
                "-Dcom.sun.management.jmxremote.port=" + jmx_port + " ";
    }

    protected static void addData(NamedCache<String, SizableObject> cache, int mbToAdd) {
        prefix++;

        IntStream.range(0, mbToAdd).forEach(i-> {
            SizableObject sizableObject = new SizableObjectFactory().buildObject(1000);
            cache.put(prefix + Integer.toString(i), sizableObject);
        });

        System.out.println("added " + mbToAdd + "MB");
    }
}
