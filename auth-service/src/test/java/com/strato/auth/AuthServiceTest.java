package com.strato.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
  void registerUser() throws Exception {
    logger.info("registerUser test");

    boolean success = authService.registerUser(this.getUserData());

    assertTrue(success);
  }


  @Test
  void login() throws Exception{
    JsonObject loginRequest = this.getLoginUserSuccess();
    JsonObject loginResponse = authService.login(loginRequest);

    assertEquals(loginResponse.getInt("response_code"), AuthService.LOGIN_SUCCESS);
    assertNotNull(loginResponse.getString("access_token"));
    assertNotNull(loginResponse.getString("refresh_token"));
    assertNotNull(loginResponse.getString("device_key"));

  }


  @Test
  void loginWithDevice() throws Exception{
    JsonObject loginRequest = this.getLoginUserSuccessWithDeviceKey();
    String inputDeviceKey = loginRequest.getString("device_key");
    JsonObject loginResponse = authService.login(loginRequest);

    assertEquals(loginResponse.getInt("response_code"), AuthService.LOGIN_SUCCESS);
    assertNotNull(loginResponse.getString("access_token"));
    assertNotNull(loginResponse.getString("refresh_token"));
    assertNotNull(loginResponse.getString("device_key"));
    assertEquals(inputDeviceKey, loginResponse.getString("device_key"));

  }


  @Test
  void loginFail() throws Exception{
    JsonObject loginRequest = this.getLoginUserFail();
    JsonObject loginResponse = authService.login(loginRequest);

    assertEquals(loginResponse.getInt("response_code"), AuthService.LOGIN_FAIL);
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


  private JsonObject getLoginUserSuccess(){
    StringBuilder userJson = new StringBuilder();
    userJson.append("{");
    userJson.append("\"username\":\"testuser-moxtmitzbu\",");
    userJson.append("\"password\":\"abc123\"");
    userJson.append("}");
    JsonReader jsonReader = Json.createReader(new StringReader(userJson.toString()));
    JsonObject userObject = jsonReader.readObject();
    return userObject;
  }


  private JsonObject getLoginUserSuccessWithDeviceKey(){
    StringBuilder userJson = new StringBuilder();
    userJson.append("{");
    userJson.append("\"username\":\"testuser-moxtmitzbu\",");
    userJson.append("\"password\":\"abc123\",");
    userJson.append("\"device_key\":\"kxmopjbqqncwfgtn\"");
    userJson.append("}");
    JsonReader jsonReader = Json.createReader(new StringReader(userJson.toString()));
    JsonObject userObject = jsonReader.readObject();
    return userObject;
  }


  private JsonObject getLoginUserFail(){
    StringBuilder userJson = new StringBuilder();
    userJson.append("{");
    userJson.append("\"username\":\"testuser-nqqowxpkqy\",");
    userJson.append("\"password\":\"abc\"");
    userJson.append("}");
    JsonReader jsonReader = Json.createReader(new StringReader(userJson.toString()));
    JsonObject userObject = jsonReader.readObject();
    return userObject;
  }

}
