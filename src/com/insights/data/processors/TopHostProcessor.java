package com.insights.data.processors;

import com.insights.data.util.PriorityQueueUtil;
import com.insights.data.models.FanSiteLog;
import com.insights.data.models.Host;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * processor to find the top 10 hosts as required in feature 1
 */
public class TopHostProcessor {
  private Map<String, Long> _hostCounters = new HashMap<>();

  /**
   * When there is a new log coming, update the host counter map
   * @param log
   */
  public void updateHostCounters(FanSiteLog log) {
    if (_hostCounters.containsKey(log.getHost())) {
      _hostCounters.put(log.getHost(), _hostCounters.get(log.getHost()) + 1);
    } else {
      _hostCounters.put(log.getHost(), 1L);
    }
  }

  /**
   * This method should be invoked when you want to get the topN resources based on the current input stream
   * @param n the number of topN
   * @return the list of top host
   */
  public List<Host> getTopNHosts(int n) {
    List<Host> elements = new ArrayList<>();
    for (Map.Entry<String, Long> entry : _hostCounters.entrySet()) {
      Host host = new Host(entry.getKey(), entry.getValue());
      elements.add(host);
    }
    return PriorityQueueUtil.getTopN(elements, Host.getComparator(), n);
  }

  
  public Map<String, Long> getHostCounters() {
    return _hostCounters;
  }
}
