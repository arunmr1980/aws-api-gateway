package com.strato.auth;

import javax.json.JsonObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.json.JsonObject;
import javax.json.Json;

import com.strato.db.DBConnector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AuthServiceDaoImpl implements AuthServiceDao{

  private static final Logger logger = LogManager.getLogger(AuthServiceDaoImpl.class);

  private Connection connection;

  private static final String USER_NAME = System.getenv().get("DB_USER_NAME");
  private static final String PASSWORD = System.getenv().get("DB_PASSWORD");
  private static final String DB_END_POINT = System.getenv().get("DB_END_POINT");
  private static final String AUTH_DB = System.getenv().get("AUTH_DB");

  private static final String TBL_USER_DEVICE = System.getenv().getOrDefault("TBL_USER_DEVICE","rs_user_device");

  public AuthServiceDaoImpl(){
    this.connection = DBConnector.createConnectionViaUserPwd(USER_NAME, PASSWORD, DB_END_POINT);
  }

  public JsonObject getUserDevice(String accessToken) throws Exception{
    logger.info("isTokenActive() token " + accessToken);
    String query = "Select user_account_key, device_key, refresh_token_expiry_datetime from "+ AUTH_DB + "." + TBL_USER_DEVICE + " Where "+
                   "access_token=?";
    PreparedStatement stmt = this.connection.prepareStatement(query);
    stmt.setString(1,accessToken);
    ResultSet results = stmt.executeQuery();
    return this.getUserDevice(results);
  }


  private JsonObject getUserDevice(ResultSet results) throws Exception{
    JsonObject device = null;
    if(results.next()){
      device = Json.createObjectBuilder()
                   .add("user_account_key",results.getString("user_account_key"))
                   .add("device_key",results.getString("device_key"))
                   .add("refresh_token_expiry_datetime", results.getLong("refresh_token_expiry_datetime"))
                   .build();
    }
    return device;
  }

}
