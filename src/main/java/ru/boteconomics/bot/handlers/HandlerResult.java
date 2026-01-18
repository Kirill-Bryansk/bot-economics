package ru.boteconomics.bot.handlers;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class HandlerResult {
    private final String message;
    private final ReplyKeyboardMarkup keyboard;
    private final String nextStateId; // ID следующего состояния или null

    public HandlerResult(String message, ReplyKeyboardMarkup keyboard, String nextStateId) {
        this.message = message;
        this.keyboard = keyboard;
        this.nextStateId = nextStateId;
    }

    // Геттеры
    public String getMessage() { return message; }
    public ReplyKeyboardMarkup getKeyboard() { return keyboard; }
    public String getNextStateId() { return nextStateId; }

    // Вспомогательные фабричные методы
    public static HandlerResult stay(String message, ReplyKeyboardMarkup keyboard) {
        return new HandlerResult(message, keyboard, null);
    }

    public static HandlerResult next(String message, ReplyKeyboardMarkup keyboard, String nextStateId) {
        return new HandlerResult(message, keyboard, nextStateId);
    }
}