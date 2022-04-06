package com.strato.auth;

import javax.json.JsonObject;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.json.Json;

import com.strato.util.CryptoUtils;

public class AuthServiceImpl implements AuthService{

  public boolean registerUser(JsonObject user) throws Exception{
    AuthServiceDao authServiceDao = new AuthServiceDaoImpl();

    JsonObject updatedUser = this.getUpdatedUserObj(user);
    return authServiceDao.registerUser(updatedUser);
  }

  public JsonObject login(JsonObject loginRequest) throws Exception{
    throw (new RuntimeException("Method not implemented"));
  }

  private JsonObject getUpdatedUserObj(JsonObject user){
    JsonBuilderFactory jsonBuilderFactory = Json.createBuilderFactory(null);
    JsonObjectBuilder jsonObjectBuilder = jsonBuilderFactory.createObjectBuilder();
    for(String key : user.keySet()) {
       jsonObjectBuilder.add(key, user.get(key));
    }
    String password = user.getString("password");
    String encryptedPassword = CryptoUtils.getEncodedPassword(password);
    jsonObjectBuilder.add("password", encryptedPassword);
    return jsonObjectBuilder.build();
  }

}
