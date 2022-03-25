package com.strato.util;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent;

import com.google.gson.Gson;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import java.io.StringReader;


public class Util {

  public static void logEnvironment(Object event, Context context, Gson gson)
  {
    LambdaLogger logger = context.getLogger();
    // log execution details
    logger.log("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
    logger.log("CONTEXT: " + gson.toJson(context));
    // log event details
    logger.log("EVENT: " + gson.toJson(event));
    logger.log("EVENT TYPE: " + event.getClass().toString());
  }

  public static JsonObject getBodyAsJsonObject(APIGatewayV2ProxyRequestEvent event, Gson gson){
    String eventString = gson.toJson(event);
    JsonReader jsonReader = Json.createReader(new StringReader(eventString));
    JsonObject eventObject = jsonReader.readObject();
    JsonObject bodyObject = eventObject.getJsonObject("body");
    return bodyObject;
  }
}
