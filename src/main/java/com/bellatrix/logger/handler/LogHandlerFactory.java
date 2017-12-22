package com.bellatrix.logger.handler;

import com.bellatrix.logger.handler.console.ConsoleLogHandler;
import com.bellatrix.logger.handler.database.ConnectionConfig;
import com.bellatrix.logger.handler.database.ConnectionPool;
import com.bellatrix.logger.handler.database.DatabaseLogHandler;
import com.bellatrix.logger.handler.file.FileLogHandler;

public class LogHandlerFactory {

    private ConnectionPool connectionPool;
    private String fileName;

    LogHandlerFactory() {
        connectionPool = createConnectionPool();
        fileName = readfileName();
    }

    private ConnectionPool createConnectionPool() {

        String driverClass = System.getenv("DB_DRIVER_CLASS");
        String jdbcUrl = System.getenv("DB_JDBC_URL");
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");

        if (driverClass != null && !driverClass.trim().isEmpty()
                && jdbcUrl != null && !jdbcUrl.trim().isEmpty()
                && username != null && !username.trim().isEmpty()
                && password != null && !password.trim().isEmpty())

            return new ConnectionPool(
                    new ConnectionConfig(
                            driverClass, jdbcUrl, username, password));

        return null;
    }

    private String readfileName() {
        String fileName = System.getenv("LOG_FILENAME");

        if (fileName != null && fileName.trim().isEmpty())
            return null;
        else
            return fileName;
    }

    public LogHandler create(LogHandlerType logHandlerType) {
        switch (logHandlerType) {
            case CONSOLE:
                return new ConsoleLogHandler();

            case FILE:
                if (fileName != null)
                    return new FileLogHandler(fileName);
                else
                    throw new RuntimeException("File name for file handler not defined");

            case DATABASE:
                if (connectionPool != null)
                    return new DatabaseLogHandler(connectionPool);
                else
                    throw new RuntimeException("Connection config for database handler not defined");

            default:
                return null;
        }
    }

    private final static LogHandlerFactory instance = new LogHandlerFactory();

    public static LogHandlerFactory getInstance() {
        return instance;
    }

}
