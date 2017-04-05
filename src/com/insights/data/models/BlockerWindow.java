package com.insights.data.models;

public class BlockerWindow {
  public static int WINDOW_SIZE = 20;
  private long[] failedAttemptedCounts = new long[WINDOW_SIZE];
  private long totalFailedAttemptCountInTimeWindow;
  private long lastFailedAttemptTime;
  private long blockedSince;

  public long getFailedAttemptedCounts(int index) {
    return failedAttemptedCounts[index];
  }

  public long getTotalFailedAttemptCountInTimeWindow() {
    return totalFailedAttemptCountInTimeWindow;
  }

  public long getLastFailedAttemptTime() {
    return lastFailedAttemptTime;
  }

  public long getBlockedSince() {
    return blockedSince;
  }

  public void setTotalFailedAttemptCountInTimeWindow(long totalFailedAttemptCountInTimeWindow) {
    this.totalFailedAttemptCountInTimeWindow = totalFailedAttemptCountInTimeWindow;
  }

  public void setFailedAttemptedCounts(int index, long count) {
    failedAttemptedCounts[index] = count;
  }

  public void setBlockedSince(long blockedSince) {
    this.blockedSince = blockedSince;
  }

  public void setLastFailedAttemptTime(long lastFailedAttemptTime) {
    this.lastFailedAttemptTime = lastFailedAttemptTime;
  }
}
