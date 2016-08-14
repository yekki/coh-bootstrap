package me.yekki.coh.bootstrap.structures.tools.index;

import java.util.Map;

public interface IndexSizerMBean {
    long getTotal();
    Map<String, Long> getIndexSizes();
}
