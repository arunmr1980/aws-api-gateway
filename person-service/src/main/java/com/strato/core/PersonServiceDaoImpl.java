package com.strato.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.json.JsonObject;

import com.strato.db.DBConnector;


class PersonServiceDaoImpl implements PersonServiceDao{

  // private DBConnector dbConnector;
  private Connection connection;

  private static final String USER_NAME = "admin";
  private static final String PASSWORD = "milktreat";
  private static final String DB_END_POINT = "test-db.cnyaitbtsksi.us-east-2.rds.amazonaws.com";

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

  public JsonObject getPerson(String personId) throws Exception{
    if(true){
      throw (new RuntimeException("Method not implemented"));
    }
    return null;
  }

}
