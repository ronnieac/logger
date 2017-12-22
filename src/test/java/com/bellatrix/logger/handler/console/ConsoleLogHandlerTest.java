package com.bellatrix.logger.handler.console;

import com.bellatrix.logger.LogType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ConsoleLogHandler.class)
public class ConsoleLogHandlerTest {

    @InjectMocks
    private ConsoleLogHandler consoleLogHandler;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        mockStatic(Instant.class);
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void shouldPrintIntoConsole() {
        DateTimeFormatter df = DateTimeFormatter.ISO_INSTANT;

        Instant at = new Date().toInstant();
        LogType type = LogType.ERROR;
        String clazz = "clazz";
        String message = "error";

        when(Instant.now()).thenReturn(at);

        consoleLogHandler.log(clazz, type, message);

        String expected = String.format("%s\t%s\t%s\t%s\n", clazz, type.name(), df.format(at),message);
        assertEquals(expected, outContent.toString());
    }
}
