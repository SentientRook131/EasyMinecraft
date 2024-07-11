package com.sen.Log;

import java.io.Serializable;
import java.time.LocalDateTime;

public record LogEntry(LocalDateTime date, String event, String executor, String action, Object info) implements Serializable {
    @Override
    public String toString() {
        return "[" + date.toString() + "] Event: " + event + "; Executor: " + executor + "; Action: " + action + "; Info: " + info;
    }

    public static LogEntry generate(String event, String executor, String action) {
        return new LogEntry(LocalDateTime.now(), event, executor, action, "");
    }
    public static LogEntry generate(String event, String executor, String action, Object info) {
        return new LogEntry(LocalDateTime.now(), event, executor, action, info.toString());
    }
}
