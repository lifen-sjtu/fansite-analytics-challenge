package com.insights.data.models;

import com.insights.data.util.DateFormatterUtil;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FanSiteLog {

  private String originalLog;
  private String host;
  private Date timestamp;
  private String resource;
  private long httpStatusCode;
  private long bytes;

  public FanSiteLog(String log) throws ParseException, NumberFormatException {
    originalLog = log.trim();

    final String regex = "(.*)\\s-\\s-\\s\\[(.*)]\\s\"(.*)\"\\s(.*)\\s(.*)";

    final Pattern pattern = Pattern.compile(regex);
    final Matcher matcher = pattern.matcher(log);

    if (!matcher.find() || matcher.groupCount() != 5) {
      throw new ParseException(log, 0);
    }
    host = matcher.group(1);
    timestamp = DateFormatterUtil.parse(matcher.group(2));
    String request = matcher.group(3);
    String[] strings = request.split(" ");
    if (strings != null && strings.length > 1) {
      resource = strings[1];
    } else {
      resource = request;
    }
    httpStatusCode = Long.parseLong(matcher.group(4));
    if (matcher.group(5).equals("-")) {
      bytes = 0;
    } else {
      bytes = Long.parseLong(matcher.group(5));
    }

  }

  public Date getTimestamp() {
    return timestamp;
  }

  public long getBytes() {
    return bytes;
  }

  public long getHttpStatusCode() {
    return httpStatusCode;
  }

  public String getHost() {
    return host;
  }

  public String getOriginalLog() {
    return originalLog;
  }

  public String getResource() {
    return resource;
  }

  public long getTimeStampInSeconds() {
    return timestamp.getTime() / 1000;
  }
}
