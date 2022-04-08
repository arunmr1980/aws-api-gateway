package com.strato.util;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JSONWebtokenUtil{

  private static final Logger logger = LogManager.getLogger(JSONWebtokenUtil.class);

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

  public static boolean validateToken(String token) throws Exception {
    try {
      Jws<Claims> claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);

      if (claims.getBody().getExpiration().before(new Date())) {
        throw new Exception("Token expired");
      }
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      logger.error(e);
      throw new Exception("Expired or invalid JWT token" + e);
    }
  }

}
