package com.strato.util;

import java.util.Random;
import java.time.Instant;
import java.util.UUID;

public class StringUtil{

  public static String getRandomString() {
    return getRandomString(10);
  }

  public static String getRandomString(int targetStringLength) {
      int leftLimit = 97; // letter 'a'
      int rightLimit = 122; // letter 'z'
      // int targetStringLength = 10;
      Random random = new Random();
      StringBuilder buffer = new StringBuilder(targetStringLength);

      for (int i = 0; i < targetStringLength; i++) {
          int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
          buffer.append((char) randomLimitedInt);
      }
      return buffer.toString();
  }

  public static String getGeneratedToken(){
    StringBuilder token = new StringBuilder();
    long currentTimeInMilisecond = Instant.now().toEpochMilli();
    return token.append(currentTimeInMilisecond)
                .append("-")
                .append(UUID.randomUUID().toString())
                .toString();
  }

}
