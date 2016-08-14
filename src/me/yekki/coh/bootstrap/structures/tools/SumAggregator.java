package me.yekki.coh.bootstrap.structures.tools;

import com.tangosol.util.InvocableMap;

import java.util.Map;
import java.util.Set;

public class SumAggregator implements InvocableMap.EntryAggregator {
 
	private static final long serialVersionUID = 1L;

	public Object aggregate(Set set) {
        int result = 0;
        for(Object o: set){
            Map.Entry entry = (Map.Entry) o;
            result+= (Integer) entry.getValue();
        }
        return result;
    }
}
