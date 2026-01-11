package ru.boteconomics.bot.service;

import org.springframework.stereotype.Service;
import ru.boteconomics.bot.domain.Transaction;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class HistoryService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM HH:mm");

    private final TransactionService transactionService;

    public HistoryService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Получить страницу истории (3 транзакции)
     */
    public List<Transaction> getPage(Long userId, int page) {
        List<Transaction> allTransactions = transactionService.getUserTransactions(userId);

        int start = page * 3; // по 3 транзакции на страницу
        int end = Math.min(start + 3, allTransactions.size());

        if (start >= allTransactions.size()) {
            return List.of(); // пустая страница
        }

        return allTransactions.subList(start, end);
    }

    /**
     * Получить общее количество страниц
     */
    public int getTotalPages(Long userId) {
        List<Transaction> allTransactions = transactionService.getUserTransactions(userId);
        return (int) Math.ceil((double) allTransactions.size() / 3);
    }

    /**
     * Форматировать страницу истории
     */
    public String formatPage(List<Transaction> transactions, int currentPage, int totalPages) {
        if (transactions.isEmpty()) {
            return "📭 На этой странице нет операций.\n\nСтраница " +
                   (currentPage + 1) + "/" + (totalPages == 0 ? 1 : totalPages);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("📋 История операций\n\n");

        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            String emoji = t.getType().name().equals("INCOME") ? "➕" : "➖";
            sb.append(String.format("%d. %s %.2f₽\n",
                    i + 1, emoji, t.getAmount()));
            sb.append("   ").append(t.getCategory().getFullName()).append("\n");
            sb.append("   📅 ").append(t.getCreatedAt().format(DATE_FORMATTER)).append("\n\n");
        }

        sb.append("Страница ").append(currentPage + 1).append("/").append(totalPages);
        return sb.toString();
    }

    /**
     * Получить транзакцию по ID (с проверкой принадлежности пользователю)
     */
    public Transaction getTransactionForUser(Long userId, Long transactionId) {
        return transactionService.getUserTransactions(userId).stream()
                .filter(t -> t.getId().equals(transactionId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Форматировать детали транзакции
     */
    public String formatTransactionDetails(Transaction transaction) {
        if (transaction == null) {
            return "❌ Транзакция не найдена";
        }

        String type = transaction.getType().name().equals("INCOME") ? "➕ Доход" : "➖ Расход";
        String date = transaction.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

        return String.format(
                "📋 Транзакция #%d\n\n" +
                "📅 Дата: %s\n" +
                "💰 Тип: %s\n" +
                "💵 Сумма: %.2f₽\n" +
                "🏷️ Категория: %s\n\n" +
                "Выберите действие:",
                transaction.getId(),
                date,
                type,
                transaction.getAmount(),
                transaction.getCategory().getFullName()
        );
    }

    /**
     * Проверить, есть ли следующая страница
     */
    public boolean hasNextPage(Long userId, int currentPage) {
        return (currentPage + 1) < getTotalPages(userId);
    }

    /**
     * Проверить, есть ли предыдущая страница
     */
    public boolean hasPreviousPage(int currentPage) {
        return currentPage > 0;
    }
}