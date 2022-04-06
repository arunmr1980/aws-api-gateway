package com.strato.auth;

import javax.json.JsonObject;


public interface AuthService{

  public static final int LOGIN_FAIL=100;
  public static final String LOGIN_FAIL_MSG="Username or password is incorrect";
  public static final int LOGIN_SUCCESS=0;
  public static final String LOGIN_SUCCESS_MSG="Login success";



  public boolean registerUser(JsonObject person) throws Exception;

  public JsonObject login(JsonObject loginRequest) throws Exception;


}
