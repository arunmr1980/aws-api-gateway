package com.strato.auth;

import javax.json.JsonObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AuthServiceImpl implements AuthService{

  private static final Logger logger = LogManager.getLogger(AuthServiceImpl.class);

  private AuthServiceDao authServiceDao = new AuthServiceDaoImpl();

  public boolean isTokenActive(String accessToken) throws Exception{
    logger.info("isTokenActive() token " + accessToken);
    JsonObject userDevice = this.authServiceDao.getUserDevice(accessToken);
    if(userDevice == null){
      logger.info("User device record not present for access token");
      return false;
    }
    return true;
  }

}
