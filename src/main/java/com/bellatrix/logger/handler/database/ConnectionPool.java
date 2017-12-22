package com.bellatrix.logger.handler.database;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {

    private final ComboPooledDataSource comboPooledDataSource;

    public ConnectionPool(ConnectionConfig connectionConfig) {
        try {
            comboPooledDataSource = new ComboPooledDataSource();
            comboPooledDataSource.setDriverClass(connectionConfig.getDriverClass());
            comboPooledDataSource.setJdbcUrl(connectionConfig.getJdbcUrl());
            comboPooledDataSource.setUser(connectionConfig.getUsername());
            comboPooledDataSource.setPassword(connectionConfig.getPassword());
        } catch (PropertyVetoException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Connection getConnection() {
        try {
            return comboPooledDataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
