package com.strato.util;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class JSONWebtokenUtil{

  private static final String jwtSecret = "ImIgBaAPVc";
  private static int jwtExpirationInMs = 86400000;

  public static String getJWTToken(String subject){
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

    return Jwts.builder().setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
  }

}
