package com.insights.data.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.junit.Test;


public class TestPriorityQueueUtil {

  @Test
  public void testGetTopNHappyPath() {
    List<Integer> elements = Arrays.asList(8, 4, 3, 6, 10, 5);
    List<Integer> expected = Arrays.asList(10, 8, 6);
    List<Integer> actual = PriorityQueueUtil.getTopN(elements, getIntegerComparator(), 3);
    assertEquals(expected.size(), actual.size());
    for (int i = 0; i < expected.size(); i++) {
      assertEquals(expected.get(i), actual.get(i));
    }
  }

  @Test
  public void testLessElementsThanN() {
    List<Integer> elements = Arrays.asList(8, 4, 3, 6, 10, 5);
    List<Integer> expected = Arrays.asList(10, 8, 6, 5, 4, 3);
    List<Integer> actual = PriorityQueueUtil.getTopN(elements, getIntegerComparator(), 20);
    assertEquals(expected.size(), actual.size());
    for (int i = 0; i < expected.size(); i++) {
      assertEquals(expected.get(i), actual.get(i));
    }
  }

  private Comparator<Integer> getIntegerComparator() {
    return new Comparator<Integer>() {
      @Override
      public int compare(Integer o1, Integer o2) {
        return o1.compareTo(o2);
      }
    };
  }
}
