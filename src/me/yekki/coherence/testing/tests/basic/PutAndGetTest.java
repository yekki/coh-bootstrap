package me.yekki.coherence.testing.tests.basic;

import com.benstopford.coherence.bootstrap.structures.framework.cluster.ClusterRunner;
import com.tangosol.net.NamedCache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PutAndGetTest extends ClusterRunner {
	
	@Test
	public void putAndGetFromCache() {
		
		NamedCache cache = getBasicCache();
		cache.put("key", "value");
		assertEquals("value", cache.get("key"));
	}
}
