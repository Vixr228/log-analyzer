package com.vixr.log_analyzer.parser;

import com.vixr.log_analyzer.parser.model.LogEntry;
import com.vixr.log_analyzer.parser.sources.LogSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogParser {
    private final LogSource logSource;

    @Autowired
    public LogParser(LogSource logSource) {
        this.logSource = logSource;
    }

    public List<LogEntry> parseLogs() {
        return logSource.fetchLogs();
    }
}
