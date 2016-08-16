package me.yekki.coh.bootstrap.basic;

import me.yekki.coh.bootstrap.structures.framework.cluster.ClusterRunner;
import me.yekki.coh.bootstrap.structures.tools.SampleInvocable;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.InvocationService;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;

public class DoRemoteWorkWithInvocablesTest extends ClusterRunner {

    public static final String config = "config/basic-invocation-service-1.xml";

    @Test
    public void shouldDoRemoteWork() throws InterruptedException {

        //get a handle on the invocation service
        InvocationService invocationService = getService();

        assertClusterStarted();

        //create an invocable to do some work - this fella just returns the memberId of the node its running on
        SampleInvocable getMemberIdInvocable = new SampleInvocable();

        //invoke!
        Map membersAndTheirIds = invocationService.query(getMemberIdInvocable, invocationService.getInfo().getServiceMembers());

        //check that each of the three nodes returned their member id (one of them will be this process as we're running as a cluster member)
        assertThat(membersAndTheirIds.values(), (Matcher)org.hamcrest.CoreMatchers.hasItems(1,2,3));
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        //start two remote nodes
        startCoherenceProcess(config);
        startCoherenceProcess(config);
    }
}
