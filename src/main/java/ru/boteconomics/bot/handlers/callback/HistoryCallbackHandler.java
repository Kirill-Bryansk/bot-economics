package ru.boteconomics.bot.handlers.callback;

import org.springframework.stereotype.Component;
import ru.boteconomics.bot.domain.Transaction;
import ru.boteconomics.bot.handlers.HandlerResult;
import ru.boteconomics.bot.keyboard.HistoryKeyboardFactory;
import ru.boteconomics.bot.keyboard.MainKeyboardFactory;
import ru.boteconomics.bot.service.HistoryService;

import java.util.List;

@Component
public class HistoryCallbackHandler extends BaseCallbackHandler {

    private final HistoryKeyboardFactory historyKeyboardFactory;

    public HistoryCallbackHandler(HistoryService historyService,
                                  HistoryKeyboardFactory historyKeyboardFactory,
                                  MainKeyboardFactory mainKeyboardFactory) {
        super(historyService, mainKeyboardFactory);
        this.historyKeyboardFactory = historyKeyboardFactory;
    }

    @Override
    public boolean canHandle(String callbackData) {
        return callbackData.startsWith("HISTORY_") ||
               callbackData.startsWith("VIEW_TRANSACTION_") ||
               callbackData.equals("SHOW_HISTORY_0");
    }

    @Override
    public HandlerResult handle(Long chatId, String callbackData) {
        if (callbackData.startsWith("HISTORY_PAGE_")) {
            return handleHistoryPage(chatId, callbackData);
        } else if (callbackData.startsWith("VIEW_TRANSACTION_")) {
            return handleViewTransaction(chatId, callbackData);
        } else if (callbackData.equals("SHOW_HISTORY_0")) {
            return showHistoryPage(chatId, 0);
        }

        return error("Неизвестная команда истории");
    }

    private HandlerResult handleHistoryPage(Long chatId, String callbackData) {
        try {
            int page = Integer.parseInt(callbackData.replace("HISTORY_PAGE_", ""));
            return showHistoryPage(chatId, page);
        } catch (NumberFormatException e) {
            return error("Ошибка номера страницы");
        }
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
            return error("Ошибка при загрузке истории");
        }
    }

    private HandlerResult handleViewTransaction(Long chatId, String callbackData) {
        return handleTransactionOperation(chatId, callbackData, transaction -> {
            String message = historyService.formatTransactionDetails(transaction);
            var keyboard = historyKeyboardFactory.createTransactionMenu(transaction.getId());
            return withKeyboard(message, keyboard);
        });
    }
}