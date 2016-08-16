package me.yekki.coh.bootstrap.basic;

import com.tangosol.net.NamedCache;
import com.tangosol.util.InvocableMap;
import com.tangosol.util.processor.AbstractProcessor;
import me.yekki.coh.bootstrap.structures.framework.cluster.ClusterRunner;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class EntryProcessorsTest extends ClusterRunner {

    @Test
    public void doSomethingAtomicallyWithAnEntryProcessor() {
        NamedCache cache = getBasicCache();

        cache.put("Key", "Value");

        Object returnVal = cache.invoke("Key", new ValueChangingEntryProcessor("Value2"));

        assertEquals("The value has been set to Value2", returnVal);
    }

    private static final class ValueChangingEntryProcessor extends AbstractProcessor {
        private final String newValue;

        ValueChangingEntryProcessor(String newValue) {
            this.newValue = newValue;
        }

        public Object process(InvocableMap.Entry entry) {
            entry.setValue(newValue);
            return "The value has been set to " + newValue;
        }
    }
}
