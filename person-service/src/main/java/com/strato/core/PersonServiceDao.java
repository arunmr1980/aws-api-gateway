package com.strato.core;

import javax.json.JsonObject;

public interface PersonServiceDao{

  public String addPerson(JsonObject person) throws Exception;

  public JsonObject getPerson(int personId) throws Exception;

}
