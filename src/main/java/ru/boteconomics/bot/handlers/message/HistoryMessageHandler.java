package ru.boteconomics.bot.handlers.message;

import org.springframework.stereotype.Component;
import ru.boteconomics.bot.domain.Transaction;
import ru.boteconomics.bot.handlers.HandlerResult;
import ru.boteconomics.bot.keyboard.HistoryKeyboardFactory;
import ru.boteconomics.bot.keyboard.MainKeyboardFactory;
import ru.boteconomics.bot.service.HistoryService;
import ru.boteconomics.bot.state.UserStateService;

import java.util.List;

@Component
public class HistoryMessageHandler extends BaseMessageHandler {

    private final HistoryKeyboardFactory historyKeyboardFactory;

    public HistoryMessageHandler(MainKeyboardFactory mainKeyboardFactory,
                                 HistoryService historyService,
                                 ValidationService validationService,
                                 UserStateService userStateService,
                                 HistoryKeyboardFactory historyKeyboardFactory) {
        super(mainKeyboardFactory, historyService, validationService, userStateService);
        this.historyKeyboardFactory = historyKeyboardFactory;
    }

    @Override
    public boolean canHandle(Long chatId, String text) {
        return text.equals("📋 История операций");
    }

    @Override
    public HandlerResult handle(Long chatId, String text) {
        return showHistoryPage(chatId, 0);
    }

    private HandlerResult showHistoryPage(Long chatId, int page) {
        try {
            List<Transaction> transactions = historyService.getPage(chatId, page);
            int totalPages = historyService.getTotalPages(chatId);

            String message = historyService.formatPage(transactions, page, totalPages);
            var keyboard = historyKeyboardFactory.createHistoryPage(transactions, page, totalPages);

            return withKeyboard(message, keyboard);
        } catch (Exception e) {
            e.printStackTrace();
            return error("Ошибка при загрузке истории операций");
        }
    }
}