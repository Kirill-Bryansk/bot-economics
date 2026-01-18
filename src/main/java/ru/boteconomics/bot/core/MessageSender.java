package ru.boteconomics.bot.core;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class MessageSender {

    public void send(TelegramLongPollingBot bot, Long chatId, String text, ReplyKeyboard keyboard) {
        if (chatId == null || text == null || text.isEmpty()) return;

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        if (keyboard != null) {
            message.setReplyMarkup(keyboard);
        }

        try {
            bot.execute(message);
            log(chatId, text);
        } catch (TelegramApiException e) {
            System.err.println("Ошибка отправки сообщения: " + e.getMessage());
        }
    }

    private void log(Long chatId, String text) {
        String preview = text.length() > 50 ? text.substring(0, 50) + "..." : text;
        System.out.println("Отправлено сообщение в чат " + chatId + ": " + preview);
    }
}