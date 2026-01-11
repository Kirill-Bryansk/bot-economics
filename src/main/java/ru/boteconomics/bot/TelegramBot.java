package ru.boteconomics.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.boteconomics.bot.handlers.AccessHandler;
import ru.boteconomics.bot.handlers.HandlerResult;
import ru.boteconomics.bot.handlers.message.MessageDispatcher; // ИЗМЕНЕНО: новый диспетчер
import ru.boteconomics.bot.handlers.callback.CallbackDispatcher;
import ru.boteconomics.config.BotConfig;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final AccessHandler accessHandler;
    private final MessageDispatcher messageDispatcher; // ИЗМЕНЕНО: MessageHandler → MessageDispatcher
    private final CallbackDispatcher callbackDispatcher;

    public TelegramBot(BotConfig botConfig, AccessHandler accessHandler,
                       MessageDispatcher messageDispatcher, // ИЗМЕНЕНО
                       CallbackDispatcher callbackDispatcher) {
        super(botConfig.getBotToken());
        this.botConfig = botConfig;
        this.accessHandler = accessHandler;
        this.messageDispatcher = messageDispatcher; // ИЗМЕНЕНО
        this.callbackDispatcher = callbackDispatcher;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = getChatId(update);

        // Проверка доступа
        if (!accessHandler.isAnna(chatId)) {
            sendMessage(chatId, accessHandler.getAccessDeniedMessage(chatId), null);
            return;
        }

        // Маршрутизация сообщений
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleTextMessage(chatId, update.getMessage().getText());
        } else if (update.hasCallbackQuery()) {
            handleCallback(chatId, update.getCallbackQuery().getData());
        }
    }

    private void handleTextMessage(Long chatId, String text) {
        HandlerResult result = messageDispatcher.dispatch(chatId, text); // ИЗМЕНЕНО: handleMessage → dispatch
        sendMessage(chatId, result.getResponse(), result.getKeyboard());
    }

    private void handleCallback(Long chatId, String callbackData) {
        HandlerResult result = callbackDispatcher.dispatch(chatId, callbackData);
        sendMessage(chatId, result.getResponse(), result.getKeyboard());
    }

    private Long getChatId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getChatId();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getChatId();
        }
        return null;
    }

    private void sendMessage(Long chatId, String text, ReplyKeyboard keyboard) {
        if (chatId == null) return;

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        if (keyboard != null) {
            message.setReplyMarkup(keyboard);
        }

        try {
            execute(message);
            logMessage(chatId, text);
        } catch (TelegramApiException e) {
            System.err.println("Ошибка отправки сообщения: " + e.getMessage());
        }
    }

    private void logMessage(Long chatId, String text) {
        String preview = text.length() > 50 ? text.substring(0, 50) + "..." : text;
        System.out.println("Отправлено сообщение в чат " + chatId + ": " + preview);
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }
}