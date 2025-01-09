package com.vixr.log_analyzer.parser.filters;

import com.vixr.log_analyzer.config.ParserConfig;
import com.vixr.log_analyzer.parser.model.LogEntry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LogFilter {

    private final ParserConfig config;

    public LogFilter(ParserConfig config) {
        this.config = config;
    }

    public List<LogEntry> filter(List<LogEntry> logEntries) {
        return filterByPatterns(filterByLevel(logEntries));
    }


    public List<LogEntry> filterByLevel(List<LogEntry> logs) {
        if (config.getLevels() != null && config.getLevels().isEmpty()) {
            return logs;
        }
        return logs.stream()
            .filter(log -> config.getLevels().contains(log.getLevel()))
            .collect(Collectors.toList());


    }

    public List<LogEntry> filterByPatterns(List<LogEntry> logs) {
        if (config.getPatterns() == null || config.getPatterns().isEmpty()) {
            return logs;
        }

        return logs.stream()
            .filter(logEntry -> config.getPatterns().stream()
                .anyMatch(pattern -> logEntry.getMessage().contains(pattern)))
            .toList();
    }

    public boolean hasPattern(LogEntry logEntry) {
        if(!config.getPatterns().isEmpty() && config.getPatterns() != null) {
            return config.getPatterns().stream()
                .anyMatch(pattern -> logEntry.getMessage().contains(pattern));
        }
        return true;
    }


}
