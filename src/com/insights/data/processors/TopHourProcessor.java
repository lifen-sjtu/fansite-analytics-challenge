package com.insights.data.processors;

import com.insights.data.util.DateFormatterUtil;
import com.insights.data.models.SlidingWindow;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;


/**
 * processor to calculate the top hours as required in feature 3
 */
public class TopHourProcessor {
  private static final int WINDOW_SIZE = 3600;
  private long[] _logCounters;
  private long _totalVisitCount;
  private Queue<SlidingWindow> _slidingWindowQueue;

  public TopHourProcessor() {
    _logCounters = new long[WINDOW_SIZE];
    _totalVisitCount = 0;
    _slidingWindowQueue = new PriorityQueue<>(SlidingWindow.getComparator());
  }

  // update the number of hits at given second from the number of hits one hour ago.
  public void updateSlidingWindow(long initialSecond, long currentSecond, long countOfVisitsAtCurrentSecond, int n) {
    int index = (int) (currentSecond % WINDOW_SIZE);
    _totalVisitCount = _totalVisitCount + countOfVisitsAtCurrentSecond - _logCounters[index];
    _logCounters[index] = countOfVisitsAtCurrentSecond;
    long startingSecondOfTheHour = currentSecond - WINDOW_SIZE + 1;
    if (startingSecondOfTheHour >= initialSecond) {
      if (_slidingWindowQueue.size() < n) {
        _slidingWindowQueue.offer(new SlidingWindow(
            _totalVisitCount, DateFormatterUtil.format(new Date(startingSecondOfTheHour*1000))));
      } else {
        if (_slidingWindowQueue.peek().getCount() < _totalVisitCount) {
          _slidingWindowQueue.poll();
          _slidingWindowQueue.offer(new SlidingWindow(
              _totalVisitCount, DateFormatterUtil.format(new Date(startingSecondOfTheHour*1000))));
        }
      }
    }
  }

  public List<SlidingWindow> getTopNSlidingWindows() {
    List<SlidingWindow> temp = new ArrayList<>();
    List<SlidingWindow> result = new ArrayList<>();
    while (!_slidingWindowQueue.isEmpty()) {
      temp.add(_slidingWindowQueue.poll());
    }
    for (int i = temp.size() - 1; i >= 0; i--) {
      result.add(temp.get(i));
    }
    return result;
  }
}
