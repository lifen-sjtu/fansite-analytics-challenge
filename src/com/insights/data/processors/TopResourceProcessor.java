package com.insights.data.processors;

import com.insights.data.util.PriorityQueueUtil;
import com.insights.data.models.FanSiteLog;
import com.insights.data.models.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * processor to find the top 10 resources as required in feature 2
 */
public class TopResourceProcessor {
  private Map<String, Long> _resourcesCounters = new HashMap<>();

  public void updateResourceCounters(FanSiteLog log) {
    if (_resourcesCounters.containsKey(log.getResource())) {
      _resourcesCounters.put(log.getResource(), _resourcesCounters.get(log.getResource()) + log.getBytes());
    } else {
      _resourcesCounters.put(log.getResource(), log.getBytes());
    }
  }

  public List<Resource> getTopNResources(int n) {
    List<Resource> elements = new ArrayList<>();
    for (Map.Entry<String, Long> entry : _resourcesCounters.entrySet()) {
      Resource resource = new Resource(entry.getKey(), entry.getValue());
      elements.add(resource);
    }

    return PriorityQueueUtil.getTopN(elements, Resource.getComparator(), n);
  }
}
