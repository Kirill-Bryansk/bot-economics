package ru.boteconomics.bot.handlers.callback;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.boteconomics.bot.domain.Transaction;
import ru.boteconomics.bot.handlers.HandlerResult;
import ru.boteconomics.bot.keyboard.MainKeyboardFactory;
import ru.boteconomics.bot.service.HistoryService;

import java.util.function.Function;

public abstract class BaseCallbackHandler {

    protected final HistoryService historyService;
    protected final MainKeyboardFactory mainKeyboardFactory;

    protected BaseCallbackHandler(HistoryService historyService,
                                  MainKeyboardFactory mainKeyboardFactory) {
        this.historyService = historyService;
        this.mainKeyboardFactory = mainKeyboardFactory;
    }

    /**
     * Единый метод для обработки операций с транзакцией
     */
    protected HandlerResult handleTransactionOperation(
            Long chatId,
            String callbackData,
            Function<Transaction, HandlerResult> operation) {

        try {
            Long transactionId = extractTransactionId(callbackData);
            Transaction transaction = historyService.getTransactionForUser(chatId, transactionId);

            if (transaction == null) {
                return transactionNotFound();
            }

            return operation.apply(transaction);

        } catch (NumberFormatException e) {
            return invalidTransactionId();
        } catch (Exception e) {
            return error("Ошибка при обработке операции");
        }
    }

    /**
     * Извлечение ID транзакции из callback данных
     */
    protected Long extractTransactionId(String callbackData) {
        // Удаляем все до последнего подчеркивания
        String idStr = callbackData.substring(callbackData.lastIndexOf('_') + 1);
        return Long.parseLong(idStr);
    }

    /**
     * Стандартные ответы об ошибках
     */
    protected HandlerResult transactionNotFound() {
        return new HandlerResult("❌ Транзакция не найдена", mainKeyboardFactory.createMainMenu());
    }

    protected HandlerResult invalidTransactionId() {
        return new HandlerResult("❌ Неверный ID транзакции", mainKeyboardFactory.createMainMenu());
    }

    protected HandlerResult error(String message) {
        return new HandlerResult("❌ " + message, mainKeyboardFactory.createMainMenu());
    }

    protected HandlerResult success(String message) {
        return new HandlerResult("✅ " + message, mainKeyboardFactory.createMainMenu());
    }

    protected HandlerResult withKeyboard(String message, ReplyKeyboard keyboard) {
        return new HandlerResult(message, keyboard);
    }

    // Абстрактные методы
    public abstract boolean canHandle(String callbackData);
    public abstract HandlerResult handle(Long chatId, String callbackData);
}