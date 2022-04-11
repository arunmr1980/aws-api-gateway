package com.strato.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.json.JsonObject;
import javax.json.Json;

import com.strato.db.DBConnector;


class PersonServiceDaoImpl implements PersonServiceDao{

  // private DBConnector dbConnector;
  private Connection connection;

  private static final String USER_NAME = System.getenv().get("DB_USER_NAME");
  private static final String PASSWORD = System.getenv().get("DB_PASSWORD");
  private static final String DB_END_POINT = System.getenv().get("DB_END_POINT");

  public PersonServiceDaoImpl(){
    this.connection = DBConnector.createConnectionViaUserPwd(USER_NAME, PASSWORD, DB_END_POINT);
  }

  public String addPerson(JsonObject person) throws Exception{
    String name = person.getString("name");
    int age = person.getInt("age");

    String query = "Insert into TestDB.Person (name, age) values (?,?)";
    PreparedStatement stmt = this.connection.prepareStatement(query);
    stmt.setString(1,name);
    stmt.setInt(2,age);
    stmt.executeUpdate();

    return "";
  }

  public JsonObject getPerson(int personId) throws Exception{
    JsonObject person = null;
    String query = "Select * from TestDB.Person where person_id=?";
    PreparedStatement stmt = this.connection.prepareStatement(query);
    stmt.setInt(1, personId);

    ResultSet results = stmt.executeQuery();
    if(results.next()){
      person = Json.createObjectBuilder().add("person_id",results.getInt("person_id"))
                                         .add("name", results.getString("name"))
                                         .add("age", results.getInt("age"))
                                         .build();
    }
    return person;
  }

}
