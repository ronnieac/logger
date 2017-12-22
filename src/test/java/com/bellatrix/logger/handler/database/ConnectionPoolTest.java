package com.bellatrix.logger.handler.database;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConnectionPool.class, ComboPooledDataSource.class})
public class ConnectionPoolTest {

    private final static String DRIVER_CLASS = "DRIVER_CLASS";
    private final static String JDBC_URL = "JDBC_URL";
    private final static String USERNAME = "USERNAME";
    private final static String PASSWORD = "PASSWORD";

    private ConnectionConfig connectionConfig;
    private ComboPooledDataSource comboPooledDataSource;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig(
                DRIVER_CLASS,
                JDBC_URL,
                USERNAME,
                PASSWORD);
    }

    @Test
    public void shouldCreateDataSourceUsingConnectionConfig() throws Exception {

        comboPooledDataSource = mock(ComboPooledDataSource.class);
        whenNew(ComboPooledDataSource.class).withNoArguments().thenReturn(comboPooledDataSource);

        ConnectionPool connectionPool = new ConnectionPool(connectionConfig);

        verify(comboPooledDataSource, times(1))
                .setDriverClass(eq(DRIVER_CLASS));
        verify(comboPooledDataSource, times(1))
                .setJdbcUrl(eq(JDBC_URL));
        verify(comboPooledDataSource, times(1))
                .setUser(eq(USERNAME));
        verify(comboPooledDataSource, times(1))
                .setPassword(eq(PASSWORD));

    }

    @Test
    public void shouldReturnConnectionFromDataSource() throws Exception {

        comboPooledDataSource = mock(ComboPooledDataSource.class);
        whenNew(ComboPooledDataSource.class).withNoArguments().thenReturn(comboPooledDataSource);

        ConnectionPool connectionPool = new ConnectionPool(connectionConfig);
        Connection connection = connectionPool.getConnection();

        verify(comboPooledDataSource, times(1)).getConnection();
    }

}
