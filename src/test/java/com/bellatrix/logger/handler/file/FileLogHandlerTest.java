package com.bellatrix.logger.handler.file;

import com.bellatrix.logger.LogType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.*;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FileLogHandler.class)
public class FileLogHandlerTest {

    private String fileName;
    private FileLogHandler fileLogHandler;

    @Before
    public void setUp() {
        fileName = UUID.randomUUID().toString();
        fileLogHandler = new FileLogHandler(fileName);

        mockStatic(Instant.class);
    }

    @Test
    public void shouldCreateNewLogFile() {
        assertTrue(new File(fileName).exists());
    }

    @Test
    public void shouldWriteNewLineIntoLogFile() throws IOException {

        DateTimeFormatter df = DateTimeFormatter.ISO_INSTANT;

        Instant at = new Date().toInstant();
        LogType type = LogType.ERROR;
        String clazz = "clazz";
        String message = "error";

        when(Instant.now()).thenReturn(at);

        fileLogHandler.log(clazz, type, message);

        String expected = String.format("%s;%s;%s;%s", clazz, type.name(), df.format(at), message);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            assertEquals(expected, bufferedReader.readLine());
        }
    }

    @After
    public void cleanUp() {
        new File(fileName).delete();
    }
}
