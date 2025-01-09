package com.vixr.log_analyzer.scheduler;

import com.vixr.log_analyzer.analyzer.LogAnalyzer;
import com.vixr.log_analyzer.parser.model.LogEntry;
import com.vixr.log_analyzer.parser.LogParser;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;


@Component
public class LogProcessingTask {

    private final LogAnalyzer logAnalyzer;
    private final LogParser logParser;

    public LogProcessingTask(LogAnalyzer logAnalyzer, LogParser logParser) {
        this.logAnalyzer = logAnalyzer;
        this.logParser = logParser;
    }

    public void process() {
        try {
            List<LogEntry> logs = logParser.parseLogs();
            logAnalyzer.analyzeLogs(logs);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
