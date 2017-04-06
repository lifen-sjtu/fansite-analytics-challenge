package com.insights.data;

import com.insights.data.processors.TestTopHostProcessor;
import com.insights.data.processors.TestTopResourceProcessor;
import com.insights.data.util.TestDateFormatterUtil;
import com.insights.data.util.TestPriorityQueueUtil;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


public class TestRunner {
  public static void main(String[] args) {

    Result result = JUnitCore.runClasses(TestTopHostProcessor.class, TestTopHostProcessor.class,
        TestTopResourceProcessor.class, TestDateFormatterUtil.class, TestPriorityQueueUtil.class);

    for (Failure failure : result.getFailures()) {
      System.out.println(failure.getTrace());
    }

    System.out.println(result.wasSuccessful());
  }
}
