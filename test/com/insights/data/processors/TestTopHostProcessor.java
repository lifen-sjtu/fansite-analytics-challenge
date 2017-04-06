package com.insights.data.processors;

import com.insights.data.models.FanSiteLog;
import java.text.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestTopHostProcessor {
  @Test
  public void testUpdateHost() throws ParseException {
    TopHostProcessor topHostProcessor = new TopHostProcessor();
    FanSiteLog log1 = new FanSiteLog("199.72.81.55 - - [01/Jul/1995:00:00:01 -0400] \"POST /login HTTP/1.0\" 200 1420");
    FanSiteLog log2 = new FanSiteLog("199.72.81.56 - - [01/Jul/1995:00:00:09 -0400] \"GET /shuttle/countdown/\" 401 120");
    FanSiteLog log3 = new FanSiteLog("199.72.81.55 - - [01/Jul/1995:00:00:09 -0400] \"GET /shuttle/countdown/\" 401 120");
    topHostProcessor.updateHostCounters(log1);
    assertEquals(topHostProcessor.getHostCounters().size(), 1);
    assertEquals(topHostProcessor.getHostCounters().get("199.72.81.55").longValue(), 1l);

    topHostProcessor.updateHostCounters(log2);
    assertEquals(topHostProcessor.getHostCounters().size(), 2);
    assertEquals(topHostProcessor.getHostCounters().get("199.72.81.56").longValue(), 1l);


    topHostProcessor.updateHostCounters(log3);
    assertEquals(topHostProcessor.getHostCounters().size(), 2);
    assertEquals(topHostProcessor.getHostCounters().get("199.72.81.55").longValue(), 2l);
  }

}
