package com.insights.data.models;

import java.util.Comparator;


public class SlidingWindow {
  private long count;
  private String formattedStartTimeOfTheHour;

  public SlidingWindow(long count, String formattedTimestamp) {
    this.count = count;
    this.formattedStartTimeOfTheHour = formattedTimestamp;
  }

  public long getCount() {
    return count;
  }

  public String getFormattedStartTimeOfTheHour() {
    return formattedStartTimeOfTheHour;
  }

  public static Comparator<SlidingWindow> getComparator() {
    return new Comparator<SlidingWindow>() {
      @Override
      public int compare(SlidingWindow o1, SlidingWindow o2) {
        if (o1.getCount() > o2.getCount()) {
          return 1;
        } else if (o1.getCount() == o2.getCount()) {
          return o2.getFormattedStartTimeOfTheHour().compareTo(o1.getFormattedStartTimeOfTheHour());
        } else {
          return -1;
        }
      }
    };
  }
}
