package com.bellatrix.logger.handler.database;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConnectionConfigTest {

    private final static String DRIVER_CLASS = "DRIVER_CLASS";
    private final static String JDBC_URL = "JDBC_URL";
    private final static String USERNAME = "USERNAME";
    private final static String PASSWORD = "PASSWORD";

    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        this.connectionConfig = new ConnectionConfig(DRIVER_CLASS, JDBC_URL, USERNAME, PASSWORD);
    }

    @Test
    public void shouldGetAllProperties() {
        assertEquals(DRIVER_CLASS, connectionConfig.getDriverClass());
        assertEquals(JDBC_URL, connectionConfig.getJdbcUrl());
        assertEquals(USERNAME, connectionConfig.getUsername());
        assertEquals(PASSWORD, connectionConfig.getPassword());
    }
}
