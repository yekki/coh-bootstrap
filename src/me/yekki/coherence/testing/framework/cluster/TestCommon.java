package me.yekki.coherence.testing.framework.cluster;

import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class TestCommon {

    static {
        System.setProperty("tangosol.coherence.override", "config/tangosol-coherence-override.xml");
    }

    public static final long MB = 1024 * 1024;
    public static final int KB = 1024;

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
