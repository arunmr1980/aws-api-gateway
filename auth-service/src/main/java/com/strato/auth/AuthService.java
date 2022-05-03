package com.strato.auth;

import javax.json.JsonObject;


public interface AuthService{

  public static final int LOGIN_FAIL=100;
  public static final String LOGIN_FAIL_MSG="Username or password is incorrect";
  public static final int LOGIN_SUCCESS=0;
  public static final String LOGIN_SUCCESS_MSG="Login success";

  public static final long ACCESS_TOKEN_EXPIRY_MS = Long.parseLong(System.getenv().getOrDefault("ACCESS_TOKEN_EXPIRY_MS","3600000"));
  public static final long REFRESH_TOKEN_EXPIRY_MS = Long.parseLong(System.getenv().getOrDefault("REFRESH_TOKEN_EXPIRY_MS","31536000000"));


  public boolean registerUser(JsonObject person) throws Exception;

  public JsonObject login(JsonObject loginRequest) throws Exception;

  public JsonObject refreshToken(JsonObject refreshRequest) throws Exception;


}
