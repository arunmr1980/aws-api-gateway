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
    String userName = "admin";
    String password = "milktreat";
    String dbEndpoint = "test-db.cnyaitbtsksi.us-east-2.rds.amazonaws.com";
    Connection connection = DBConnector.createConnectionViaUserPwd(userName, password, dbEndpoint);
    assertNotNull(connection);
  }


}
