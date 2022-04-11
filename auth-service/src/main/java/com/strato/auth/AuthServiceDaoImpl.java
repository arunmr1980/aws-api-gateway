package com.strato.auth;

import javax.json.JsonObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.json.JsonObject;
import javax.json.Json;

import com.strato.db.DBConnector;

import com.strato.util.StringUtil;

class AuthServiceDaoImpl implements AuthServiceDao{

  private Connection connection;

  private static final String USER_NAME = System.getenv().get("DB_USER_NAME");
  private static final String PASSWORD = System.getenv().get("DB_PASSWORD");
  private static final String DB_END_POINT = System.getenv().get("DB_END_POINT");

  public AuthServiceDaoImpl(){
    this.connection = DBConnector.createConnectionViaUserPwd(USER_NAME, PASSWORD, DB_END_POINT);
  }


  public boolean registerUser(JsonObject user) throws Exception{
    String useraccountkey = StringUtil.getRandomString();
    String email = user.getString("email");
    String mobile = user.getString("mobile");
    String username = user.getString("username");
    String firstname = user.getString("firstname");
    String lastname = user.getString("lastname");
    String password = user.getString("password");

    String query = "Insert into AuthDB.User (useraccountkey, email, mobile, username, firstname, "+
                   "lastname, password) values (?,?,?,?,?,?,?)";
    PreparedStatement stmt = this.connection.prepareStatement(query);
    stmt.setString(1,useraccountkey);
    stmt.setString(2,email);
    stmt.setString(3,mobile);
    stmt.setString(4,username);
    stmt.setString(5,firstname);
    stmt.setString(6,lastname);
    stmt.setString(7,password);

    stmt.executeUpdate();


    return true;
  }

  public JsonObject login(JsonObject loginRequest) throws Exception{
    JsonObject user = null;
    String username = loginRequest.getString("username");
    String password = loginRequest.getString("password");

    String query = "Select password, useraccountkey from AuthDB.User where username=?";
    PreparedStatement stmt = this.connection.prepareStatement(query);
    stmt.setString(1, username);

    ResultSet results = stmt.executeQuery();
    if(results.next()){
      user = Json.createObjectBuilder().add("password",results.getString("password"))
                                       .add("useraccountkey", results.getString("useraccountkey"))
                                       .build();
    }

    return user;
  }


  public void updateAuthToken(String userAccountKey, String token) throws Exception{
    String query = "Update AuthDB.User set authtoken=? where useraccountkey=?";
    PreparedStatement stmt = this.connection.prepareStatement(query);
    stmt.setString(1,token);
    stmt.setString(2,userAccountKey);
    stmt.executeUpdate();
  }

}
