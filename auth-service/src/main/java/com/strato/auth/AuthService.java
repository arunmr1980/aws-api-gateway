package com.strato.auth;

import javax.json.JsonObject;


public interface AuthService{

  public static final String LOGIN_FAIL="login_fail";
  public static final String LOGIN_FAIL_MSG="Username or password is incorrect";

  public static final String LOGIN_SUCCESS="login_success";
  public static final String LOGIN_SUCCESS_MSG="Login success";

  public static final String REFRESH_TOKEN_SUCCESS="refresh_token_success";
  public static final String REFRESH_TOKEN_SUCCESS_MSG="Token refresh success";

  public static final String REFRESH_TOKEN_FAIL="refresh_token_fail";
  public static final String REFRESH_TOKEN_FAIL_MSG="Token refresh fail";

  public static final String REFRESH_TOKEN_EXPIRED="refresh_token_expired";
  public static final String REFRESH_TOKEN_EXPIRED_MSG="Token refresh expired";

  public static final long ACCESS_TOKEN_EXPIRY_MS = Long.parseLong(System.getenv().getOrDefault("ACCESS_TOKEN_EXPIRY_MS","3600000"));
  public static final long REFRESH_TOKEN_EXPIRY_MS = Long.parseLong(System.getenv().getOrDefault("REFRESH_TOKEN_EXPIRY_MS","31536000000"));


  public boolean registerUser(JsonObject person) throws Exception;

  public JsonObject login(JsonObject loginRequest) throws Exception;

  public JsonObject refreshToken(JsonObject refreshRequest) throws Exception;

}
