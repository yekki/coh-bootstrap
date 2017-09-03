package me.yekki.coh.bootstrap.structures.tools;

import com.tangosol.util.InvocableMap;

import java.util.Collection;
import java.util.Set;

public class ParallelSumAggregator implements InvocableMap.ParallelAwareAggregator
{

    private static final long serialVersionUID = 1L;

    private InvocableMap.EntryAggregator delegate;

    public ParallelSumAggregator(InvocableMap.EntryAggregator delegate) {
        this.delegate = delegate;
    }

    @Override
    public InvocableMap.EntryAggregator getParallelAggregator() {
        return this;
    }


    @Override
    @SuppressWarnings("rawtypes")
    public Object aggregateResults(Collection collection) {
        int result = 0;
        for(Object o: collection){
            result+= (Integer) o;
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    public Object aggregate(Set set) {
        return  delegate.aggregate(set);
    }
}