 package ru.boteconomics.bot.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class MessageSender {

    private TelegramLongPollingBot bot;

    /**
     * Установить бота (вызывается при инициализации)
     */
    public void setBot(TelegramLongPollingBot bot) {
        this.bot = bot;
        log.info("MessageSender: бот установлен");
    }

    /**
     * Отправить сообщение (новая сигнатура - без передачи бота)
     */
    public void send(Long chatId, String text, ReplyKeyboard keyboard) {
        if (chatId == null || text == null || text.isEmpty()) {
            log.warn("Попытка отправить пустое сообщение chatId={}", chatId);
            return;
        }

        if (bot == null) {
            log.error("Бот не установлен в MessageSender!");
            return;
        }

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        if (keyboard != null) {
            message.setReplyMarkup(keyboard);
        }

        try {
            bot.execute(message);
            logMessage(chatId, text, true);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения chatId={}: {}", chatId, e.getMessage());
            logMessage(chatId, text, false);
        }
    }

    /**
     * Отправить сообщение без клавиатуры
     */
    public void send(Long chatId, String text) {
        send(chatId, text, null);
    }

    /**
     * Отправить BotResponse
     */
    public void send(Long chatId, ru.boteconomics.bot.core.response.BotResponse response) {
        if (response == null) {
            log.warn("Попытка отправить null BotResponse chatId={}", chatId);
            return;
        }
        send(chatId, response.getMessage(), response.getKeyboard());
    }

    private void logMessage(Long chatId, String text, boolean success) {
        String preview = text.length() > 50 ? text.substring(0, 50) + "..." : text;
        String status = success ? "УСПЕШНО" : "ОШИБКА";
        log.debug("Отправка сообщения chatId={} [{}]: {}", chatId, status, preview);
    }
}