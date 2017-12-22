package com.bellatrix.logger.handler.database;

import com.bellatrix.logger.LogType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.zapodot.junit.db.EmbeddedDatabaseRule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;


public class DatabaseLogHandlerTest {

    @Rule
    public EmbeddedDatabaseRule embeddedDatabaseMysqlRule =
            EmbeddedDatabaseRule.builder()
                    .withMode("ORACLE")
                    .withInitialSqlFromResource("classpath:initial.sql")
                    .build();

    private ConnectionPool connectionPool;
    private DatabaseLogHandler databaseLogHandler;

    @Before
    public void setUp() {
        connectionPool = mock(ConnectionPool.class);
        when(connectionPool.getConnection()).thenReturn(embeddedDatabaseMysqlRule.getConnection());
        databaseLogHandler = new DatabaseLogHandler(connectionPool);
    }

    @Test
    public void shouldInsertNewLog() throws SQLException {
        LogType type = LogType.ERROR;
        String clazz = "clazz";
        String message = UUID.randomUUID().toString();

        databaseLogHandler.log(clazz, type, message);

        ResultSet rs = embeddedDatabaseMysqlRule.getConnection()
                .prepareStatement("SELECT * FROM LOG")
                .executeQuery();

        assertTrue(rs.next());
        assertEquals(clazz, rs.getString(1));
        assertEquals(type.getValue(), rs.getString(3));
        assertEquals(message, rs.getString(4));
    }

}
