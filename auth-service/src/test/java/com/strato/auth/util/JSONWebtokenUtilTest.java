package com.strato.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class JSONWebtokenUtilTest{

  private static final Logger logger = LoggerFactory.getLogger(CryptoUtilsTest.class);

  @Test
  void getJWTToken() throws Exception {
    logger.info("getJWTToken test");

    String subject = "helloworld";

    String jwtToken = JSONWebtokenUtil.getJWTToken(subject);
    logger.info("subject :- " + subject);
    logger.info("jwtToken :- " + jwtToken);

    assertNotNull(jwtToken);
  }
}
