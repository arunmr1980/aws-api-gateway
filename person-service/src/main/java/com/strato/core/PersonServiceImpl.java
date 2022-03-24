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

  public JsonObject getPerson(int personId) throws Exception{

    return this.personServiceDao.getPerson(personId);
  }

}
