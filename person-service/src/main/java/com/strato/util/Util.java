package com.strato.util;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent;

import com.google.gson.Gson;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Util {

  private static final Logger logger = LogManager.getLogger(Util.class);


  public static void logEnvironment(Object event, Context context, Gson gson)
  {
    // LambdaLogger logger = context.getLogger();
    // log execution details
    logger.info("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
    logger.info("CONTEXT: " + gson.toJson(context));
    // log event details
    logger.info("EVENT: " + gson.toJson(event));
    logger.info("EVENT TYPE: " + event.getClass().toString());
  }

  public static JsonObject getBodyAsJsonObject(APIGatewayV2ProxyRequestEvent event, Gson gson){
    String eventString = gson.toJson(event);
    logger.info("Event String - '"+ eventString + "'");
    JsonReader jsonReader = Json.createReader(new StringReader(eventString));
    logger.info("jsonReader created ...");
    JsonObject eventObject = jsonReader.readObject();
    logger.info("eventObject read ..." + eventObject);
    String bodyString = eventObject.getString("body");
    logger.info("body string ..." + bodyString);

    jsonReader = Json.createReader(new StringReader(bodyString));
    JsonObject bodyObject = jsonReader.readObject();
    logger.info("bodyObject read ..." + bodyString);
    return bodyObject;
  }
}
