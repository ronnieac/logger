package com.bellatrix.logger;

import com.bellatrix.logger.handler.LogHandlerFactory;
import com.bellatrix.logger.handler.LogHandlerType;
import com.bellatrix.logger.handler.console.ConsoleLogHandler;
import com.bellatrix.logger.handler.database.DatabaseLogHandler;
import com.bellatrix.logger.handler.file.FileLogHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({JobLogger.class, LogHandlerFactory.class})
public class JobLoggerTest {

    private LogHandlerFactory logHandlerFactory;

    private ConsoleLogHandler consoleLogHandler;
    private FileLogHandler fileLogHandler;
    private DatabaseLogHandler databaseLogHandler;

    @Before
    public void setUp() {
        mockStatic(LogHandlerFactory.class);

        consoleLogHandler = mock(ConsoleLogHandler.class);
        fileLogHandler = mock(FileLogHandler.class);
        databaseLogHandler = mock(DatabaseLogHandler.class);

        logHandlerFactory = mock(LogHandlerFactory.class);

        when(logHandlerFactory.create(eq(LogHandlerType.CONSOLE))).thenReturn(consoleLogHandler);
        when(logHandlerFactory.create(eq(LogHandlerType.FILE))).thenReturn(fileLogHandler);
        when(logHandlerFactory.create(eq(LogHandlerType.DATABASE))).thenReturn(databaseLogHandler);

        when(LogHandlerFactory.getInstance()).thenReturn(logHandlerFactory);
    }

    @Test
    public void shouldReturnIllegalArgumentException() {
        Set<LogType> logTypesEnabled = Stream.of(LogType.WARNING, LogType.MESSAGE).collect(Collectors.toSet());
        Set<LogHandlerType> logHandlerTypesEnabled = Collections.emptySet();

        try {
            JobLogger jobLogger = new JobLogger(logTypesEnabled, logHandlerTypesEnabled, "clazz");
            Assert.fail("Job logger has no validation for arguments");
        } catch (IllegalArgumentException ex) {

        }
    }

    @Test
    public void shouldCreateValidJobLogger() {
        Set<LogType> logTypesEnabled = Stream.of(LogType.WARNING, LogType.MESSAGE).collect(Collectors.toSet());
        Set<LogHandlerType> logHandlerTypesEnabled = Stream.of(LogHandlerType.CONSOLE, LogHandlerType.FILE).collect(Collectors.toSet());

        JobLogger jobLogger = new JobLogger(logTypesEnabled, logHandlerTypesEnabled, "clazz");
    }

    @Test
    public void shouldCallLogHandlers() {
        Set<LogType> logTypesEnabled = Stream.of(LogType.WARNING, LogType.MESSAGE).collect(Collectors.toSet());
        Set<LogHandlerType> logHandlerTypesEnabled = Stream.of(LogHandlerType.CONSOLE, LogHandlerType.FILE).collect(Collectors.toSet());

        JobLogger jobLogger = new JobLogger(logTypesEnabled, logHandlerTypesEnabled, "clazz");
        jobLogger.log(LogType.MESSAGE, "MESSAGE");

        verify(consoleLogHandler, times(1))
                .log(eq("clazz"), eq(LogType.MESSAGE), eq("MESSAGE"));

        verify(fileLogHandler, times(1))
                .log(eq("clazz"), eq(LogType.MESSAGE), eq("MESSAGE"));

        verify(databaseLogHandler, times(0))
                .log(any(), any(), any());
    }

    @Test
    public void shouldNotLogMessage() {
        Set<LogType> logTypesEnabled = Stream.of(LogType.WARNING, LogType.MESSAGE).collect(Collectors.toSet());
        Set<LogHandlerType> logHandlerTypesEnabled = Stream.of(LogHandlerType.CONSOLE, LogHandlerType.FILE).collect(Collectors.toSet());

        JobLogger jobLogger = new JobLogger(logTypesEnabled, logHandlerTypesEnabled, "clazz");
        jobLogger.log(LogType.ERROR, "MESSAGE");

        verify(consoleLogHandler, times(0))
                .log(any(), any(), any());

        verify(fileLogHandler, times(0))
                .log(any(), any(), any());

        verify(databaseLogHandler, times(0))
                .log(any(), any(), any());
    }
}
