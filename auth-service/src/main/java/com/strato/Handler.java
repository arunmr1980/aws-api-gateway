package com.strato;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;
import java.util.HashMap;

import javax.json.JsonObject;

import com.strato.util.Util;

import com.strato.auth.AuthService;
import com.strato.auth.AuthServiceImpl;


// Handler value: example.Handler
public class Handler implements RequestHandler<APIGatewayV2ProxyRequestEvent, APIGatewayV2ProxyResponseEvent>{

  Gson gson = new GsonBuilder().setPrettyPrinting().create();

  @Override
  public APIGatewayV2ProxyResponseEvent handleRequest(APIGatewayV2ProxyRequestEvent event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    APIGatewayV2ProxyResponseEvent response = null;

    Util.logEnvironment(event, context, gson);

    JsonObject eventObject = Util.getEventAsJsonObject(event,gson);
    String httpMethod = eventObject.getString("httpMethod").toUpperCase();
    String path = eventObject.getString("path").toUpperCase();
    logger.log("Path :- " + path );

    try{
      AuthService authService = new AuthServiceImpl();
      switch(path){
        case "/USER":
          boolean isSuccess = authService.registerUser(Util.getBodyAsJsonObject(event, gson));
          response = this.getResponse(isSuccess);
          break;
        case "/AUTH/LOGIN":
          JsonObject loginResponse = authService.login(Util.getBodyAsJsonObject(event, gson));
          response = this.getResponse(loginResponse);
          break;
        case "/AUTH/TOKEN":
          JsonObject refreshTokenResponse = authService.refreshToken(Util.getBodyAsJsonObject(event, gson));
          response = this.getResponse(refreshTokenResponse);
          break;
      }
    }catch(Exception ex){
      logger.log("Exception in Handler ");
      logger.log(ex.getMessage());
      ex.printStackTrace();
      throw(new RuntimeException(ex.getMessage()));
    }

    logger.log("Response :-");
    logger.log(gson.toJson(response));
    return response;
  }

  private APIGatewayV2ProxyResponseEvent getResponse(boolean isSuccess){
    String responseString = "{\"isSuccess\": "+ isSuccess+" }";
    return this.getResponse(Util.getAsJsonObject(responseString));
  }

  private APIGatewayV2ProxyResponseEvent getResponse(JsonObject jsonObj){
    APIGatewayV2ProxyResponseEvent response = new APIGatewayV2ProxyResponseEvent();
    response.setIsBase64Encoded(false);
    response.setStatusCode(200);
    HashMap<String, String> headers = new HashMap<String, String>();
    headers.put("Content-Type", "text/json");
    response.setHeaders(headers);
    if(jsonObj != null){
      response.setBody(jsonObj.toString());
    }else{
      response.setBody("{}");
    }

    return response;
  }

}
