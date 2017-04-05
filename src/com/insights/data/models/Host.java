package com.insights.data.models;

import java.util.Comparator;


public class Host {
  private String name;
  private long frequency;

  public Host(String name, long frequency) {
    this.name = name;
    this.frequency = frequency;
  }

  public String getName() {
    return name;
  }

  public long getFrequency() {
    return frequency;
  }

  public static Comparator<Host> getComparator() {
    return new Comparator<Host>() {
      @Override
      public int compare(Host o1, Host o2) {
        if (o1.getFrequency() - o2.getFrequency() < 0) {
          return -1;
        } else if (o1.getFrequency() - o2.getFrequency() == 0) {
          return o2.name.compareTo(o1.name);
        } else {
          return 1;
        }
      }
    };
  }
}
