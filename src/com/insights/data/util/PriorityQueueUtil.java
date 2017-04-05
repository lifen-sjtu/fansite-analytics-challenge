package com.insights.data.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;


/**
 * This util handles the priority queue functionality which is useful in multiple features.
 */
public class PriorityQueueUtil {

  /**
   * Given a list of elements, find the top N elements using a prioirty queue to avoid a full sorting.
   * @param elements the list of elements we want to find top N
   * @param comparator the self defined comparator used for ranking
   * @param N the number of topN
   * @param <T> the generic type of the element
   * @return a sorted list of top N elements
   */
  public static <T> List<T> getTopN(List<T> elements, Comparator<T> comparator, int N) {
    PriorityQueue<T> queue = new PriorityQueue<>(comparator);
    for (T element : elements) {
      if (queue.size() < N) {
        queue.offer(element);
      } else {
        if (comparator.compare(queue.peek(), element) < 0) {
          queue.poll();
          queue.offer(element);
        }
      }
    }

    List<T> temp = new ArrayList<>();
    List<T> result = new ArrayList<>();
    while (!queue.isEmpty()) {
      temp.add(queue.poll());
    }
    for (int i = temp.size() - 1; i >= 0; i--) {
      result.add(temp.get(i));
    }

    return result;
  }
}
