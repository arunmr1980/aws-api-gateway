package com.strato.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.strato.util.StringUtil;


class AuthServiceTest {
  private static final Logger logger = LoggerFactory.getLogger(AuthServiceTest.class);

  AuthService authService = new AuthServiceImpl();

  @Test
  void testRefreshTokenFlow() throws Exception{
    // #1 Register user
    JsonObject user = this.getUserData();
    String userName = user.getString("username");
    String password = user.getString("password");
    boolean success = authService.registerUser(user);
    assertTrue(success);

    // #2 Login user
    JsonObject loginUser = this.getLoginUser(userName, password);
    JsonObject loginResponse = authService.login(loginUser);
    String accessToken = loginResponse.getString("access_token");
    String refreshToken = loginResponse.getString("refresh_token");
    assertEquals(loginResponse.getString("response_code"), AuthService.LOGIN_SUCCESS);
    assertNotNull(accessToken);
    assertNotNull(refreshToken);
    assertNotNull(loginResponse.getString("device_key"));

    //#3 Refresh token
    JsonObject refreshRequest =  this.getRefreshTokenRequest(accessToken, refreshToken);
    JsonObject refreshResponse = authService.refreshToken(refreshRequest);
    assertNotNull(refreshResponse);
    assertTrue(refreshResponse.containsKey("access_token"), "access_token not present in refresh response");
    assertTrue(refreshResponse.containsKey("refresh_token"), "refresh_token not present in refresh response");
    String accessTokenNew = refreshResponse.getString("access_token");
    String refreshTokenNew = refreshResponse.getString("refresh_token");
    assertEquals(refreshResponse.getString("response_code"), AuthService.REFRESH_TOKEN_SUCCESS);
    assertNotNull(accessTokenNew);
    assertNotNull(refreshTokenNew);
    logger.info("Access Token login     - " + accessToken);
    logger.info("Access Token refreshed - " + accessTokenNew);
    assertNotEquals(accessToken, accessTokenNew);
    assertNotEquals(refreshToken, refreshTokenNew);

  }


  @Test
  void refreshTokenFailure() throws Exception{
    String accessToken = "randontoken****";
    String refreshToken = "randomrefresh";
    JsonObject refreshRequest =  this.getRefreshTokenRequest(accessToken, refreshToken);
    JsonObject refreshResponse = authService.refreshToken(refreshRequest);
    assertNotNull(refreshResponse);
    assertEquals(refreshResponse.getString("response_code"), AuthService.REFRESH_TOKEN_FAIL);

  }

  @Test
  void registerUser() throws Exception {
    logger.info("registerUser test");

    boolean success = authService.registerUser(this.getUserData());

    assertTrue(success);
  }


  @Test
  void login() throws Exception{
    JsonObject loginRequest = this.getLoginUserSuccess();
    JsonObject loginResponse = authService.login(loginRequest);

    assertEquals(loginResponse.getString("response_code"), AuthService.LOGIN_SUCCESS);
    assertNotNull(loginResponse.getString("access_token"));
    assertNotNull(loginResponse.getString("refresh_token"));
    assertNotNull(loginResponse.getString("device_key"));

  }


  @Test
  void loginWithDevice() throws Exception{
    JsonObject loginRequest = this.getLoginUserSuccessWithDeviceKey();
    String inputDeviceKey = loginRequest.getString("device_key");
    JsonObject loginResponse = authService.login(loginRequest);

    assertEquals(loginResponse.getString("response_code"), AuthService.LOGIN_SUCCESS);
    assertNotNull(loginResponse.getString("access_token"));
    assertNotNull(loginResponse.getString("refresh_token"));
    assertNotNull(loginResponse.getString("device_key"));
    assertEquals(inputDeviceKey, loginResponse.getString("device_key"));

  }


  @Test
  void loginFail() throws Exception{
    JsonObject loginRequest = this.getLoginUserFail();
    JsonObject loginResponse = authService.login(loginRequest);

    assertEquals(loginResponse.getString("response_code"), AuthService.LOGIN_FAIL);
  }


  private JsonObject getUserData(){
    StringBuilder userJson = new StringBuilder();
    userJson.append("{");
    userJson.append("\"email\":\"testemail@abc.in\",");
    userJson.append("\"mobile\":\"6783456729\",");
    userJson.append("\"username\":\"testuser-" + StringUtil.getRandomString() + "\",");
    userJson.append("\"firstname\":\"Jason\",");
    userJson.append("\"lastname\":\"Job\",");
    userJson.append("\"password\":\"abc123\"");
    userJson.append("}");
    JsonReader jsonReader = Json.createReader(new StringReader(userJson.toString()));
    JsonObject userObject = jsonReader.readObject();
    return userObject;
  }

  private JsonObject getLoginUser(String userName, String password){
    return this.getLoginUser(userName, password, null);
  }

  private JsonObject getLoginUser(String userName, String password, String deviceKey){
    StringBuilder userJson = new StringBuilder();
    userJson.append("{");
    userJson.append("\"username\":\"" + userName + "\",");
    if(deviceKey != null){
      userJson.append("\"device_key\":\"" + deviceKey + "\",");
    }
    userJson.append("\"password\":\"" + password + "\"");
    userJson.append("}");
    JsonReader jsonReader = Json.createReader(new StringReader(userJson.toString()));
    JsonObject userObject = jsonReader.readObject();
    return userObject;
  }


  private JsonObject getLoginUserSuccess(){
    return this.getLoginUser("testuser-moxtmitzbu", "abc123");
  }


  private JsonObject getLoginUserSuccessWithDeviceKey(){
    return this.getLoginUser("testuser-moxtmitzbu", "abc123", "kxmopjbqqncwfgtn");
  }


  private JsonObject getLoginUserFail(){
    return this.getLoginUser("testuser-nqqowxpkqy", "abc123");
  }


  private JsonObject getRefreshTokenRequest(String accessToken, String refreshToken){
    StringBuilder userJson = new StringBuilder();
    userJson.append("{");
    userJson.append("\"grant_type\":\"refresh_token\",");
    userJson.append("\"refresh_token\":\"" + refreshToken + "\",");
    userJson.append("\"access_token\":\"" + accessToken + "\"");
    userJson.append("}");
    JsonReader jsonReader = Json.createReader(new StringReader(userJson.toString()));
    JsonObject userObject = jsonReader.readObject();
    return userObject;
  }

}
