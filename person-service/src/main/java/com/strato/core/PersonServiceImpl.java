package com.strato.core;

import javax.json.JsonObject;

public class PersonServiceImpl implements PersonService{

  private PersonServiceDao personServiceDao;

  public PersonServiceImpl(){
    this.personServiceDao = new PersonServiceDaoImpl();
  }

  public String addPerson(JsonObject person) throws Exception{

    return this.personServiceDao.addPerson(person);

  }

  public JsonObject getPerson(String personId) throws Exception{
    if(true){
      throw (new RuntimeException("Method not implemented"));
    }
    return null;
  }

}
