package com.insights.data.models;

import java.util.Comparator;


public class Resource {
  private String name;
  private long bytesConsumed;

  public Resource(String name, long bytesConsumed) {
    this.name = name;
    this.bytesConsumed = bytesConsumed;
  }

  public String getName() {
    return name;
  }

  public long getBytesConsumed() {
    return bytesConsumed;
  }

  public static Comparator<Resource> getComparator() {
    return new Comparator<Resource>() {
      @Override
      public int compare(Resource o1, Resource o2) {
        if (o1.getBytesConsumed() - o2.getBytesConsumed() < 0) {
          return -1;
        } else if (o1.getBytesConsumed() - o2.getBytesConsumed() == 0) {
          return o2.name.compareTo(o1.name);
        } else {
          return 1;
        }
      }
    };
  }
}
