package com.bellatrix.logger.handler.console;

import com.bellatrix.logger.LogType;
import com.bellatrix.logger.handler.LogHandler;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class ConsoleLogHandler implements LogHandler {

    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_INSTANT;

    @Override
    public void log(String clazz, LogType logType, String message) {
        String finalMessage = prepareMessage(clazz, logType, message);
        System.out.println(finalMessage);
    }

    private String prepareMessage(String clazz, LogType logType, String message) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(clazz);
        messageBuilder.append("\t");
        messageBuilder.append(logType.name());
        messageBuilder.append("\t");
        messageBuilder.append(DATE_TIME_FORMATTER.format(Instant.now()));
        messageBuilder.append("\t");
        messageBuilder.append(message);
        return messageBuilder.toString();
    }
}
