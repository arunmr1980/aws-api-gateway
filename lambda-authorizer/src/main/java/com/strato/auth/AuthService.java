package com.strato.auth;


public interface AuthService{

  public boolean isTokenActive(String accessToken) throws Exception;
}
