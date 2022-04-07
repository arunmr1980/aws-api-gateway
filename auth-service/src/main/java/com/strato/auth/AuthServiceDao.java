package com.strato.auth;

import javax.json.JsonObject;

public interface AuthServiceDao{

  public boolean registerUser(JsonObject person) throws Exception;

  public JsonObject login(JsonObject loginRequest) throws Exception;

  public void updateAuthToken(String userAccountKey, String token) throws Exception;

}
