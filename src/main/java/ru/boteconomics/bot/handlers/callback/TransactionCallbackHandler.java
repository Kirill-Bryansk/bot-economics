package ru.boteconomics.bot.handlers.callback;

import org.springframework.stereotype.Component;
import ru.boteconomics.bot.handlers.HandlerResult;
import ru.boteconomics.bot.keyboard.HistoryKeyboardFactory;
import ru.boteconomics.bot.keyboard.MainKeyboardFactory;
import ru.boteconomics.bot.service.HistoryService;
import ru.boteconomics.bot.service.TransactionService;
import ru.boteconomics.bot.state.UserStateService;


@Component
public class TransactionCallbackHandler extends BaseCallbackHandler {

    private final TransactionService transactionService;
    private final UserStateService userStateService;
    private final HistoryKeyboardFactory historyKeyboardFactory;

    public TransactionCallbackHandler(HistoryService historyService,
                                      TransactionService transactionService,
                                      UserStateService userStateService,
                                      HistoryKeyboardFactory historyKeyboardFactory,
                                      MainKeyboardFactory mainKeyboardFactory) {
        super(historyService, mainKeyboardFactory);
        this.transactionService = transactionService;
        this.userStateService = userStateService;
        this.historyKeyboardFactory = historyKeyboardFactory;
    }

    @Override
    public boolean canHandle(String callbackData) {
        return callbackData.startsWith("EDIT_") ||
               callbackData.startsWith("DELETE_") ||
               callbackData.startsWith("CONFIRM_DELETE_");
    }

    @Override
    public HandlerResult handle(Long chatId, String callbackData) {
        if (callbackData.startsWith("EDIT_AMOUNT_")) {
            return handleEditAmount(chatId, callbackData);
        } else if (callbackData.startsWith("EDIT_CATEGORY_")) {
            return handleEditCategory(chatId, callbackData);
        } else if (callbackData.startsWith("DELETE_")) {
            return handleDelete(chatId, callbackData);
        } else if (callbackData.startsWith("CONFIRM_DELETE_")) {
            return handleConfirmDelete(chatId, callbackData);
        }

        return error("Неизвестная команда транзакции");
    }

    private HandlerResult handleEditAmount(Long chatId, String callbackData) {
        return handleTransactionOperation(chatId, callbackData, transaction -> {
            // Начинаем редактирование суммы
            userStateService.startEditingTransaction(chatId, transaction.getId());

            String message = "✏️ Введите новую сумму для транзакции:\n\n" +
                             "Текущая сумма: " + transaction.getAmount() + "₽\n" +
                             "Категория: " + transaction.getCategory().getFullName();

            var keyboard = historyKeyboardFactory.createBackToTransactionMenu(transaction.getId());

            return withKeyboard(message, keyboard);
        });
    }

    private HandlerResult handleEditCategory(Long chatId, String callbackData) {
        return handleTransactionOperation(chatId, callbackData, transaction -> {
            // Начинаем редактирование категории
            userStateService.startEditingTransaction(chatId, transaction.getId());

            String message = "🔄 Выберите новую категорию:\n\n" +
                             "Текущая категория: " + transaction.getCategory().getFullName() + "\n" +
                             "Сумма: " + transaction.getAmount() + "₽";

            var keyboard = historyKeyboardFactory.createBackToTransactionMenu(transaction.getId());

            return withKeyboard(message, keyboard);
        });
    }

    private HandlerResult handleDelete(Long chatId, String callbackData) {
        return handleTransactionOperation(chatId, callbackData, transaction -> {
            String message = "⚠️ Вы уверены, что хотите удалить транзакцию?\n\n" +
                             historyService.formatTransactionDetails(transaction);

            var keyboard = historyKeyboardFactory.createDeleteConfirmation(transaction.getId());

            return withKeyboard(message, keyboard);
        });
    }

    private HandlerResult handleConfirmDelete(Long chatId, String callbackData) {
        return handleTransactionOperation(chatId, callbackData, transaction -> {
            // Удаляем транзакцию
            transactionService.deleteTransaction(transaction.getId());

            String message = "✅ Транзакция успешно удалена";
            var keyboard = historyKeyboardFactory.createBackToHistory();

            return withKeyboard(message, keyboard);
        });
    }
}