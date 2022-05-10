package com.strato.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

class DateUtilTest{

  private static final Logger logger = LoggerFactory.getLogger(StringUtilTest.class);

  @Test
  void isExpired() throws Exception {
    logger.info("###---   isExpired() test");

    long now = DateUtil.getNowInMilliSeconds();
    long future_date_expiry = now + 100000;
    long past_date_expiry = now - 100000;

    assertTrue(DateUtil.isExpired(past_date_expiry), "Past date must be expired");
    assertFalse(DateUtil.isExpired(future_date_expiry), "Future date must not be expired");
  }


}
