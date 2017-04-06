package com.insights.data.processors;

import com.insights.data.models.FanSiteLog;
import java.text.ParseException;
import org.junit.Test;

import static org.junit.Assert.*;


public class TestTopResourceProcessor {
  @Test
  public void testUpdateResource() throws ParseException {
    TopResourceProcessor topResourceProcessor = new TopResourceProcessor();
    FanSiteLog log1 = new FanSiteLog("199.72.81.55 - - [01/Jul/1995:00:00:01 -0400] \"POST /login HTTP/1.0\" 200 1420");
    FanSiteLog log2 = new FanSiteLog("199.72.81.56 - - [01/Jul/1995:00:00:09 -0400] \"GET /shuttle/countdown/\" 401 120");
    FanSiteLog log3 = new FanSiteLog("199.72.81.55 - - [01/Jul/1995:00:00:09 -0400] \"GET /shuttle/countdown/\" 401 120");
    topResourceProcessor.updateResourceCounters(log1);
    assertEquals(topResourceProcessor.getResourcesCounters().size(), 1);
    assertEquals(topResourceProcessor.getResourcesCounters().get("/login").longValue(), 1420l);

    topResourceProcessor.updateResourceCounters(log2);
    assertEquals(topResourceProcessor.getResourcesCounters().size(), 2);
    assertEquals(topResourceProcessor.getResourcesCounters().get("/shuttle/countdown/").longValue(), 120l);


    topResourceProcessor.updateResourceCounters(log3);
    assertEquals(topResourceProcessor.getResourcesCounters().size(), 2);
    assertEquals(topResourceProcessor.getResourcesCounters().get("/shuttle/countdown/").longValue(), 240l);
  }
}
