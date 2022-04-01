package com.strato.auth;

import javax.json.JsonObject;


public interface AuthService{


  public boolean registerUser(JsonObject person) throws Exception;

  public JsonObject login(JsonObject loginRequest) throws Exception;


}
