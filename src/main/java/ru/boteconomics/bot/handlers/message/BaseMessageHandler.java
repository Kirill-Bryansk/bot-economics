package ru.boteconomics.bot.handlers.message;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.boteconomics.bot.handlers.HandlerResult;
import ru.boteconomics.bot.keyboard.MainKeyboardFactory;
import ru.boteconomics.bot.service.HistoryService;
import ru.boteconomics.bot.state.UserStateService;

public abstract class BaseMessageHandler {

    protected final MainKeyboardFactory mainKeyboardFactory;
    protected final HistoryService historyService;
    protected final ValidationService validationService;
    protected final UserStateService userStateService;

    protected BaseMessageHandler(MainKeyboardFactory mainKeyboardFactory,
                                 HistoryService historyService,
                                 ValidationService validationService,
                                 UserStateService userStateService) {
        this.mainKeyboardFactory = mainKeyboardFactory;
        this.historyService = historyService;
        this.validationService = validationService;
        this.userStateService = userStateService;
    }

    // Общие методы для всех обработчиков
    protected HandlerResult error(String message) {
        return new HandlerResult("❌ " + message, mainKeyboardFactory.createMainMenu());
    }

    protected HandlerResult success(String message) {
        return new HandlerResult("✅ " + message, mainKeyboardFactory.createMainMenu());
    }

    protected HandlerResult withKeyboard(String message, ReplyKeyboard keyboard) {
        return new HandlerResult(message, keyboard);
    }

    protected HandlerResult mainMenu(String message) {
        return new HandlerResult(message, mainKeyboardFactory.createMainMenu());
    }

    // Абстрактные методы
    public abstract boolean canHandle(Long chatId, String text);
    public abstract HandlerResult handle(Long chatId, String text);
}