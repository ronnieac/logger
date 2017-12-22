package com.bellatrix.logger.handler;

import com.bellatrix.logger.LogType;

public interface LogHandler {

    void log(String clazz, LogType logType, String message);
}
