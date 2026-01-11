package ru.boteconomics.bot.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.boteconomics.bot.domain.Category;
import ru.boteconomics.bot.domain.Transaction;
import ru.boteconomics.bot.domain.TransactionType;
import ru.boteconomics.bot.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction saveTransaction(Long telegramUserId, TransactionType type,
                                       BigDecimal amount, Category category, String description) {
        Transaction transaction = new Transaction();
        transaction.setTelegramUserId(telegramUserId);
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setCategory(category);
        transaction.setDescription(description);

        return transactionRepository.save(transaction);
    }

    // НОВЫЙ МЕТОД: Обновление транзакции
    @Transactional
    public void updateTransaction(Long transactionId, BigDecimal newAmount, Category newCategory) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Транзакция не найдена"));

        transaction.setAmount(newAmount);
        transaction.setCategory(newCategory);
        transactionRepository.save(transaction);
    }

    // НОВЫЙ МЕТОД: Удаление транзакции
    @Transactional
    public void deleteTransaction(Long transactionId) {
        transactionRepository.deleteById(transactionId);
    }

    // НОВЫЙ МЕТОД: Поиск транзакции по ID
    @Transactional(readOnly = true)
    public Transaction findById(Long transactionId) {
        return transactionRepository.findById(transactionId).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getUserTransactions(Long telegramUserId) {
        return transactionRepository.findByTelegramUserIdOrderByCreatedAtDesc(telegramUserId);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTodayTransactions(Long telegramUserId) {
        return transactionRepository.findTodayTransactions(telegramUserId);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getCurrentMonthTransactions(Long telegramUserId) {
        return transactionRepository.findCurrentMonthTransactions(telegramUserId);
    }

    @Transactional(readOnly = true)
    public Map<TransactionType, BigDecimal> getTodaySummary(Long telegramUserId) {
        List<Transaction> todayTransactions = getTodayTransactions(telegramUserId);

        BigDecimal totalIncome = todayTransactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = todayTransactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Map.of(
                TransactionType.INCOME, totalIncome,
                TransactionType.EXPENSE, totalExpense
        );
    }

    @Transactional(readOnly = true)
    public Map<Category, BigDecimal> getTodayExpensesByCategory(Long telegramUserId) {
        return getTodayTransactions(telegramUserId).stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add
                        )
                ));
    }

    @Transactional(readOnly = true)
    public Map<Category, BigDecimal> getMonthExpensesByCategory(Long telegramUserId) {
        return getCurrentMonthTransactions(telegramUserId).stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add
                        )
                ));
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalBalance(Long telegramUserId) {
        List<Transaction> allTransactions = getUserTransactions(telegramUserId);

        BigDecimal totalIncome = allTransactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = allTransactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalIncome.subtract(totalExpense);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByDateRange(Long telegramUserId,
                                                        LocalDate startDate,
                                                        LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        return transactionRepository.findByTelegramUserIdAndCreatedAtBetween(
                telegramUserId, start, end);
    }

    @Transactional(readOnly = true)
    public BigDecimal getChildExpenses(Long telegramUserId, String childName) {
        return getCurrentMonthTransactions(telegramUserId).stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .filter(t -> {
                    Category category = t.getCategory();
                    return Category.isChildCategory(category) &&
                           Category.extractChildFromCategory(category).name().equals(childName);
                })
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}