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

import com.strato.core.PersonService;
import com.strato.core.PersonServiceImpl;

// Handler value: example.Handler
public class Handler implements RequestHandler<APIGatewayV2ProxyRequestEvent, APIGatewayV2ProxyResponseEvent>{

  Gson gson = new GsonBuilder().setPrettyPrinting().create();

  @Override
  public APIGatewayV2ProxyResponseEvent handleRequest(APIGatewayV2ProxyRequestEvent event, Context context)
  {
    LambdaLogger logger = context.getLogger();

    // log execution details
    Util.logEnvironment(event, context, gson);

    JsonObject eventObject = Util.getEventAsJsonObject(event,gson);
    String httpMethod = eventObject.getString("httpMethod").toUpperCase();

    try{
      PersonService personService = new PersonServiceImpl();
      switch(httpMethod){
        case "POST":
          personService.addPerson(Util.getBodyAsJsonObject(event,gson));
          break;
        case "GET":
          personService.getPerson(1);
          break;
      }
    }catch(Exception ex){
      logger.log("Exception in Handler ");
      logger.log(ex.getMessage());
      ex.printStackTrace();
      throw(new RuntimeException(ex.getMessage()));
    }

    return this.getResponse();
  }



  private APIGatewayV2ProxyResponseEvent getResponse(){
    APIGatewayV2ProxyResponseEvent response = new APIGatewayV2ProxyResponseEvent();
    response.setIsBase64Encoded(false);
    response.setStatusCode(200);
    HashMap<String, String> headers = new HashMap<String, String>();
    headers.put("Content-Type", "text/html");
    response.setHeaders(headers);
    response.setBody("<!DOCTYPE html><html><head><title>AWS Lambda sample</title></head><body>"+
    "<h1>Welcome</h1><p>Page generated by a Lambda function.</p>" +
    "</body></html>");
    return response;
  }

}