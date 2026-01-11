package ru.boteconomics.bot.handlers;

import org.springframework.stereotype.Component;
import ru.boteconomics.config.BotConfig;

@Component
public class AccessHandler {

    private final BotConfig botConfig;

    public AccessHandler(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    public boolean isAnna(Long userId) {
        return userId != null && botConfig.isAnna(userId);
    }

    public String getAccessDeniedMessage(Long chatId) {
        return "🚫 Доступ запрещен\n\n" +
               "Это приватный бот для учета семейных финансов.\n" +
               "Обратись к разработчику для получения доступа.";
    }
}