package com.bellatrix.logger;

public enum LogType {

    MESSAGE("1"),
    ERROR("2"),
    WARNING("3");

    private String value;

    LogType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
