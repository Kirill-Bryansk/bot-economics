package ru.boteconomics.bot.handlers;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

public class HandlerResult {
    private final String response;
    private final ReplyKeyboard keyboard;

    public HandlerResult(String response, ReplyKeyboard keyboard) {
        this.response = response;
        this.keyboard = keyboard;
    }

    public String getResponse() {
        return response;
    }

    public ReplyKeyboard getKeyboard() {
        return keyboard;
    }
}