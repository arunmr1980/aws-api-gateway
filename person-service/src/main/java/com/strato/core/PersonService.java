package com.strato.core;

import javax.json.JsonObject;

public interface PersonService{

  public String addPerson(JsonObject person);

  public JsonObject getPerson(String personId);

}
