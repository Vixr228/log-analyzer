package com.vixr.log_analyzer.notification;

import com.vixr.log_analyzer.analyzer.model.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class NotificationService {

    private final MessageBuilder messageBuilder;
    private final TelegramNotifier telegramNotifier;

    @Autowired
    public NotificationService(MessageBuilder messageBuilder, TelegramNotifier telegramNotifier) {
        this.messageBuilder = messageBuilder;
        this.telegramNotifier = telegramNotifier;
    }

    public void sendNotification(Report report) throws IOException {
        String message = messageBuilder.buildMessage(report);
        telegramNotifier.sendNotification(message);

        File file = messageBuilder.prepareFile(report);
        telegramNotifier.sendFile(file);
    }
}
