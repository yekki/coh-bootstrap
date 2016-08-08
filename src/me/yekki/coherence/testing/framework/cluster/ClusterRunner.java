package me.yekki.coherence.testing.framework.cluster;

public class ClusterRunner extends TestCommon {

    public final String LOCAL_STORAGE_FALSE = "-Dtangosol.coherence.distributed.localstorage=false";

    protected ClassLoader classloader = getClass().getClassLoader();
}
