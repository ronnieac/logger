package com.bellatrix.logger;

import com.bellatrix.logger.handler.LogHandler;
import com.bellatrix.logger.handler.LogHandlerFactory;
import com.bellatrix.logger.handler.LogHandlerType;

import java.util.HashSet;
import java.util.Set;

public class JobLogger {

    private final Set<LogType> logTypesEnabled = new HashSet<>();
    private final Set<LogHandler> logHandlers = new HashSet<>();

    private final String clazz;

    public JobLogger(
            Set<LogType> logTypesEnabled,
            Set<LogHandlerType> logHandlerTypesEnabled,
            String clazz) {

        validateConstructorParams(
                logTypesEnabled,
                logHandlerTypesEnabled,
                clazz);

        this.logTypesEnabled.addAll(logTypesEnabled);

        logHandlerTypesEnabled.forEach(logHandlerType -> {
            this.logHandlers.add(
                    LogHandlerFactory.getInstance().create(logHandlerType));
        });

        this.clazz = clazz;
    }

    private void validateConstructorParams(
            Set<LogType> logTypesEnabled,
            Set<LogHandlerType> logHandlerTypesEnabled,
            String clazz) {

        if (logTypesEnabled == null || logTypesEnabled.isEmpty())
            throw new IllegalArgumentException("Log types enabled must be at least one");

        if (logHandlerTypesEnabled == null || logHandlerTypesEnabled.isEmpty())
            throw new IllegalArgumentException("Log handler types enabled must be at least one");

        if (clazz == null || clazz.trim().isEmpty())
            throw new IllegalArgumentException("Invalid clazz argument");
    }

    public void log(LogType logType, String message) {

        if (logTypesEnabled.contains(logType)) {
            this.logHandlers.forEach(logHandler -> {
                logHandler.log(clazz, logType, message);
            });
        }
    }
}
