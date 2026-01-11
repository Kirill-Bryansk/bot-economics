package ru.boteconomics.bot.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.boteconomics.bot.domain.Transaction;
import ru.boteconomics.bot.domain.TransactionType;
import ru.boteconomics.bot.keyboard.HistoryKeyboardFactory;
import ru.boteconomics.bot.keyboard.InlineKeyboardFactory;
import ru.boteconomics.bot.keyboard.MainKeyboardFactory;
import ru.boteconomics.bot.service.HistoryService;
import ru.boteconomics.bot.service.ReportService;
import ru.boteconomics.bot.service.TransactionService;
import ru.boteconomics.bot.state.UserStateService;

import java.math.BigDecimal;
import java.util.List;

@Component
public class MessageHandler {

    private final MainKeyboardFactory mainKeyboardFactory;
    private final InlineKeyboardFactory inlineKeyboardFactory;
    private final UserStateService userStateService;
    private final ReportService reportService;
    private final HistoryService historyService;
    private final HistoryKeyboardFactory historyKeyboardFactory;
    private final TransactionService transactionService; // ← ДОБАВИТЬ

    public MessageHandler(
            MainKeyboardFactory mainKeyboardFactory,
            InlineKeyboardFactory inlineKeyboardFactory,
            UserStateService userStateService,
            ReportService reportService,
            HistoryService historyService,
            HistoryKeyboardFactory historyKeyboardFactory,
            TransactionService transactionService) { // ← ДОБАВИТЬ
        this.mainKeyboardFactory = mainKeyboardFactory;
        this.inlineKeyboardFactory = inlineKeyboardFactory;
        this.userStateService = userStateService;
        this.reportService = reportService;
        this.historyService = historyService;
        this.historyKeyboardFactory = historyKeyboardFactory;
        this.transactionService = transactionService; // ← ДОБАВИТЬ
    }

    public HandlerResult handleMessage(Long chatId, String text) {
        String response;
        ReplyKeyboard keyboard = null;

        switch (text) {
            case "/start":
            case "🏠 Главное меню":
                response = "👋 Привет, Анна!\nВыберите действие:";
                keyboard = mainKeyboardFactory.createMainMenu();
                userStateService.reset(chatId);
                break;

            case "💸 Добавить расход":
                response = "💸 Введите сумму расхода:\n(Например: 500 или 1000.50)";
                keyboard = inlineKeyboardFactory.createCancelButton();
                userStateService.startAddExpense(chatId);
                break;

            case "💰 Добавить доход":
                response = "💰 Введите сумму дохода:\n(Например: 50000 или 75000.50)";
                keyboard = inlineKeyboardFactory.createCancelButton();
                userStateService.startAddIncome(chatId);
                break;

            case "📊 Отчет за сегодня":
                response = reportService.generateDailyReport(chatId);
                keyboard = mainKeyboardFactory.createMainMenu();
                break;

            case "📈 Отчет за месяц":
                response = reportService.generateMonthlyReport(chatId);
                keyboard = mainKeyboardFactory.createMainMenu();
                break;

            case "📋 История операций":
                return showHistoryPage(chatId, 0);

            case "❓ Помощь":
                response = getHelpMessage();
                keyboard = mainKeyboardFactory.createMainMenu();
                break;

            default:
                return handleAmountInput(chatId, text);
        }

        return new HandlerResult(response, keyboard);
    }
    private HandlerResult showHistoryPage(Long chatId, int page) {
        try {
            List<Transaction> transactions = historyService.getPage(chatId, page);
            int totalPages = historyService.getTotalPages(chatId);

            String message = historyService.formatPage(transactions, page, totalPages);
            var keyboard = historyKeyboardFactory.createHistoryPage(transactions, page, totalPages);

            return new HandlerResult(message, keyboard);
        } catch (Exception e) {
            e.printStackTrace();
            return new HandlerResult(
                    "❌ Ошибка при загрузке истории операций",
                    mainKeyboardFactory.createMainMenu()
            );
        }
    }


    private HandlerResult handleAmountInput(Long chatId, String text) {
        UserStateService.State state = userStateService.getState(chatId);

        if (state == UserStateService.State.WAITING_AMOUNT) {
            // Добавление новой транзакции
            try {
                double amount = Double.parseDouble(text.replace(",", "."));
                if (amount <= 0) {
                    return new HandlerResult(
                            "❌ Сумма должна быть положительной!",
                            inlineKeyboardFactory.createCancelButton()
                    );
                }

                userStateService.setAmount(chatId, amount);

                if (userStateService.getActionType(chatId) == UserStateService.ActionType.ADD_EXPENSE) {
                    return new HandlerResult(
                            "💸 Выберите категорию расхода:",
                            inlineKeyboardFactory.createCategories(TransactionType.EXPENSE)
                    );
                } else {
                    return new HandlerResult(
                            "💰 Выберите категорию дохода:",
                            inlineKeyboardFactory.createCategories(TransactionType.INCOME)
                    );
                }
            } catch (NumberFormatException e) {
                return new HandlerResult(
                        "❌ Неверный формат суммы!\nВведите число (например: 500 или 1000.50)",
                        inlineKeyboardFactory.createCancelButton()
                );
            }
        }
        else if (state == UserStateService.State.EDITING_TRANSACTION) {
            // Редактирование суммы существующей транзакции
            try {
                double newAmount = Double.parseDouble(text.replace(",", "."));
                if (newAmount <= 0) {
                    return new HandlerResult(
                            "❌ Сумма должна быть положительной!",
                            inlineKeyboardFactory.createCancelButton()
                    );
                }

                Long transactionId = userStateService.getEditingTransactionId(chatId);
                if (transactionId == null) {
                    return new HandlerResult(
                            "❌ Ошибка: ID транзакции не найден",
                            mainKeyboardFactory.createMainMenu()
                    );
                }

                // Получаем текущую транзакцию
                Transaction transaction = historyService.getTransactionForUser(chatId, transactionId);
                if (transaction == null) {
                    return new HandlerResult(
                            "❌ Транзакция не найдена",
                            mainKeyboardFactory.createMainMenu()
                    );
                }

                // Обновляем сумму транзакции
                transactionService.updateTransaction(
                        transactionId,
                        BigDecimal.valueOf(newAmount),
                        transaction.getCategory()
                );

                String response = String.format("✅ Сумма обновлена!\n\n" +
                                                "📋 Транзакция #%d\n" +
                                                "💵 Новая сумма: %.2f руб.\n" +
                                                "🏷️ Категория: %s\n\n" +
                                                "Используйте меню для новых операций 👇",
                        transactionId, newAmount, transaction.getCategory().getFullName());

                userStateService.reset(chatId);
                return new HandlerResult(response, mainKeyboardFactory.createMainMenu());

            } catch (NumberFormatException e) {
                return new HandlerResult(
                        "❌ Неверный формат суммы!\nВведите число (например: 500 или 1000.50)",
                        inlineKeyboardFactory.createCancelButton()
                );
            }
        }
        else {
            return new HandlerResult(
                    "Неизвестная команда. Используйте меню ниже 👇",
                    mainKeyboardFactory.createMainMenu()
            );
        }
    }

    private String getHelpMessage() {
        return "❓ Помощь:\n\n" +
               "💸 Добавить расход - записать новую трату\n" +
               "💰 Добавить доход - записать новый доход\n" +
               "📊 Отчет за сегодня - статистика за день\n" +
               "📈 Отчет за месяц - статистика за месяц\n" +
               "📋 История операций - просмотр и редактирование всех операций\n" +
               "🏠 Главное меню - вернуться в основное меню";
    }
}