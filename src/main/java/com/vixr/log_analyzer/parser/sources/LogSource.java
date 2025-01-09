package com.vixr.log_analyzer.parser.sources;

import com.vixr.log_analyzer.parser.model.LogEntry;

import java.util.List;

public interface LogSource {
    List<LogEntry> fetchLogs();
}
