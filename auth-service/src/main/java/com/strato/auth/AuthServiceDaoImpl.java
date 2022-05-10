package com.strato.auth;

import javax.json.JsonObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.json.JsonObject;
import javax.json.Json;

import com.strato.db.DBConnector;

import com.strato.util.StringUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class AuthServiceDaoImpl implements AuthServiceDao{

  private static final Logger logger = LogManager.getLogger(AuthServiceDaoImpl.class);

  private Connection connection;

  private static final String USER_NAME = System.getenv().get("DB_USER_NAME");
  private static final String PASSWORD = System.getenv().get("DB_PASSWORD");
  private static final String DB_END_POINT = System.getenv().get("DB_END_POINT");
  private static final String AUTH_DB = System.getenv().get("AUTH_DB");

  private static final String TBL_USER = System.getenv().getOrDefault("TBL_USER","rs_user");
  private static final String TBL_USER_DEVICE = System.getenv().getOrDefault("TBL_USER_DEVICE","rs_user_device");

  public AuthServiceDaoImpl(){
    this.connection = DBConnector.createConnectionViaUserPwd(USER_NAME, PASSWORD, DB_END_POINT);
  }


  public JsonObject getUserDevice(String accessToken, String refreshToken) throws Exception{
    JsonObject device = null;
    String query = "Select user_account_key, refresh_token_expiry_datetime from "+ AUTH_DB + "." + TBL_USER_DEVICE + " Where "+
                   "access_token=? And refresh_token=?";
    PreparedStatement stmt = this.connection.prepareStatement(query);
    stmt.setString(1,accessToken);
    stmt.setString(2,refreshToken);
    ResultSet results = stmt.executeQuery();
    if(results.next()){
      device = Json.createObjectBuilder().add("user_account_key",results.getString("user_account_key"))
                                       .add("refresh_token_expiry_datetime", results.getLong("refresh_token_expiry_datetime"))
                                       .build();
    }
    return device;
  }


  public boolean registerUser(JsonObject user) throws Exception{
    String useraccountkey = StringUtil.getRandomString();
    String email = user.getString("email");
    String mobile = user.getString("mobile");
    String username = user.getString("username");
    String firstname = user.getString("firstname");
    String lastname = user.getString("lastname");
    String password = user.getString("password");

    String query = "Insert into "+ AUTH_DB + "." + TBL_USER + " (user_account_key, email, mobile, username, firstname, "+
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

    String query = "Select password, user_account_key from "+ AUTH_DB + "." + TBL_USER + " where username=?";
    PreparedStatement stmt = this.connection.prepareStatement(query);
    stmt.setString(1, username);

    ResultSet results = stmt.executeQuery();
    if(results.next()){
      user = Json.createObjectBuilder().add("password",results.getString("password"))
                                       .add("user_account_key", results.getString("user_account_key"))
                                       .build();
    }

    return user;
  }


  public void updateAuthTokens(String userAccountKey,
                               String accessToken,
                               long accessTokenExpiryDateTime,
                               String refreshToken,
                               long refreshTokenExpiryDateTime,
                               String deviceKey,
                               String deviceName) throws Exception {
    logger.info("Updating auth tokens ....");
    JsonObject device = this.getRegisteredDevice(userAccountKey, deviceKey);
    if(device == null){
      logger.info("Registering new device for account " + userAccountKey);
      String query = "Insert into "+ AUTH_DB + "." + TBL_USER_DEVICE + " (user_account_key, device_key,"+
                     "device_name, access_token, refresh_token, access_token_expiry_datetime,"+
                     "refresh_token_expiry_datetime) values(?,?,?,?,?,?,?)";
      PreparedStatement stmt = this.connection.prepareStatement(query);
      stmt.setString(1,userAccountKey);
      stmt.setString(2,deviceKey);
      stmt.setString(3,deviceName);
      stmt.setString(4,accessToken);
      stmt.setString(5,refreshToken);
      stmt.setLong(6,accessTokenExpiryDateTime);
      stmt.setLong(7,refreshTokenExpiryDateTime);
      stmt.executeUpdate();
    }else{
      logger.info("Updating device for account " + userAccountKey);
      String query = "Update "+ AUTH_DB + "." + TBL_USER_DEVICE + " Set access_token=?, access_token_expiry_datetime=?,"+
                     "refresh_token=?, refresh_token_expiry_datetime=? "+
                     "Where user_account_key=? and device_key=?";
      PreparedStatement stmt = this.connection.prepareStatement(query);
      stmt.setString(1,accessToken);
      stmt.setLong(2,accessTokenExpiryDateTime);
      stmt.setString(3,refreshToken);
      stmt.setLong(4,refreshTokenExpiryDateTime);
      stmt.setString(5,userAccountKey);
      stmt.setString(6,deviceKey);

      stmt.executeUpdate();
    }
  }

  private JsonObject getRegisteredDevice(String userAccountKey, String deviceKey) throws Exception {
    if(deviceKey == null || deviceKey.trim().isEmpty()){
      logger.info("Device key is empty");
      return null;
    }
    JsonObject device = null;

    String query = "Select device_key, user_account_key from "+ AUTH_DB + "." + TBL_USER_DEVICE + " where user_account_key=? and device_key=?";
    PreparedStatement stmt = this.connection.prepareStatement(query);
    stmt.setString(1, userAccountKey);
    stmt.setString(2, deviceKey);

    ResultSet results = stmt.executeQuery();
    if(results.next()){
      device = Json.createObjectBuilder().add("device_key",results.getString("device_key"))
                                       .add("user_account_key", results.getString("user_account_key"))
                                       .build();
    }

    return device;

  }

}
