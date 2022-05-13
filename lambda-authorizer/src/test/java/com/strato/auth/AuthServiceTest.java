package com.strato.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class AuthServiceTest{

  private static final Logger logger = LoggerFactory.getLogger(AuthServiceTest.class);

  private AuthService authService = new AuthServiceImpl();

  @Test
  void isTokenActive() throws Exception {
    logger.info("isTokenActive test");

    String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6c3lvdm1mZG5mIiwiaWF0IjoxNjUyMjY2NjgyLCJleHAiOjE2NTIzNTMwODh9.h7nSGQeUROBUhWslsYtIctts0MqlH0yQP3NhZQdJqaYgTLCHbv4JuNKR05oWv6Aphdt1y3T6KiRyBdIP3x5R0Q";
    boolean result = authService.isTokenActive(token);
    assertTrue(result);
  }

  @Test
  void isTokenActiveFail() throws Exception {
    logger.info("isTokenActiveFail test");

    String token = "random-token";
    boolean result = authService.isTokenActive(token);
    assertFalse(result);
  }

}
