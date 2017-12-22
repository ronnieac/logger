package com.bellatrix.logger;

import com.bellatrix.logger.handler.LogHandlerType;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {

    private final static Set<LogType> logTypesEnabled = Stream.of(LogType.WARNING, LogType.MESSAGE).collect(Collectors.toSet());
    private final static Set<LogHandlerType> logHandlerTypesEnabled = Stream.of(LogHandlerType.CONSOLE, LogHandlerType.FILE).collect(Collectors.toSet());
    private final static JobLogger jobLogger = new JobLogger(logTypesEnabled, logHandlerTypesEnabled, App.class.getTypeName());

    public static void main(String[] args) {
        jobLogger.log(LogType.MESSAGE, "Message");
        jobLogger.log(LogType.WARNING, "Warning");
        jobLogger.log(LogType.ERROR, "Error");
    }
}
