package com.vixr.log_analyzer.analyzer;

import com.vixr.log_analyzer.analyzer.model.Report;
import com.vixr.log_analyzer.parser.model.LogEntry;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StatisticsAggregator {

    public Report aggregateStatistics(List<LogEntry> allLogs, List<LogEntry> filteredLogs) {
        Map<String, Integer> amountByLevel = new HashMap<>();

        for (LogEntry log : filteredLogs) {
            amountByLevel.merge(log.getLevel(), 1, Integer::sum);
        }

        Integer totalLogsAmount = allLogs.size();
        Integer filteredLogsAmount = filteredLogs.size();

        return new Report(totalLogsAmount, filteredLogsAmount, amountByLevel, filteredLogs);
    }

}
