package com.vixr.log_analyzer.analyzer.model;

import com.vixr.log_analyzer.parser.model.LogEntry;

import java.util.List;
import java.util.Map;


public class Report {

    private Integer totalLogsAmount;
    private Integer filteredLogsAmount;
    private Map<String, Integer> amountByLevel;
    private List<LogEntry> filteredLogs;

    public Report(Integer totalLogsAmount, Integer filteredLogsAmount, Map<String, Integer> amountByLevel, List<LogEntry> filteredLogs) {
        this.totalLogsAmount = totalLogsAmount;
        this.filteredLogsAmount = filteredLogsAmount;
        this.amountByLevel = amountByLevel;
        this.filteredLogs = filteredLogs;
    }

    public Integer getTotalLogsAmount() {
        return totalLogsAmount;
    }

    public void setTotalLogsAmount(Integer totalLogsAmount) {
        this.totalLogsAmount = totalLogsAmount;
    }

    public Integer getFilteredLogsAmount() {
        return filteredLogsAmount;
    }

    public void setFilteredLogsAmount(Integer filteredLogsAmount) {
        this.filteredLogsAmount = filteredLogsAmount;
    }

    public Map<String, Integer> getAmountByLevel() {
        return amountByLevel;
    }

    public void setAmountByLevel(Map<String, Integer> amountByLevel) {
        this.amountByLevel = amountByLevel;
    }

    public List<LogEntry> getFilteredLogs() {
        return filteredLogs;
    }

    public void setFilteredLogs(List<LogEntry> filteredLogs) {
        this.filteredLogs = filteredLogs;
    }

}
