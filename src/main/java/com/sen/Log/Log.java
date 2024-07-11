package com.sen.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Log implements Serializable {
    protected List<LogEntry> logEntryList;
    public Iterator<LogEntry> getIterator() {
        return logEntryList.iterator();
    }
    public Log() {
        logEntryList = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        logEntryList.forEach(le -> result.append(le).append("\n"));
        return result.toString();
    }

    public void saveToFile(String filePath) throws IOException {
        FileWriter writer = new FileWriter(filePath);
        writer.write(toString());
        writer.flush();
        writer.close();
    }

    public void saveToFile(File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(toString());
        writer.flush();
        writer.close();
    }

    public Log append(LogEntry logEntry) {
        this.logEntryList.add(logEntry);
        return this;
    }
}
