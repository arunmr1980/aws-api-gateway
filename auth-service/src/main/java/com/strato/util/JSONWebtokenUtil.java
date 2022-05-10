package com.strato.util;

import java.util.Date;
import java.util.Random;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class JSONWebtokenUtil{

  private static final Logger logger = LogManager.getLogger(JSONWebtokenUtil.class);

  private static final String jwtSecret = "ImIgBaAPVc";
  private static long jwtExpirationInMs = 86400000;

  public static String getJWTToken(String subject){
    Date now = new Date();
    Random r = new Random();
    // random number added to avoid identical tokens being created when generated
    // below 1000 ms gap
    long randomNum = r.nextInt(10000);
    long expiryDateInMs = now.getTime() + jwtExpirationInMs + randomNum;
    Date expiryDate = new Date(expiryDateInMs);

    return Jwts.builder().setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
  }

}
