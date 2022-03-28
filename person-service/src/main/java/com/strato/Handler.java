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
    APIGatewayV2ProxyResponseEvent response = null;

    // log execution details
    Util.logEnvironment(event, context, gson);

    JsonObject eventObject = Util.getEventAsJsonObject(event,gson);
    String httpMethod = eventObject.getString("httpMethod").toUpperCase();
    String path = eventObject.getString("path").toUpperCase();

    try{
      PersonService personService = new PersonServiceImpl();
      switch(httpMethod){
        case "POST":
          personService.addPerson(Util.getBodyAsJsonObject(event,gson));
          response = this.getResponse(null);
          break;
        case "GET":
          int personId = Integer.parseInt(path.substring(path.lastIndexOf("/")+1));
          JsonObject person = personService.getPerson(personId);
          response = this.getResponse(person);
          break;
      }
    }catch(Exception ex){
      logger.log("Exception in Handler ");
      logger.log(ex.getMessage());
      ex.printStackTrace();
      throw(new RuntimeException(ex.getMessage()));
    }

    return response;
  }



  private APIGatewayV2ProxyResponseEvent getResponse(JsonObject jsonObj){
    APIGatewayV2ProxyResponseEvent response = new APIGatewayV2ProxyResponseEvent();
    response.setIsBase64Encoded(false);
    response.setStatusCode(200);
    HashMap<String, String> headers = new HashMap<String, String>();
    headers.put("Content-Type", "text/json");
    response.setHeaders(headers);
    if(jsonObj != null){
      response.setBody(gson.toJson(jsonObj));
    }else{
      response.setBody("{}");
    }

    return response;
  }

}
