package com.strato.auth;

import javax.json.JsonObject;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.json.Json;

import com.strato.util.CryptoUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AuthServiceImpl implements AuthService{

  private static final Logger logger = LogManager.getLogger(AuthServiceImpl.class);

  public boolean registerUser(JsonObject user) throws Exception{
    AuthServiceDao authServiceDao = new AuthServiceDaoImpl();

    JsonObject updatedUser = this.getJsonObjWithEncryptedPassword(user);
    return authServiceDao.registerUser(updatedUser);
  }

  public JsonObject login(JsonObject loginRequest) throws Exception{
    // JsonObject updatedLoginRequest = this.getJsonObjWithEncryptedPassword(loginRequest);
    AuthServiceDao authServiceDao = new AuthServiceDaoImpl();
    int responseCode = AuthService.LOGIN_FAIL;
    String responseMessage = AuthService.LOGIN_FAIL_MSG;

    JsonObject user = authServiceDao.login(loginRequest);
    if(user != null){
      // String encryptedInputPassword = CryptoUtils.getEncodedPassword(loginRequest.getString("password"));
      // String encryptedInputPassword = CryptoUtils.getEncodedPassword(loginRequest.getString("password"));
      // logger.info("Encrypted Input password " + encryptedInputPassword);
      String passwordFromDB = user.getString("password");
      logger.info("Password from DB " + passwordFromDB);
      if(CryptoUtils.checkPasswordMatch(loginRequest.getString("password"), passwordFromDB)){
        logger.info("Passwords matched ...");
        responseCode = AuthService.LOGIN_SUCCESS;
        responseMessage = AuthService.LOGIN_SUCCESS_MSG;
      }else{
        logger.info("Password does not match");
      }
    }else{
      logger.info("User does not exist");
    }
    return this.getLoginResponse(responseCode, responseMessage);
  }


  private JsonObject getLoginResponse(int responseCode, String message){
    JsonBuilderFactory factory = Json.createBuilderFactory(null);
    JsonObject responseObj = factory.createObjectBuilder()
                                .add("responseCode", responseCode)
                                .add("message", message)
                                .build();
    return responseObj;
  }


  private JsonObject getJsonObjWithEncryptedPassword(JsonObject user){
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
