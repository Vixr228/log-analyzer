package com.vixr.log_analyzer.analyzer;

import com.vixr.log_analyzer.analyzer.model.Report;
import com.vixr.log_analyzer.parser.model.LogEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class LogAnalyzer {
    private final EventDetector eventDetector;
    private final StatisticsAggregator statisticsAggregator;
    private final AlertNotifier alertNotifier;

    @Autowired
    public LogAnalyzer(EventDetector eventDetector, StatisticsAggregator statisticsAggregator, AlertNotifier alertNotifier) {
        this.eventDetector = eventDetector;
        this.statisticsAggregator = statisticsAggregator;
        this.alertNotifier = alertNotifier;
    }

    public void analyzeLogs(List<LogEntry> logs) throws IOException {
        // Обнаружение событий
        List<LogEntry> detectedEvents = eventDetector.detectEvents(logs);

        // Агрегация статистики
        Report report = statisticsAggregator.aggregateStatistics(logs, detectedEvents);

        // Отправка уведомлений с отчетом
        alertNotifier.sendAlerts(report);
    }


}
