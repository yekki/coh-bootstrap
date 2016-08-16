package me.yekki.coh.bootstrap.basic;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.net.cache.TypeAssertion;
import me.yekki.coh.bootstrap.structures.framework.cluster.ClusterRunner;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class GenericTypeTest extends ClusterRunner{

    @Test
    public void demoOfGenericType1() {

        NamedCache<String, String> cache = CacheFactory.getTypedCache("generic-type", TypeAssertion.WITH_RAW_TYPES);
        cache.put("key", "value");
        Object value = cache.get("key");
        assertThat(value, instanceOf(String.class));
    }

    @Test
    public void demoOfGenericType2() {
        NamedCache<String, String> cache = CacheFactory.getTypedCache("generic-type", TypeAssertion.withRawTypes());
        cache.put("key", "value");
        Object value = cache.get("key");
        assertThat(value, instanceOf(String.class));
    }

    @Test
    public void demoOfGenericType3() {
        NamedCache<String, String> cache = CacheFactory.getTypedCache("generic-type", TypeAssertion.withTypes(String.class, String.class));
        cache.put("key", "value");
        Object value = cache.get("key");
        assertThat(value, instanceOf(String.class));
    }
}
