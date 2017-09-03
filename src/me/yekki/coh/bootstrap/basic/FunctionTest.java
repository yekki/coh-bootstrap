package me.yekki.coh.bootstrap.basic;

import com.tangosol.net.NamedCache;
import com.tangosol.util.Filter;
import com.tangosol.util.filter.LessFilter;
import com.tangosol.util.filter.LikeFilter;
import me.yekki.coh.bootstrap.structures.dataobjects.Person;
import me.yekki.coh.bootstrap.structures.framework.cluster.ClusterRunner;
import org.junit.Test;

import java.util.List;

public class FunctionTest extends ClusterRunner {
/*
    @Test
    public void replaceAll() {

        NamedCache<Integer, Integer> cache = getBasicCache();

        IntStream.range(0, 10).forEach(i->cache.put(i, i));
        cache.replaceAll((k, v)->{
            return v * 2;
        });


        assertEquals(cache.get(Integer.valueOf(1)), Integer.valueOf(2));
    }
*/
    @Test
    public void filter() {

        NamedCache<String, Person> cache = getBasicPofCache();
        List<Person> persons = Person.createShortList();

        persons.forEach(p->cache.put(p.getEmail(), p));

        Filter filter = new LikeFilter<>(Person::getGivenName, "%Phil").and(new LessFilter<>(Person::getAge, 60));

        cache.keySet(filter).forEach(p->System.out.println(cache.get(p)));
    }
}
