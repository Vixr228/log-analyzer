package com.vixr.log_analyzer.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LogScheduler {
    private final LogProcessingTask logProcessingTask;

    @Value("${log.processing.fixed-rate}")
    private long fixedRate;

    @Autowired
    public LogScheduler(LogProcessingTask logProcessingTask) {
        this.logProcessingTask = logProcessingTask;
    }

    @Scheduled(fixedRateString = "${log.processing.fixed-rate}")
    public void scheduleLogProcessing(){
        System.out.println("Starting scheduled log processing...");
        logProcessingTask.process();
    }
}
