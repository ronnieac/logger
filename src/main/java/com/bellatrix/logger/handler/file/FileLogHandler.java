package com.bellatrix.logger.handler.file;

import com.bellatrix.logger.LogType;
import com.bellatrix.logger.handler.LogHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class FileLogHandler implements LogHandler {

    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_INSTANT;

    private final File file;

    public FileLogHandler(String fileName) {
        file = new File(fileName);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void log(String clazz, LogType logType, String message) {

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.append(prepareMessage(clazz, logType, message));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String prepareMessage(String clazz, LogType logType, String message) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(clazz);
        messageBuilder.append(";");
        messageBuilder.append(logType.name());
        messageBuilder.append(";");
        messageBuilder.append(DATE_TIME_FORMATTER.format(Instant.now()));
        messageBuilder.append(";");
        messageBuilder.append(message);
        messageBuilder.append("\n");
        return messageBuilder.toString();
    }
}
