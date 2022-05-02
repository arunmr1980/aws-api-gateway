package com.strato.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class StringUtilTest{

  private static final Logger logger = LoggerFactory.getLogger(StringUtilTest.class);

  @Test
  void getGeneratedToken() throws Exception {
    logger.info("getGeneratedToken test");

    String token = StringUtil.getGeneratedToken();
    logger.info(" token :- " + token);

    assertNotNull(token);
  }

  
}
