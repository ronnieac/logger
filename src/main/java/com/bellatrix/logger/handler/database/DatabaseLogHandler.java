package com.bellatrix.logger.handler.database;

import com.bellatrix.logger.LogType;
import com.bellatrix.logger.handler.LogHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class DatabaseLogHandler implements LogHandler {

    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_INSTANT;

    private ConnectionPool connectionPool;

    public DatabaseLogHandler(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void log(String clazz, LogType logType, String message) {

        try (PreparedStatement preparedStatement =
                     this.connectionPool.getConnection().prepareStatement(
                             "INSERT INTO LOG VALUES(?, ?, ?, ?)")) {

            preparedStatement.setString(1, clazz);
            preparedStatement.setString(2, DATE_TIME_FORMATTER.format(Instant.now()));
            preparedStatement.setString(3, logType.getValue());
            preparedStatement.setString(4, message);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
