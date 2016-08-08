package me.yekki.coherence.testing.dataobjects;

import junit.framework.Test;
import me.yekki.coherence.testing.framework.cluster.TestCommon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class SizableObjectFactory {

    private static long counter = 1L;

    public SizableObject buildObject(long sizeInKB) {

        List<byte[]> payload = new ArrayList<byte[]>();
        LongStream.range(0, sizeInKB).forEach(it->{
            payload.add(new byte[TestCommon.KB]);
        });

        return new SizableObject(payload);
    }

    public Map<Long, SizableObject> buildMapOfObjectsObject(int numberOfObjects, long sizeInKB) {

        Map<Long, SizableObject> map = new HashMap<Long,SizableObject>();

        IntStream.range(0, numberOfObjects).forEach(i->{
            map.put(++counter, buildObject(sizeInKB));
        });

        return map;
    }
}
