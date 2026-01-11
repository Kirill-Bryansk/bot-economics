package ru.boteconomics.bot.service;

import org.springframework.stereotype.Service;
import ru.boteconomics.bot.domain.Category;
import ru.boteconomics.bot.domain.Transaction;
import ru.boteconomics.bot.domain.TransactionType;
import ru.boteconomics.bot.domain.Child;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final TransactionService transactionService;

    public ReportService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public String generateDailyReport(Long telegramUserId) {
        List<Transaction> todayTransactions = transactionService.getTodayTransactions(telegramUserId);
        Map<TransactionType, BigDecimal> summary = transactionService.getTodaySummary(telegramUserId);
        Map<Category, BigDecimal> expensesByCategory = transactionService.getTodayExpensesByCategory(telegramUserId);

        if (todayTransactions.isEmpty()) {
            return "📊 За сегодня операций нет";
        }

        StringBuilder report = new StringBuilder();
        report.append("📊 ОТЧЕТ ЗА СЕГОДНЯ (").append(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))).append(")\n\n");

        // Итоги по типам
        report.append("📈 ИТОГИ:\n");
        report.append(String.format("💰 Доходы: %.2f руб.\n", summary.get(TransactionType.INCOME)));
        report.append(String.format("💸 Расходы: %.2f руб.\n", summary.get(TransactionType.EXPENSE)));
        report.append(String.format("📊 Баланс: %.2f руб.\n\n",
                summary.get(TransactionType.INCOME).subtract(summary.get(TransactionType.EXPENSE))));

        // Расходы по категориям
        if (!expensesByCategory.isEmpty()) {
            report.append("📋 РАСХОДЫ ПО КАТЕГОРИЯМ:\n");

            // Сортируем по убыванию суммы
            expensesByCategory.entrySet().stream()
                    .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                    .forEach(entry -> {
                        Category category = entry.getKey();
                        BigDecimal amount = entry.getValue();
                        BigDecimal percentage = calculatePercentage(amount, summary.get(TransactionType.EXPENSE));

                        report.append(String.format("%s %.2f руб. (%.1f%%)\n",
                                category.getFullName(), amount, percentage));
                    });
            report.append("\n");
        }

        // Последние операции
        report.append("🔄 ПОСЛЕДНИЕ ОПЕРАЦИИ:\n");
        todayTransactions.stream()
                .limit(5)
                .forEach(t -> {
                    String emoji = t.getType() == TransactionType.INCOME ? "➕" : "➖";
                    report.append(String.format("%s %.2f руб. - %s\n",
                            emoji, t.getAmount(), t.getCategory().getFullName()));
                });

        return report.toString();
    }

    public String generateMonthlyReport(Long telegramUserId) {
        List<Transaction> monthTransactions = transactionService.getCurrentMonthTransactions(telegramUserId);
        Map<Category, BigDecimal> expensesByCategory = transactionService.getMonthExpensesByCategory(telegramUserId);

        if (monthTransactions.isEmpty()) {
            return "📈 За текущий месяц операций нет";
        }

        // Считаем статистику
        BigDecimal totalIncome = monthTransactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = monthTransactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance = totalIncome.subtract(totalExpense);

        // Группируем по дням для графика
        Map<LocalDate, BigDecimal> dailyExpenses = monthTransactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(
                        t -> t.getCreatedAt().toLocalDate(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add
                        )
                ));

        StringBuilder report = new StringBuilder();
        report.append("📈 ОТЧЕТ ЗА МЕСЯЦ (").append(LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM yyyy"))).append(")\n\n");

        // Общая статистика
        report.append("📊 ОБЩАЯ СТАТИСТИКА:\n");
        report.append(String.format("💰 Доходы: %.2f руб.\n", totalIncome));
        report.append(String.format("💸 Расходы: %.2f руб.\n", totalExpense));
        report.append(String.format("📈 Баланс: %.2f руб.\n\n", balance));

        // Топ категорий расходов
        if (!expensesByCategory.isEmpty()) {
            report.append("🏆 ТОП-5 КАТЕГОРИЙ РАСХОДОВ:\n");

            expensesByCategory.entrySet().stream()
                    .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                    .limit(5)
                    .forEach(entry -> {
                        Category category = entry.getKey();
                        BigDecimal amount = entry.getValue();
                        BigDecimal percentage = calculatePercentage(amount, totalExpense);

                        report.append(String.format("%s %.2f руб. (%.1f%%)\n",
                                category.getFullName(), amount, percentage));
                    });
            report.append("\n");
        }

        // Дети
        report.append("👨‍👩‍👧‍👦 РАСХОДЫ НА ДЕТЕЙ:\n");
        for (Child child : Child.values()) {
            BigDecimal childExpenses = transactionService.getChildExpenses(telegramUserId, child.name());
            if (childExpenses.compareTo(BigDecimal.ZERO) > 0) {
                report.append(String.format("%s %.2f руб.\n", child.getFullName(), childExpenses));
            }
        }

        // Средний дневной расход
        if (!dailyExpenses.isEmpty()) {
            BigDecimal avgDailyExpense = dailyExpenses.values().stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(dailyExpenses.size()), 2, RoundingMode.HALF_UP);

            report.append(String.format("\n📅 Средний дневной расход: %.2f руб.", avgDailyExpense));
        }

        return report.toString();
    }

    private BigDecimal calculatePercentage(BigDecimal part, BigDecimal total) {
        if (total.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return part.multiply(BigDecimal.valueOf(100))
                .divide(total, 1, RoundingMode.HALF_UP);
    }
}