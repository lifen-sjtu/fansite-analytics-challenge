package com.insights.data.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


/**
 * Util handles the date to string formatting and string to date parsing logic
 */
public class DateFormatterUtil {
  private static final String DATE_PATTERN = "dd/MMM/yyyy:HH:mm:ss Z";
  private static final SimpleDateFormat FORMATTER = new SimpleDateFormat(DATE_PATTERN);

  static {
    FORMATTER.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));
  }

  public static String format(Date date) {
    return FORMATTER.format(date);
  }

  public static Date parse(String s) throws ParseException {
    return FORMATTER.parse(s);
  }
}
