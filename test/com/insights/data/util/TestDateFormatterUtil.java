package com.insights.data.util;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.Date;
import org.junit.Test;


public class TestDateFormatterUtil {
  @Test
  public void testFormatDateIntoString() {
    Date date = new Date(1491435067000L);
    assertEquals(DateFormatterUtil.format(date), "05/Apr/2017:19:31:07 -0400");
  }

  @Test
  public void testParseStringIntoDate() throws ParseException {
    String str = "05/Apr/2017:19:31:07 -0400";
    assertEquals(DateFormatterUtil.parse(str), new Date(1491435067000L));
  }


}
