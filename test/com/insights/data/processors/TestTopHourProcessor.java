package com.insights.data.processors;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class TestTopHourProcessor {
  private static final int WINDOW_SIZE = 3600;

  @Test
  public void testUpdateSlidingWindow() {
    TopHourProcessor topHourProcessor = new TopHourProcessor();
    long initialSecond = 0;
    long firstUpdateTime = 10;
    long secondUpdateTime = 4000;
    int topN = 3;
    topHourProcessor.updateSlidingWindow(initialSecond, firstUpdateTime, 100, topN);
    assertEquals(topHourProcessor.getTotalVisitCount(), 100l);
    long[] expectedCounters = new long[WINDOW_SIZE];
    expectedCounters[(int)firstUpdateTime] = 100;
    long[] actualCounters = topHourProcessor.getLogCounters();
    for (int i = 0; i < expectedCounters.length; i++) {
      assertEquals(actualCounters[i], expectedCounters[i]);
    }


    for (long i = firstUpdateTime + 1; i < secondUpdateTime; i++) {
      topHourProcessor.updateSlidingWindow(initialSecond, i, 0, topN);
    }

    topHourProcessor.updateSlidingWindow(initialSecond, secondUpdateTime, 200, topN);
    assertEquals("The previous 100 visits should expire", topHourProcessor.getTotalVisitCount(), 200l);
    expectedCounters[(int)firstUpdateTime] = 0;
    expectedCounters[(int)secondUpdateTime%WINDOW_SIZE] = 200;
    actualCounters = topHourProcessor.getLogCounters();
    for (int i = 0; i < expectedCounters.length; i++) {
      assertEquals("The previous 100 visits should expire", actualCounters[i], expectedCounters[i]);
    }
  }

}
