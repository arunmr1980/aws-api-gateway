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
  void addPerson() throws Exception {
    logger.info("Adding Person test");

    String personId = personService.addPerson(this.getPersonData());

    assertTrue(personId != null);
  }


  @Test
  void getPerson() throws Exception{
    JsonObject person = personService.getPerson(1);
    logger.info("Person json object");
    logger.info(person.toString());
    assertEquals(person.getString("name"), "Joe");
  }

  private JsonObject getPersonData(){
    String personJSON = "{\"name\":\"Joe\",\"age\":12}";
    JsonReader jsonReader = Json.createReader(new StringReader(personJSON));
    JsonObject personObject = jsonReader.readObject();
    return personObject;
  }

}
