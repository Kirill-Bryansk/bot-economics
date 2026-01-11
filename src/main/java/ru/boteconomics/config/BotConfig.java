package ru.boteconomics.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfig {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.name}")
    private String botName;

    @Value("${anna.telegram.id}")
    private Long annaTelegramId;

    public String getBotToken() {
        return botToken;
    }

    public String getBotName() {
        return botName;
    }

    public Long getAnnaTelegramId() {
        return annaTelegramId;
    }

    public boolean isAnna(Long userId) {
        return annaTelegramId.equals(userId);
    }
}