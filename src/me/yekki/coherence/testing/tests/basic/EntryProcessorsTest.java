package me.yekki.coherence.testing.tests.basic;

import com.benstopford.coherence.bootstrap.structures.framework.cluster.ClusterRunner;
import com.tangosol.net.NamedCache;
import com.tangosol.util.InvocableMap;
import com.tangosol.util.processor.AbstractProcessor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EntryProcessorsTest extends ClusterRunner {

    @Test
    public void doSomethingAtomicallyWithAnEntryProcessor() {

        NamedCache cache = getBasicCache();

        cache.put("key", "value");

        Object retValue = cache.invoke("key", new ValueChangingEntryProcessor("newValue"));

        assertEquals("The value has been set to newValue", retValue);
    }

    static final class ValueChangingEntryProcessor extends AbstractProcessor {

        private final String newValue;

        public ValueChangingEntryProcessor(String newValue) {

            this.newValue = newValue;
        }

        public Object process(InvocableMap.Entry entry) {
            entry.setValue(newValue);
            return "The value has been set to " + newValue;
        }
    }
}
