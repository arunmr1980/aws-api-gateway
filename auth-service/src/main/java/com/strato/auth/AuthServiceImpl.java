package com.strato.auth;

import javax.json.JsonObject;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.json.Json;

import com.strato.util.CryptoUtils;
import com.strato.util.JSONWebtokenUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AuthServiceImpl implements AuthService{

  private static final Logger logger = LogManager.getLogger(AuthServiceImpl.class);

  private AuthServiceDao authServiceDao = new AuthServiceDaoImpl();

  public boolean registerUser(JsonObject user) throws Exception{
    JsonObject updatedUser = this.getJsonObjWithEncryptedPassword(user);
    return this.authServiceDao.registerUser(updatedUser);
  }

  public JsonObject login(JsonObject loginRequest) throws Exception{
    int responseCode = AuthService.LOGIN_FAIL;
    String responseMessage = AuthService.LOGIN_FAIL_MSG;
    String authToken = null;

    JsonObject user = this.authServiceDao.login(loginRequest);
    if(user != null){
      String passwordFromDB = user.getString("password");
      logger.info("Password from DB " + passwordFromDB);
      if(CryptoUtils.checkPasswordMatch(loginRequest.getString("password"), passwordFromDB)){
        logger.info("Passwords matched ...");
        authToken = this.generateAndUpdateAuthToken(user);
        responseCode = AuthService.LOGIN_SUCCESS;
        responseMessage = AuthService.LOGIN_SUCCESS_MSG;
      }else{
        logger.info("Password does not match");
      }
    }else{
      logger.info("User does not exist");
    }
    return this.getLoginResponse(responseCode, responseMessage, authToken);
  }


  private String generateAndUpdateAuthToken(JsonObject user) throws Exception{
    String userAccountKey = user.getString("useraccountkey");
    String authToken = JSONWebtokenUtil.getJWTToken(userAccountKey);
    logger.info("JSON Token :- " + authToken);
    this.authServiceDao.updateAuthToken(userAccountKey, authToken);
    return authToken;
  }



  private JsonObject getLoginResponse(int responseCode, String message, String authToken){
    JsonBuilderFactory factory = Json.createBuilderFactory(null);
    JsonObjectBuilder objBuilder = factory.createObjectBuilder();
    objBuilder.add("responseCode", responseCode)
              .add("message", message);
    if(authToken != null){
      objBuilder.add("authToken", authToken);
    }
    JsonObject responseObj = objBuilder.build();
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
