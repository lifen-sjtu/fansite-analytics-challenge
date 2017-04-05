package com.insights.data.processors;

import com.insights.data.models.FanSiteLog;
import com.insights.data.models.Host;
import com.insights.data.models.Resource;
import com.insights.data.models.SlidingWindow;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


/**
 * Main processor to read the log, and delegate tasks to feature specific processor, and write results into files.
 */
public class LogProcessor {

  private static final int TOP_N = 10;
  private TopResourceProcessor _topResourceProcessor;
  private TopHostProcessor _topHostProcessor;
  private TopHourProcessor _topHourProcessor;
  private LoginAttemptProcessor _loginAttemptProcessor;
  private List<FanSiteLog> _bufferedLogsInOneSecond;
  private long _initialSecond;
  private long _currentSecond;

  public LogProcessor() {
    _topHostProcessor = new TopHostProcessor();
    _topResourceProcessor = new TopResourceProcessor();
    _topHourProcessor = new TopHourProcessor();
    _loginAttemptProcessor = new LoginAttemptProcessor();
    _bufferedLogsInOneSecond = new ArrayList<>();
    _initialSecond = 0;
    _currentSecond = 0;
  }

  public void readOneLog(FanSiteLog log) {
    if (_initialSecond == 0) {
      _initialSecond = log.getTimeStampInSeconds();
      _currentSecond = log.getTimeStampInSeconds();
    }
    // we will buffer the logs in the same second and do one shot processing
    if (_currentSecond == log.getTimeStampInSeconds()) {
      _bufferedLogsInOneSecond.add(log);
    } else {
      clearBufferedLogsTillEnd(log.getTimeStampInSeconds());
      _bufferedLogsInOneSecond.add(log);
    }
  }

  // when we are done with reading all logs, clear the buffer for the last time.
  public void markReadingDone() {
    clearBufferedLogsTillEnd(_currentSecond + 3600);
  }

  private void clearBufferedLogsTillEnd(long ending) {
    // for each second between the second of the buffered logs to the second of latest log,
    // we will update the sliding window to start from this second and see the total hits in one hour.
    _topHourProcessor.updateSlidingWindow(_initialSecond, _currentSecond, _bufferedLogsInOneSecond.size(), TOP_N);
    for (long i = _currentSecond + 1; i < ending; i++) {
      _topHourProcessor.updateSlidingWindow(_initialSecond, i, 0, TOP_N);
    }

    for (FanSiteLog bufferedLog : _bufferedLogsInOneSecond) {
      _topHostProcessor.updateHostCounters(bufferedLog);
      _topResourceProcessor.updateResourceCounters(bufferedLog);
      _loginAttemptProcessor.detectConsecutiveFailedLogins(bufferedLog);
    }
    _bufferedLogsInOneSecond = new ArrayList<>();
    _currentSecond = ending;
  }

  public void generateTopNHosts(String filename) throws IOException {
    List<Host> hosts = _topHostProcessor.getTopNHosts(TOP_N);
    File fout = new File(filename);
    FileOutputStream fos = new FileOutputStream(fout);
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
    for (Host host : hosts) {
      bw.write(host.getName() + "," + host.getFrequency());
      bw.newLine();
    }
    bw.close();
  }

  public void generateTopNResources(String filename) throws IOException {
    List<Resource> resources = _topResourceProcessor.getTopNResources(TOP_N);
    File fout = new File(filename);
    FileOutputStream fos = new FileOutputStream(fout);
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
    for (Resource resource : resources) {
      bw.write(resource.getName());
      bw.newLine();
    }
    bw.close();
  }

  public void generateTopNBusyHours(String filename) throws IOException {
    List<SlidingWindow> slidingWindows = _topHourProcessor.getTopNSlidingWindows();
    File fout = new File(filename);
    FileOutputStream fos = new FileOutputStream(fout);
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
    for (SlidingWindow slidingWindow : slidingWindows) {
      bw.write(slidingWindow.getFormattedStartTimeOfTheHour() + "," + slidingWindow.getCount());
      bw.newLine();
    }
    bw.close();
  }

  public void generateBlockedAttempts(String filename) throws IOException {
    List<String> blockedAttempts = _loginAttemptProcessor.getBlockedAttempts();
    File fout = new File(filename);
    FileOutputStream fos = new FileOutputStream(fout);
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
    for (String blockedAttempt : blockedAttempts) {
      bw.write(blockedAttempt);
      bw.newLine();
    }
    bw.close();
  }
}
