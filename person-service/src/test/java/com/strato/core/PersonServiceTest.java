package com.strato.core;

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

class PersonServiceTest {
  private static final Logger logger = LoggerFactory.getLogger(PersonServiceTest.class);

  PersonService personService = new PersonServiceImpl();

  @Test
  void addPerson() {
    logger.info("Adding Person test");

    String personId = personService.addPerson(this.getPerson());

    assertTrue(personId != null);
  }

  private JsonObject getPerson(){
    String personJSON = "{\"name\":\"Joe\"}";
    JsonReader jsonReader = Json.createReader(new StringReader(personJSON));
    JsonObject personObject = jsonReader.readObject();
    return personObject;
  }

}
