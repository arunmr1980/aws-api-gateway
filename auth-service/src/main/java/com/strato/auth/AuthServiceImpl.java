package com.strato.auth;

import javax.json.JsonObject;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.json.Json;

import java.util.Map;
import java.util.HashMap;

import com.strato.util.CryptoUtils;
import com.strato.util.JSONWebtokenUtil;
import com.strato.util.StringUtil;

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
    String accessToken = null;
    String refreshToken = null;

    JsonObject user = this.authServiceDao.login(loginRequest);
    if(user != null){
      String passwordFromDB = user.getString("password");
      logger.info("Password from DB " + passwordFromDB);
      if(CryptoUtils.checkPasswordMatch(loginRequest.getString("password"), passwordFromDB)){
        logger.info("Passwords matched ...");
        Map<String, String> tokensMap = this.generateAndUpdateTokens(user);
        accessToken = tokensMap.get("accessToken") + "";
        refreshToken = tokensMap.get("refreshToken") + "";
        responseCode = AuthService.LOGIN_SUCCESS;
        responseMessage = AuthService.LOGIN_SUCCESS_MSG;
      }else{
        logger.info("Password does not match");
      }
    }else{
      logger.info("User does not exist");
    }
    return this.getLoginResponse(responseCode, responseMessage, accessToken, refreshToken);
  }


  private Map<String, String> generateAndUpdateTokens(JsonObject user) throws Exception{
    Map<String, String> tokensMap = new HashMap<String, String>();
    String userAccountKey = user.getString("useraccountkey");
    String accessToken = JSONWebtokenUtil.getJWTToken(userAccountKey);
    logger.info("JSON Token :- " + accessToken);
    String refreshToken = StringUtil.getGeneratedToken();
    logger.info("Refresh Token :- " + refreshToken);
    this.authServiceDao.updateAuthToken(userAccountKey, accessToken);

    tokensMap.put("accessToken", accessToken);
    tokensMap.put("refreshToken", refreshToken);
    return tokensMap;
  }


  private JsonObject getLoginResponse(int responseCode, String message, String accessToken, String refreshToken){
    JsonBuilderFactory factory = Json.createBuilderFactory(null);
    JsonObjectBuilder objBuilder = factory.createObjectBuilder();
    objBuilder.add("responseCode", responseCode)
              .add("message", message);
    if(accessToken != null){
      objBuilder.add("accessToken", accessToken);
    }
    if(refreshToken != null){
      objBuilder.add("refreshToken", refreshToken);
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
