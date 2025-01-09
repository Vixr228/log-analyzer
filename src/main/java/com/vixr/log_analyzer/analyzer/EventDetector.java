package com.vixr.log_analyzer.analyzer;

import com.vixr.log_analyzer.parser.filters.LogFilter;
import com.vixr.log_analyzer.parser.model.LogEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventDetector {

    private final LogFilter logFilter;

    @Autowired
    public EventDetector(LogFilter logFilter) {
        this.logFilter = logFilter;
    }

    public List<LogEntry> detectEvents(List<LogEntry> logs) {
        return logFilter.filter(logs);
    }
}
