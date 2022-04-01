package com.strato.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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


  // @Test
  // void login() throws Exception{
  //   JsonObject loginResponse = personService.getPerson(1);
  //   logger.info("Person json object");
  //   logger.info(person.toString());
  //   assertEquals(person.getString("name"), "Joe");
  // }

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

}
