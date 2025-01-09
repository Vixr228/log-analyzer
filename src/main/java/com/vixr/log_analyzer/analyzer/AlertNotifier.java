package com.vixr.log_analyzer.analyzer;

import com.vixr.log_analyzer.analyzer.model.Report;
import com.vixr.log_analyzer.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class AlertNotifier {

    private final NotificationService notificationService;

    @Autowired
    public AlertNotifier(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void sendAlerts(Report report) throws IOException {
        if(!report.getFilteredLogs().isEmpty()) {
            notificationService.sendNotification(report);
        }
    }
}
