package com.strato.core;

import javax.json.JsonObject;

public interface PersonService{

  public String addPerson(JsonObject person) throws Exception;

  public JsonObject getPerson(int personId) throws Exception;

}
