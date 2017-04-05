package com.insights.data;

import com.insights.data.models.FanSiteLog;
import com.insights.data.processors.LogProcessor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

public class Main {

  public static void main(String[] args) {

    String fileName = args[0];
    String line;
    LogProcessor logProcessor = new LogProcessor();
    try {
      FileReader fileReader = new FileReader(fileName);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      while ((line = bufferedReader.readLine()) != null) {
        try {
          FanSiteLog currentLog = new FanSiteLog(line);
          logProcessor.readOneLog(currentLog);
        } catch (ParseException | NumberFormatException ex) {
          // get some issues parsing a log, just skip it without breaking the main thread.
          continue;
        }
      }
      bufferedReader.close();
      logProcessor.markReadingDone();

      logProcessor.generateTopNHosts(args[1]);
      logProcessor.generateTopNBusyHours(args[2]);
      logProcessor.generateTopNResources(args[3]);
      logProcessor.generateBlockedAttempts(args[4]);

    } catch (IOException ex) {
      System.out.println("unable to read or write file ");
    }
  }
}
