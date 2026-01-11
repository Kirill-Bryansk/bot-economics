package ru.boteconomics.bot.handlers;

import org.springframework.stereotype.Component;
import ru.boteconomics.bot.domain.Category;
import ru.boteconomics.bot.domain.Child;
import ru.boteconomics.bot.domain.Transaction;
import ru.boteconomics.bot.domain.TransactionType;
import ru.boteconomics.bot.keyboard.HistoryKeyboardFactory;
import ru.boteconomics.bot.keyboard.InlineKeyboardFactory;
import ru.boteconomics.bot.keyboard.MainKeyboardFactory;
import ru.boteconomics.bot.service.HistoryService;
import ru.boteconomics.bot.service.TransactionService;
import ru.boteconomics.bot.state.UserStateService;

import java.math.BigDecimal;

@Component
public class CallbackHandler {

    private final MainKeyboardFactory mainKeyboardFactory;
    private final InlineKeyboardFactory inlineKeyboardFactory;
    private final UserStateService userStateService;
    private final TransactionService transactionService;
    private final HistoryService historyService;
    private final HistoryKeyboardFactory historyKeyboardFactory;

    public CallbackHandler(
            MainKeyboardFactory mainKeyboardFactory,
            InlineKeyboardFactory inlineKeyboardFactory,
            UserStateService userStateService,
            TransactionService transactionService,
            HistoryService historyService,
            HistoryKeyboardFactory historyKeyboardFactory) {
        this.mainKeyboardFactory = mainKeyboardFactory;
        this.inlineKeyboardFactory = inlineKeyboardFactory;
        this.userStateService = userStateService;
        this.transactionService = transactionService;
        this.historyService = historyService;
        this.historyKeyboardFactory = historyKeyboardFactory;
    }

    public HandlerResult handleCallback(Long chatId, String callbackData) {
        // Обработка истории операций
        if (callbackData.startsWith("HISTORY_PAGE_")) {
            return handleHistoryPage(chatId, callbackData);
        }
        if (callbackData.startsWith("VIEW_TRANSACTION_")) {
            return handleViewTransaction(chatId, callbackData);
        }
        if (callbackData.startsWith("EDIT_AMOUNT_")) {
            return handleEditAmount(chatId, callbackData);
        }
        if (callbackData.startsWith("EDIT_CATEGORY_")) {
            return handleEditCategory(chatId, callbackData);
        }
        if (callbackData.startsWith("DELETE_")) {
            return handleDelete(chatId, callbackData);
        }
        if (callbackData.startsWith("CONFIRM_DELETE_")) {
            return handleConfirmDelete(chatId, callbackData);
        }
        if (callbackData.equals("SHOW_HISTORY_0")) {
            return showHistoryPage(chatId, 0);
        }
        if (callbackData.equals("NO_ACTION")) {
            return new HandlerResult("", null); // Пустой ответ для неактивных кнопок
        }

        switch (callbackData) {
            case "CANCEL":
                return handleCancel(chatId);

            case "BACK_TO_CATEGORIES":
                return new HandlerResult(
                        "💸 Выберите категорию расхода:",
                        inlineKeyboardFactory.createCategories(TransactionType.EXPENSE)
                );

            case "BACK_TO_CHILDREN":
            case "SHOW_CHILDREN":
                return new HandlerResult(
                        "👨‍👩‍👧‍👦 Выберите ребенка:",
                        inlineKeyboardFactory.createChildrenCategories()
                );

            case "SHOW_ARTEMIY":
                userStateService.setChild(chatId, Child.ARTEMIY);
                return completeChildTransaction(chatId, Child.ARTEMIY);

            case "SHOW_ARINA":
                userStateService.setChild(chatId, Child.ARINA);
                return completeChildTransaction(chatId, Child.ARINA);

            case "SHOW_EKATERINA":
                userStateService.setChild(chatId, Child.EKATERINA);
                return completeChildTransaction(chatId, Child.EKATERINA);

            default:
                if (callbackData.startsWith("CATEGORY_")) {
                    return handleCategorySelection(chatId, callbackData);
                } else {
                    return new HandlerResult(
                            "Неизвестная операция",
                            mainKeyboardFactory.createMainMenu()
                    );
                }
        }
    }

    // ========== МЕТОДЫ ДЛЯ ИСТОРИИ ОПЕРАЦИЙ ==========

    private HandlerResult handleHistoryPage(Long chatId, String callbackData) {
        try {
            int page = Integer.parseInt(callbackData.replace("HISTORY_PAGE_", ""));
            return showHistoryPage(chatId, page);
        } catch (NumberFormatException e) {
            return new HandlerResult(
                    "❌ Ошибка номера страницы",
                    mainKeyboardFactory.createMainMenu()
            );
        }
    }

    private HandlerResult showHistoryPage(Long chatId, int page) {
        try {
            var transactions = historyService.getPage(chatId, page);
            int totalPages = historyService.getTotalPages(chatId);

            String message = historyService.formatPage(transactions, page, totalPages);
            var keyboard = historyKeyboardFactory.createHistoryPage(transactions, page, totalPages);

            return new HandlerResult(message, keyboard);
        } catch (Exception e) {
            e.printStackTrace();
            return new HandlerResult(
                    "❌ Ошибка при загрузке истории",
                    mainKeyboardFactory.createMainMenu()
            );
        }
    }

    private HandlerResult handleViewTransaction(Long chatId, String callbackData) {
        try {
            Long transactionId = Long.parseLong(callbackData.replace("VIEW_TRANSACTION_", ""));
            Transaction transaction = historyService.getTransactionForUser(chatId, transactionId);

            if (transaction == null) {
                return new HandlerResult(
                        "❌ Транзакция не найдена",
                        mainKeyboardFactory.createMainMenu()
                );
            }

            String message = historyService.formatTransactionDetails(transaction);
            var keyboard = historyKeyboardFactory.createTransactionMenu(transactionId);

            return new HandlerResult(message, keyboard);
        } catch (NumberFormatException e) {
            return new HandlerResult(
                    "❌ Ошибка ID транзакции",
                    mainKeyboardFactory.createMainMenu()
            );
        }
    }

    private HandlerResult handleEditAmount(Long chatId, String callbackData) {
        try {
            Long transactionId = Long.parseLong(callbackData.replace("EDIT_AMOUNT_", ""));
            Transaction transaction = historyService.getTransactionForUser(chatId, transactionId);

            if (transaction == null) {
                return new HandlerResult(
                        "❌ Транзакция не найдена",
                        mainKeyboardFactory.createMainMenu()
                );
            }

            // Начинаем редактирование суммы
            userStateService.startEditingTransaction(chatId, transactionId);

            String message = "✏️ Введите новую сумму для транзакции:\n\n" +
                             "Текущая сумма: " + transaction.getAmount() + "₽\n" +
                             "Категория: " + transaction.getCategory().getFullName();

            var keyboard = historyKeyboardFactory.createBackToTransactionMenu(transactionId);

            return new HandlerResult(message, keyboard);
        } catch (NumberFormatException e) {
            return new HandlerResult(
                    "❌ Ошибка ID транзакции",
                    mainKeyboardFactory.createMainMenu()
            );
        }
    }

    private HandlerResult handleEditCategory(Long chatId, String callbackData) {
        try {
            Long transactionId = Long.parseLong(callbackData.replace("EDIT_CATEGORY_", ""));
            Transaction transaction = historyService.getTransactionForUser(chatId, transactionId);

            if (transaction == null) {
                return new HandlerResult(
                        "❌ Транзакция не найдена",
                        mainKeyboardFactory.createMainMenu()
                );
            }

            // Показываем выбор категории (используем существующий интерфейс)
            userStateService.startEditingTransaction(chatId, transactionId);

            String message = "🔄 Выберите новую категорию:\n\n" +
                             "Текущая категория: " + transaction.getCategory().getFullName() + "\n" +
                             "Сумма: " + transaction.getAmount() + "₽";

            TransactionType type = transaction.getType();
            var keyboard = inlineKeyboardFactory.createCategories(type);

            return new HandlerResult(message, keyboard);
        } catch (NumberFormatException e) {
            return new HandlerResult(
                    "❌ Ошибка ID транзакции",
                    mainKeyboardFactory.createMainMenu()
            );
        }
    }

    private HandlerResult handleDelete(Long chatId, String callbackData) {
        try {
            Long transactionId = Long.parseLong(callbackData.replace("DELETE_", ""));
            Transaction transaction = historyService.getTransactionForUser(chatId, transactionId);

            if (transaction == null) {
                return new HandlerResult(
                        "❌ Транзакция не найдена",
                        mainKeyboardFactory.createMainMenu()
                );
            }

            String message = "⚠️ Вы уверены, что хотите удалить транзакцию?\n\n" +
                             historyService.formatTransactionDetails(transaction);

            var keyboard = historyKeyboardFactory.createDeleteConfirmation(transactionId);

            return new HandlerResult(message, keyboard);
        } catch (NumberFormatException e) {
            return new HandlerResult(
                    "❌ Ошибка ID транзакции",
                    mainKeyboardFactory.createMainMenu()
            );
        }
    }

    private HandlerResult handleConfirmDelete(Long chatId, String callbackData) {
        try {
            Long transactionId = Long.parseLong(callbackData.replace("CONFIRM_DELETE_", ""));

            // Удаляем транзакцию
            transactionService.deleteTransaction(transactionId);

            String message = "✅ Транзакция успешно удалена";
            var keyboard = historyKeyboardFactory.createBackToHistory();

            return new HandlerResult(message, keyboard);
        } catch (Exception e) {
            e.printStackTrace();
            return new HandlerResult(
                    "❌ Ошибка при удалении транзакции",
                    mainKeyboardFactory.createMainMenu()
            );
        }
    }

    // ========== СУЩЕСТВУЮЩИЕ МЕТОДЫ ==========

    private HandlerResult completeChildTransaction(Long chatId, Child child) {
        UserStateService.TransactionData data = userStateService.completeTransaction(chatId);
        Category category = data.category;
        return saveAndConfirmTransaction(chatId, data, category);
    }

    private HandlerResult handleCancel(Long chatId) {
        userStateService.reset(chatId);
        return new HandlerResult(
                "❌ Операция отменена",
                mainKeyboardFactory.createMainMenu()
        );
    }

    private HandlerResult handleCategorySelection(Long chatId, String callbackData) {
        Category category = Category.fromCallback(callbackData);

        if (category == null) {
            return new HandlerResult(
                    "❌ Ошибка: категория не найдена",
                    mainKeyboardFactory.createMainMenu()
            );
        }

        // 1. Сохраняем категорию в состояние
        userStateService.setCategory(chatId, category);

        // 2. Проверяем, нужно ли выбрать ребенка
        if (Category.isChildCategory(category)) {
            return new HandlerResult(
                    "👶 Выберите ребенка:",
                    inlineKeyboardFactory.createChildrenCategories()
            );
        }

        // 3. Если ребенок не нужен - завершаем транзакцию
        UserStateService.TransactionData data = userStateService.completeTransaction(chatId);
        return saveAndConfirmTransaction(chatId, data, category);
    }

    private HandlerResult saveAndConfirmTransaction(Long chatId,
                                                    UserStateService.TransactionData data,
                                                    Category category) {
        TransactionType type = data.actionType == UserStateService.ActionType.ADD_EXPENSE
                ? TransactionType.EXPENSE
                : TransactionType.INCOME;

        // Проверяем, редактируем ли существующую транзакцию
        if (data.actionType == UserStateService.ActionType.EDIT_TRANSACTION && data.transactionId != null) {
            // Обновляем существующую транзакцию
            transactionService.updateTransaction(data.transactionId, BigDecimal.valueOf(data.amount), category);

            String response = String.format("✅ Транзакция обновлена!\n\n" +
                                            "Сумма: %.2f руб.\n" +
                                            "Категория: %s\n\n" +
                                            "Используйте меню для новых операций 👇",
                    data.amount, category.getFullName());

            return new HandlerResult(response, mainKeyboardFactory.createMainMenu());
        } else {
            // Сохраняем новую транзакцию
            transactionService.saveTransaction(
                    chatId,
                    type,
                    BigDecimal.valueOf(data.amount),
                    category,
                    null
            );

            String response = String.format("✅ Успешно добавлено!\n\n" +
                                            "Тип: %s\n" +
                                            "Сумма: %.2f руб.\n" +
                                            "Категория: %s\n\n" +
                                            "Используйте меню для новых операций 👇",
                    data.actionType == UserStateService.ActionType.ADD_EXPENSE ? "Расход" : "Доход",
                    data.amount, category.getFullName());

            return new HandlerResult(response, mainKeyboardFactory.createMainMenu());
        }
    }
}