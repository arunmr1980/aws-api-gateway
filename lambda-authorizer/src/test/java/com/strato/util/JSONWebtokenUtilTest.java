package com.strato.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class JSONWebtokenUtilTest{

  private static final Logger logger = LoggerFactory.getLogger(JSONWebtokenUtilTest.class);

  @Test
  void validateToken() throws Exception {
    logger.info("validateToken test");

    String token = JSONWebtokenUtil.getJWTToken("unittest");
    boolean result = JSONWebtokenUtil.validateToken(token);
    assertTrue(result);
  }
  
}
