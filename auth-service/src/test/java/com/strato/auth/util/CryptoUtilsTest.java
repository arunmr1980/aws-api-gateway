package com.strato.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class CryptoUtilsTest{

  private static final Logger logger = LoggerFactory.getLogger(CryptoUtilsTest.class);

  @Test
  void getEncodedPassword() throws Exception {
    logger.info("getEncodedPassword test");

    String password = "big1secre1t";

    String encodedPassword = CryptoUtils.getEncodedPassword(password);
    logger.info("password :- " + password);
    logger.info("encodedPassword :- " + encodedPassword);

    assertNotNull(encodedPassword);
  }

}
