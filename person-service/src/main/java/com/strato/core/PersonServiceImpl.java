package com.strato.core;

import javax.json.JsonObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PersonServiceImpl implements PersonService{

  private static final Logger logger = LogManager.getLogger(PersonServiceImpl.class);

  private PersonServiceDao personServiceDao;

  public PersonServiceImpl(){
    this.personServiceDao = new PersonServiceDaoImpl();
  }

  public String addPerson(JsonObject person) throws Exception{
    logger.info("Adding person ");
    return this.personServiceDao.addPerson(person);

  }

  public JsonObject getPerson(int personId) throws Exception{

    return this.personServiceDao.getPerson(personId);
  }

}
