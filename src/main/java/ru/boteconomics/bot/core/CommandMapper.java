package ru.boteconomics.bot.core;

import org.springframework.stereotype.Component;

@Component
public class CommandMapper {

    public String toCallback(String text) {
        return switch (text) {
            case "/start", "🏠 Главное меню" -> "MAIN_MENU";
            case "💸 Добавить расход" -> "ADD_EXPENSE";
            case "📋 История операций" -> "HISTORY";
            case "📊 Статистика" -> "STATISTICS";
            default -> null;
        };
    }

    public boolean isKnownCommand(String text) {
        return toCallback(text) != null;
    }
}