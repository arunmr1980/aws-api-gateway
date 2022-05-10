package com.strato.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class JSONWebtokenUtilTest{

  private static final Logger logger = LoggerFactory.getLogger(JSONWebtokenUtilTest.class);

  @Test
  void getJWTToken() throws Exception {
    logger.info("getJWTToken test");

    String subject = "helloworld";

    String jwtToken = JSONWebtokenUtil.getJWTToken(subject);
    logger.info("subject :- " + subject);
    logger.info("jwtToken :- " + jwtToken);

    assertNotNull(jwtToken);
  }

  @Test
  void getJWTTokenUnique() throws Exception {
    logger.info("getJWTToken test");

    String subject = "helloworldunique";

    String jwtToken1 = JSONWebtokenUtil.getJWTToken(subject);
    logger.info("subject :- " + subject);
    logger.info("jwtToken1 :- " + jwtToken1);
    assertNotNull(jwtToken1);

    String jwtToken2 = JSONWebtokenUtil.getJWTToken(subject);
    logger.info("subject :- " + subject);
    logger.info("jwtToken2 :- " + jwtToken2);
    assertNotNull(jwtToken2);

    assertNotEquals(jwtToken1, jwtToken2);
  }
}
