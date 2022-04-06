package com.strato.auth;

import javax.json.JsonObject;

public class AuthServiceImpl implements AuthService{

  public boolean registerUser(JsonObject user) throws Exception{
    AuthServiceDao authServiceDao = new AuthServiceDaoImpl();
    return authServiceDao.registerUser(user);
  }

  public JsonObject login(JsonObject loginRequest) throws Exception{
    throw (new RuntimeException("Method not implemented"));
  }

}
