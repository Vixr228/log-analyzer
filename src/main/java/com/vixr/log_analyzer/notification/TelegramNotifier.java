package com.vixr.log_analyzer.notification;

import com.vixr.log_analyzer.config.NotificationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;

@Component
public class TelegramNotifier {
    private static final String TELEGRAM_API_URL = "https://api.telegram.org/bot";

    private final NotificationConfig notificationConfig;

    private final RestTemplate restTemplate;

    @Autowired
    public TelegramNotifier(NotificationConfig notificationConfig) {
        this.notificationConfig = notificationConfig;
        this.restTemplate = new RestTemplate();
    }

    public void sendNotification(String message) throws IOException {
        String url = TELEGRAM_API_URL + notificationConfig.getTelegramBotToken()
            + "/sendMessage?chat_id="
            + notificationConfig.getTelegramChatId() + "&text=" + message;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Notification sent to Telegram.");
            } else {
                System.out.println("Failed to send notification to Telegram: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("Error sending message to Telegram: " + e.getMessage());
        }
    }

    public void sendFile(File file) {
        String url = TELEGRAM_API_URL + notificationConfig.getTelegramBotToken() + "/sendDocument";

        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("chat_id", notificationConfig.getTelegramChatId());
            body.add("document", new FileSystemResource(file));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("File sent to Telegram successfully.");
            } else {
                System.out.println("Failed to send file to Telegram: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("Error sending file to Telegram: " + e.getMessage());
        }
    }
}
