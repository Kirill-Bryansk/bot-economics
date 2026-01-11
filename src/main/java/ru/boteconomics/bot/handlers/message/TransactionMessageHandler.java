package ru.boteconomics.bot.handlers.message;

import org.springframework.stereotype.Component;
import ru.boteconomics.bot.domain.Transaction;
import ru.boteconomics.bot.domain.TransactionType;
import ru.boteconomics.bot.handlers.HandlerResult;
import ru.boteconomics.bot.keyboard.InlineKeyboardFactory;
import ru.boteconomics.bot.keyboard.MainKeyboardFactory;
import ru.boteconomics.bot.service.HistoryService;
import ru.boteconomics.bot.service.TransactionService;
import ru.boteconomics.bot.state.UserStateService;

import java.math.BigDecimal;

@Component
public class TransactionMessageHandler extends BaseMessageHandler {

    private final InlineKeyboardFactory inlineKeyboardFactory;
    private final TransactionService transactionService;

    public TransactionMessageHandler(MainKeyboardFactory mainKeyboardFactory,
                                     HistoryService historyService,
                                     ValidationService validationService,
                                     UserStateService userStateService,
                                     InlineKeyboardFactory inlineKeyboardFactory,
                                     TransactionService transactionService) {
        super(mainKeyboardFactory, historyService, validationService, userStateService);
        this.inlineKeyboardFactory = inlineKeyboardFactory;
        this.transactionService = transactionService;
    }

    @Override
    public boolean canHandle(Long chatId, String text) {
        UserStateService.State state = userStateService.getState(chatId);
        return state == UserStateService.State.WAITING_AMOUNT ||
               state == UserStateService.State.EDITING_TRANSACTION ||
               text.equals("💸 Добавить расход") ||
               text.equals("💰 Добавить доход");
    }

    @Override
    public HandlerResult handle(Long chatId, String text) {
        UserStateService.State state = userStateService.getState(chatId);

        if (text.equals("💸 Добавить расход")) {
            return startAddExpense(chatId);
        } else if (text.equals("💰 Добавить доход")) {
            return startAddIncome(chatId);
        } else if (state == UserStateService.State.WAITING_AMOUNT) {
            return handleAmountInput(chatId, text);
        } else if (state == UserStateService.State.EDITING_TRANSACTION) {
            return handleEditAmount(chatId, text);
        }

        return error("Неизвестная команда транзакции");
    }

    private HandlerResult startAddExpense(Long chatId) {
        userStateService.startAddExpense(chatId);
        return withKeyboard(
                "💸 Введите сумму расхода:\n(Например: 500 или 1000.50)",
                inlineKeyboardFactory.createCancelButton()
        );
    }

    private HandlerResult startAddIncome(Long chatId) {
        userStateService.startAddIncome(chatId);
        return withKeyboard(
                "💰 Введите сумму дохода:\n(Например: 50000 или 75000.50)",
                inlineKeyboardFactory.createCancelButton()
        );
    }

    private HandlerResult handleAmountInput(Long chatId, String text) {
        // ЯВНО УКАЗЫВАЕМ ТИП: ValidationService.AmountValidationResult
        ValidationService.AmountValidationResult validation = validationService.validateAmount(text);
        if (!validation.isValid()) {
            return withKeyboard(validation.errorMessage(), inlineKeyboardFactory.createCancelButton());
        }

        double amount = validation.value();
        userStateService.setAmount(chatId, amount);

        if (userStateService.getActionType(chatId) == UserStateService.ActionType.ADD_EXPENSE) {
            return withKeyboard(
                    "💸 Выберите категорию расхода:",
                    inlineKeyboardFactory.createCategories(TransactionType.EXPENSE)
            );
        } else {
            return withKeyboard(
                    "💰 Выберите категорию дохода:",
                    inlineKeyboardFactory.createCategories(TransactionType.INCOME)
            );
        }
    }

    private HandlerResult handleEditAmount(Long chatId, String text) {
        // ЯВНО УКАЗЫВАЕМ ТИП: ValidationService.AmountValidationResult
        ValidationService.AmountValidationResult validation = validationService.validateAmount(text);
        if (!validation.isValid()) {
            return withKeyboard(validation.errorMessage(), inlineKeyboardFactory.createCancelButton());
        }

        double newAmount = validation.value();
        Long transactionId = userStateService.getEditingTransactionId(chatId);

        if (transactionId == null) {
            return error("Ошибка: ID транзакции не найден");
        }

        Transaction transaction = historyService.getTransactionForUser(chatId, transactionId);
        if (transaction == null) {
            return error("Транзакция не найдена");
        }

        // Обновляем сумму транзакции
        transactionService.updateTransaction(
                transactionId,
                BigDecimal.valueOf(newAmount),
                transaction.getCategory()
        );

        String response = String.format(
                "✅ Сумма обновлена!\n\n" +
                "📋 Транзакция #%d\n" +
                "💵 Новая сумма: %.2f руб.\n" +
                "🏷️ Категория: %s\n\n" +
                "Используйте меню для новых операций 👇",
                transactionId, newAmount, transaction.getCategory().getFullName()
        );

        userStateService.reset(chatId);
        return mainMenu(response);
    }
}