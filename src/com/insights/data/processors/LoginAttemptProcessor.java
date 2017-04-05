package com.insights.data.processors;

import com.insights.data.models.BlockerWindow;
import com.insights.data.models.FanSiteLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * processor to block consecutive failed login attempts as required in feature 4
 */
public class LoginAttemptProcessor {
  private static final long FIVE_MINUTES_IN_SECONDS = 60 * 5;
  private static final String LOGIN_RESOURCE = "/login";
  private static final long LOGIN_FAILURE_STATUS_CODE = 401L;
  private static final long LOGIN_SUCCESS_STATUS_CODE = 200L;
  private static final int CONSECUTIVE_FAILURE_LOGIN_THRESHOLD_TO_BLOCK = 3;

  private Map<String, BlockerWindow> _blockerWindowMap = new HashMap<>();
  private List<String> _blockedAttempts = new ArrayList<>();

  public void detectConsecutiveFailedLogins(FanSiteLog log) {

    // If we find the host of incoming log is still in blocked state, just log it without inspecting the log.
    if (_blockerWindowMap.containsKey(log.getHost())) {
      BlockerWindow blockerWindow = _blockerWindowMap.get(log.getHost());
      if (blockerWindow.getBlockedSince() > 0 &&
          log.getTimeStampInSeconds() - blockerWindow.getBlockedSince() <= FIVE_MINUTES_IN_SECONDS) {
        _blockedAttempts.add(log.getOriginalLog());
        return;
      }
    }

    // If the host is in good status and this is a login attempt, we need to update the blocker window accordingly.
    if (LOGIN_RESOURCE.equals(log.getResource())) {
      if (log.getHttpStatusCode() == LOGIN_SUCCESS_STATUS_CODE) {
        // reset the blocker window since we observe a successful login
        _blockerWindowMap.put(log.getHost(), new BlockerWindow());
      } else if (log.getHttpStatusCode() == LOGIN_FAILURE_STATUS_CODE){
        BlockerWindow blockerWindow = _blockerWindowMap.containsKey(log.getHost()) ?
            _blockerWindowMap.get(log.getHost()) : new BlockerWindow();
        // update the counter in the blocker window to keep track of the total failures in the given time window
        updateFailedAttempt(blockerWindow, log);
        _blockerWindowMap.put(log.getHost(), blockerWindow);
      }
    }
  }

  // when there is a failed login, we need to mark the right index and increase total count by one.
  // however we also need to clear some previous failed logins if it didn't happen in the last 20 seconds.
  public void updateFailedAttempt(BlockerWindow blockerWindow, FanSiteLog log) {
    int index = (int) (log.getTimeStampInSeconds() % BlockerWindow.WINDOW_SIZE);
    long gap = log.getTimeStampInSeconds() - blockerWindow.getLastFailedAttemptTime() > BlockerWindow.WINDOW_SIZE - 1 ?
        BlockerWindow.WINDOW_SIZE - 1: log.getTimeStampInSeconds() - blockerWindow.getLastFailedAttemptTime();

    // update the current failed login
    blockerWindow.setTotalFailedAttemptCountInTimeWindow(
        blockerWindow.getTotalFailedAttemptCountInTimeWindow() + 1 - blockerWindow.getFailedAttemptedCounts(index));
    blockerWindow.setFailedAttemptedCounts(index, 1L);

    // clear the expired failed login
    int k = 1;
    while (k <= gap) {
      int indexToExpire = (index + k) % BlockerWindow.WINDOW_SIZE;

      blockerWindow.setTotalFailedAttemptCountInTimeWindow(
          blockerWindow.getTotalFailedAttemptCountInTimeWindow() - blockerWindow.getFailedAttemptedCounts(indexToExpire));
      blockerWindow.setFailedAttemptedCounts(indexToExpire, 0L);

      k++;
    }

    if (blockerWindow.getTotalFailedAttemptCountInTimeWindow() >= CONSECUTIVE_FAILURE_LOGIN_THRESHOLD_TO_BLOCK) {
      blockerWindow.setBlockedSince(log.getTimeStampInSeconds());
    } else {
      blockerWindow.setLastFailedAttemptTime(log.getTimeStampInSeconds());
    }
  }

  public List<String> getBlockedAttempts() {
    return _blockedAttempts;
  }

}
