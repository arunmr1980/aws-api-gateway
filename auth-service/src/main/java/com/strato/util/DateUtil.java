package com.strato.util;

import java.time.Clock;

public class DateUtil{

  public static long getNowInMilliSeconds(){
    Clock clock = Clock.systemDefaultZone();
    return clock.millis();
  }
}
