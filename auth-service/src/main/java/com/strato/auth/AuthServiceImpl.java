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
    JsonObject response = null;
    String refreshToken = refreshRequest.getString("refresh_token");
    String accessToken = refreshRequest.getString("access_token");
    JsonObject userDevice = this.authServiceDao.getUserDevice(accessToken, refreshToken);
    if(userDevice != null){
      logger.info("Device record found for refresh token");
      long expiryDateTime = userDevice.getJsonNumber("refresh_token_expiry_datetime").longValue();
      if (!DateUtil.isExpired(expiryDateTime)){
        logger.info("Refreshing tokens ...");
        Map<String, String> tokensMap = this.generateAndUpdateTokensForTokenRefresh(userDevice);
        String accessTokenNew = tokensMap.get("access_token") + "";
        String refreshTokenNew = tokensMap.get("refresh_token") + "";
        response = this.getResponse(AuthService.REFRESH_TOKEN_SUCCESS,
                                    AuthService.REFRESH_TOKEN_SUCCESS_MSG,
                                    accessTokenNew,
                                    refreshTokenNew,
                                    null //deviceKey
                                    );
      }else{
        logger.info("Refresh token has expired");
        response = this.getResponse(AuthService.REFRESH_TOKEN_EXPIRED,
                                    AuthService.REFRESH_TOKEN_EXPIRED_MSG,
                                    null,  // access token
                                    null, // refresh token
                                    null //deviceKey
                                    );
      }
    }else{
      logger.info("No user device record exist for the tokens");
      response = this.getResponse(AuthService.REFRESH_TOKEN_FAIL,
                                  AuthService.REFRESH_TOKEN_FAIL_MSG,
                                  null,  // access token
                                  null, // refresh token
                                  null //deviceKey
                                  );
    }
    return response;
  }


  public boolean registerUser(JsonObject user) throws Exception{
    JsonObject updatedUser = this.getJsonObjWithEncryptedPassword(user);
    return this.authServiceDao.registerUser(updatedUser);
  }


  public JsonObject login(JsonObject loginRequest) throws Exception{
    String responseCode = AuthService.LOGIN_FAIL;
    String responseMessage = AuthService.LOGIN_FAIL_MSG;
    String accessToken = null;
    String refreshToken = null;
    String deviceKey = null;

    JsonObject user = this.authServiceDao.login(loginRequest);
    if(user != null){
      String passwordFromDB = user.getString("password");
      logger.info("Password from DB " + passwordFromDB);
      if(CryptoUtils.checkPasswordMatch(loginRequest.getString("password"), passwordFromDB)){
        logger.info("Passwords matched ...");
        Map<String, String> tokensMap = this.generateAndUpdateTokensForLogin(user, loginRequest);
        accessToken = tokensMap.get("access_token") + "";
        refreshToken = tokensMap.get("refresh_token") + "";
        deviceKey = tokensMap.get("device_key") + "";
        responseCode = AuthService.LOGIN_SUCCESS;
        responseMessage = AuthService.LOGIN_SUCCESS_MSG;
      }else{
        logger.info("Password does not match");
      }
    }else{
      logger.info("User does not exist");
    }
    return this.getResponse(responseCode, responseMessage, accessToken, refreshToken, deviceKey);
  }


  private Map<String, String> generateAndUpdateTokensForTokenRefresh(JsonObject userDevice) throws Exception{
    String userAccountKey = userDevice.getString("user_account_key");
    String deviceKey = userDevice.getString("device_key");
    String deviceName = null;// Device name is not updated in token refresh
    return this.generateAndUpdateTokens(userAccountKey, deviceKey, deviceName);
  }


  private Map<String, String> generateAndUpdateTokensForLogin(JsonObject user, JsonObject loginRequest) throws Exception{
    String userAccountKey = user.getString("user_account_key");
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
    return this.generateAndUpdateTokens(userAccountKey, deviceKey, deviceName);
  }


  private Map<String, String> generateAndUpdateTokens(String userAccountKey, String deviceKey, String deviceName) throws Exception{
    Map<String, String> tokensMap = new HashMap<String, String>();
    String accessToken = JSONWebtokenUtil.getJWTToken(userAccountKey);
    logger.info("JSON Token :- " + accessToken);
    String refreshToken = StringUtil.getGeneratedToken();
    logger.info("Refresh Token :- " + refreshToken);
    this.updateDeviceAndTokens(accessToken, refreshToken, userAccountKey, deviceKey, deviceName);

    tokensMap.put("access_token", accessToken);
    tokensMap.put("refresh_token", refreshToken);
    tokensMap.put("device_key", deviceKey);

    return tokensMap;
  }


  private String updateDeviceAndTokens(String accessToken,
                                       String refreshToken,
                                       String userAccountKey,
                                       String deviceKey,
                                       String deviceName) throws Exception{
    long accessTokenExpiryDateTime = DateUtil.getNowInMilliSeconds() + ACCESS_TOKEN_EXPIRY_MS;
    long refreshTokenExpiryDateTime = DateUtil.getNowInMilliSeconds() + REFRESH_TOKEN_EXPIRY_MS;

    logger.info("Access token expiry - " + accessTokenExpiryDateTime);
    logger.info("Refresh token expiry - " + refreshTokenExpiryDateTime);
    logger.info("Device [" + deviceKey + "] " + deviceName);

    this.authServiceDao.updateAuthTokens( userAccountKey,
                                          accessToken,
                                          accessTokenExpiryDateTime,
                                          refreshToken,
                                          refreshTokenExpiryDateTime,
                                          deviceKey,
                                          deviceName
                                          );
    return deviceKey;
  }


  private JsonObject getResponse(String responseCode, String message, String accessToken, String refreshToken, String deviceKey){
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
    if(deviceKey != null){
      objBuilder.add("device_key", deviceKey);
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
