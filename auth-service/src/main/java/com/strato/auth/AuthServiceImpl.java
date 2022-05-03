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
import com.strato.util.DateUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AuthServiceImpl implements AuthService{

  private static final Logger logger = LogManager.getLogger(AuthServiceImpl.class);

  private AuthServiceDao authServiceDao = new AuthServiceDaoImpl();

  public JsonObject refreshToken(JsonObject refreshRequest) throws Exception{
    return null;
  }


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
        Map<String, String> tokensMap = this.generateAndUpdateTokens(user, loginRequest);
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


  private Map<String, String> generateAndUpdateTokens(JsonObject user, JsonObject loginRequest) throws Exception{
    Map<String, String> tokensMap = new HashMap<String, String>();
    String userAccountKey = user.getString("user_account_key");
    String accessToken = JSONWebtokenUtil.getJWTToken(userAccountKey);
    logger.info("JSON Token :- " + accessToken);
    String refreshToken = StringUtil.getGeneratedToken();
    logger.info("Refresh Token :- " + refreshToken);
    this.updateTokens(accessToken, refreshToken, userAccountKey, loginRequest);

    tokensMap.put("accessToken", accessToken);
    tokensMap.put("refreshToken", refreshToken);
    return tokensMap;
  }


  private void updateTokens(String accessToken, String refreshToken, String userAccountKey, JsonObject loginRequest) throws Exception{
    long accessTokenExpiryDateTime = DateUtil.getNowInMilliSeconds() + ACCESS_TOKEN_EXPIRY_MS;
    long refreshTokenExpiryDateTime = DateUtil.getNowInMilliSeconds() + REFRESH_TOKEN_EXPIRY_MS;

    logger.info("Access token expiry - " + accessTokenExpiryDateTime);
    logger.info("Refresh token expiry - " + refreshTokenExpiryDateTime);

    String deviceKey = null;
    if(loginRequest.containsKey("device_key")){
      deviceKey = loginRequest.getString("device_key");
    }else{
      final int KEY_LENGTH = 16;
      deviceKey = StringUtil.getRandomString(KEY_LENGTH);
    }
    String deviceName = "Not Provided";
    if(loginRequest.containsKey("device_name")){
      deviceName = loginRequest.getString("device_name");
    }

    logger.info("Device [" + deviceKey + "] " + deviceName);

    this.authServiceDao.updateAuthTokens( userAccountKey,
                                          accessToken,
                                          accessTokenExpiryDateTime,
                                          refreshToken,
                                          refreshTokenExpiryDateTime,
                                          deviceKey,
                                          deviceName
                                          );
  }


  private JsonObject getLoginResponse(int responseCode, String message, String accessToken, String refreshToken){
    JsonBuilderFactory factory = Json.createBuilderFactory(null);
    JsonObjectBuilder objBuilder = factory.createObjectBuilder();
    objBuilder.add("response_code", responseCode)
              .add("message", message);
    if(accessToken != null){
      objBuilder.add("access_token", accessToken);
    }
    if(refreshToken != null){
      objBuilder.add("refresh_token", refreshToken);
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
