package com.strato.auth;

import javax.json.JsonObject;

public interface AuthServiceDao{

  public boolean registerUser(JsonObject person) throws Exception;

  public JsonObject login(JsonObject loginRequest) throws Exception;

  public void updateAuthTokens(String userAccountKey,
                               String accessToken,
                               long accessTokenExpiryDateTime,
                               String refreshToken,
                               long refreshTokenExpiryDateTime,
                               String deviceKey,
                               String deviceName) throws Exception;

  public JsonObject getUserDevice(String accessToken, String refreshToken) throws Exception;

  public JsonObject getUserDevice(String accessToken) throws Exception;

}
