package com.strato.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;


class DBConnectorTest{

  private static final Logger logger = LoggerFactory.getLogger(DBConnectorTest.class);

  @Test
  void createConnectionViaUserPwd(){
    String userName = System.getenv().get("DB_USER_NAME");
    String password = System.getenv().get("DB_PASSWORD");
    String dbEndpoint = System.getenv().get("DB_END_POINT");
    Connection connection = DBConnector.createConnectionViaUserPwd(userName, password, dbEndpoint);
    assertNotNull(connection);
  }


}
