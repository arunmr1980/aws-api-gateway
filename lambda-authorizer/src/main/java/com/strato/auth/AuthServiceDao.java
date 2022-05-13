package com.strato.auth;

import javax.json.JsonObject;

public interface AuthServiceDao{

  public JsonObject getUserDevice(String accessToken) throws Exception;

}
