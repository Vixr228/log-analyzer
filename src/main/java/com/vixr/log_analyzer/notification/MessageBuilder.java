package com.vixr.log_analyzer.notification;

import com.vixr.log_analyzer.analyzer.model.Report;
import com.vixr.log_analyzer.parser.model.LogEntry;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class MessageBuilder {

    public String buildMessage(Report report) {
        StringBuilder message = new StringBuilder("⚠️ Сканирование выполнено.\n");
        message.append("— Всего обнаружено логов: ").append(report.getTotalLogsAmount()).append("\n")
            .append("— Логов, удовлетворяющих условиям: ").append(report.getFilteredLogsAmount()).append("\n")
            .append("— Расшифровка по уровням:\n");

        report.getAmountByLevel().forEach((level, count) ->
            message.append("   * ").append(level).append(": ").append(count).append("\n")
        );

        return message.toString();
    }

    public File prepareFile(Report report) {
        File tempFile = null;
        try {
            // Создаем временный файл
            tempFile = File.createTempFile("logs-", ".log");
            tempFile.deleteOnExit();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                for (LogEntry log : report.getFilteredLogs()) {
                    writer.write(formatLogEntry(log));
                    writer.newLine(); // Добавляем новую строку после каждой записи
                }
            }

            System.out.println("Logs successfully written to temporary file: " + tempFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error writing logs to temporary file: " + e.getMessage());
        }

        return tempFile;
    }

    private String formatLogEntry(LogEntry log) {
        return String.format("[%s] %s - %s: %s",
            log.getTimestamp(),
            log.getLevel(),
            log.getSource(),
            log.getMessage()
        );
    }
}
