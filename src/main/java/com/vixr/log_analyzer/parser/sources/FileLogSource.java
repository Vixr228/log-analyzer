package com.vixr.log_analyzer.parser.sources;

import com.vixr.log_analyzer.config.LogConfig;
import com.vixr.log_analyzer.parser.model.LogEntry;
import com.vixr.log_analyzer.repository.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class FileLogSource implements LogSource {
    Logger logger = LoggerFactory.getLogger(FileLogSource.class);

    private final LogConfig logConfig;
    private final LogRepository logRepository;

    private LocalDateTime lastProcessedTimestamp = null;

    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2}:\\d{2}");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    @Autowired
    public FileLogSource(LogConfig logConfig, LogRepository logRepository) {
        this.logConfig = logConfig;
        this.logRepository = logRepository;
    }

    @Override
    public List<LogEntry> fetchLogs() {
        StringBuilder logBuilder = new StringBuilder();
        List<LogEntry> logEntries = new ArrayList<>();
        String filePath = logConfig.getFilePath();
        System.out.println(filePath);

        lastProcessedTimestamp = logRepository.findLastLogTimestamp();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            System.out.println(filePath);
            String line;

            while ((line = reader.readLine()) != null) {
                if (DATE_PATTERN.matcher(line).find()) {
                    if (!logBuilder.isEmpty()) {
                        LogEntry entry = parseLogEntry(logBuilder.toString());
                        if (entry != null && isNewEntry(entry)) {
                            logEntries.add(entry);
                            logRepository.save(entry);
                            lastProcessedTimestamp = entry.getTimestamp();
                        }
                        logBuilder.setLength(0);

                    }
                }
                logBuilder.append(line).append(System.lineSeparator());
            }

            if (!logBuilder.isEmpty()) {
                LogEntry entry = parseLogEntry(logBuilder.toString());
                if (entry != null && isNewEntry(entry)) {
                    logEntries.add(entry);
                    logRepository.save(entry);
                    lastProcessedTimestamp = entry.getTimestamp();
                }
            }
        } catch (IOException e) {
            logger.error("Cant read file");
        } catch (IllegalArgumentException e) {
            logger.error("No path to log file");
        }

        return logEntries;
    }

    private LogEntry parseLogEntry(String line) {
        try {
            String[] parts = line.split(" ", 6);

            LocalDateTime timestamp = LocalDateTime.parse(parts[0] + " " + parts[1], DATE_FORMATTER);
            String level = parts[2];
            String source = parts[3];
            String message = parts[4] + parts[5];

            return new LogEntry(timestamp, level, source, message);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isNewEntry(LogEntry entry) {
        return lastProcessedTimestamp == null || entry.getTimestamp().isAfter(lastProcessedTimestamp);
    }

}
