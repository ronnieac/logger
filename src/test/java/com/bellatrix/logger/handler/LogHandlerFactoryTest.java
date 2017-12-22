package com.bellatrix.logger.handler;

import com.bellatrix.logger.handler.console.ConsoleLogHandler;
import com.bellatrix.logger.handler.database.ConnectionConfig;
import com.bellatrix.logger.handler.database.ConnectionPool;
import com.bellatrix.logger.handler.database.DatabaseLogHandler;
import com.bellatrix.logger.handler.file.FileLogHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LogHandlerFactory.class, ConnectionConfig.class})
public class LogHandlerFactoryTest {

    private final static String DRIVER_CLASS = "DRIVER_CLASS";
    private final static String JDBC_URL = "JDBC_URL";
    private final static String USERNAME = "USERNAME";
    private final static String PASSWORD = "PASSWORD";
    private final static String FILENAME = UUID.randomUUID().toString();

    @Rule
    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    private LogHandlerFactory logHandlerFactory;

    @Before
    public void setUp() throws Exception {

        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        ConnectionPool connectionPool = mock(ConnectionPool.class);

        whenNew(ConnectionConfig.class)
                .withArguments(eq(DRIVER_CLASS), eq(JDBC_URL), eq(USERNAME), eq(PASSWORD))
                .thenReturn(connectionConfig);

        whenNew(ConnectionPool.class)
                .withArguments(eq(connectionConfig))
                .thenReturn(connectionPool);
    }

    @Test
    public void shouldCreateConsoleHandler() throws Exception {

        environmentVariables.set("DB_DRIVER_CLASS", null);
        environmentVariables.set("DB_JDBC_URL", null);
        environmentVariables.set("DB_USERNAME", null);
        environmentVariables.set("DB_PASSWORD", null);
        environmentVariables.set("LOG_FILENAME", null);

        logHandlerFactory = new LogHandlerFactory();

        LogHandler logHandler = logHandlerFactory.create(LogHandlerType.CONSOLE);

        assertEquals(ConsoleLogHandler.class.getTypeName(), logHandler.getClass().getTypeName());
    }

    @Test
    public void shouldCreateFileHandler() throws Exception {

        environmentVariables.set("DB_DRIVER_CLASS", null);
        environmentVariables.set("DB_JDBC_URL", null);
        environmentVariables.set("DB_USERNAME", null);
        environmentVariables.set("DB_PASSWORD", null);
        environmentVariables.set("LOG_FILENAME", FILENAME);

        logHandlerFactory = new LogHandlerFactory();

        LogHandler logHandler = logHandlerFactory.create(LogHandlerType.FILE);

        assertEquals(FileLogHandler.class.getTypeName(), logHandler.getClass().getTypeName());
    }

    @Test
    public void shouldCreateDatabaseHandler() throws Exception {

        environmentVariables.set("DB_DRIVER_CLASS", DRIVER_CLASS);
        environmentVariables.set("DB_JDBC_URL", JDBC_URL);
        environmentVariables.set("DB_USERNAME", USERNAME);
        environmentVariables.set("DB_PASSWORD", PASSWORD);
        environmentVariables.set("LOG_FILENAME", null);

        logHandlerFactory = new LogHandlerFactory();

        LogHandler logHandler = logHandlerFactory.create(LogHandlerType.DATABASE);

        assertEquals(DatabaseLogHandler.class.getTypeName(), logHandler.getClass().getTypeName());
    }

    @After
    public void cleanUp() {
        new File(FILENAME).delete();
    }
}
