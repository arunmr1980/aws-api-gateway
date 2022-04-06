package com.strato.util;

import org.jasypt.util.password.BasicPasswordEncryptor;

public class CryptoUtils{

  public static String getEncodedPassword(String password){
    BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
		String encryptedPassword = passwordEncryptor.encryptPassword(password);
    return encryptedPassword;
  }

}
