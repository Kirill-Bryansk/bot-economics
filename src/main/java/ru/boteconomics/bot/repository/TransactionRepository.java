package ru.boteconomics.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.boteconomics.bot.domain.Transaction;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByTelegramUserIdOrderByCreatedAtDesc(Long telegramUserId);

    List<Transaction> findByTelegramUserIdAndCreatedAtBetween(
            Long telegramUserId,
            LocalDateTime start,
            LocalDateTime end);

    @Query("SELECT t FROM Transaction t " +
           "WHERE t.telegramUserId = :userId " +
           "AND DATE(t.createdAt) = CURRENT_DATE " +
           "ORDER BY t.createdAt DESC")
    List<Transaction> findTodayTransactions(@Param("userId") Long userId);

    @Query("SELECT t FROM Transaction t " +
           "WHERE t.telegramUserId = :userId " +
           "AND EXTRACT(MONTH FROM t.createdAt) = EXTRACT(MONTH FROM CURRENT_DATE) " +
           "AND EXTRACT(YEAR FROM t.createdAt) = EXTRACT(YEAR FROM CURRENT_DATE) " +
           "ORDER BY t.createdAt DESC")
    List<Transaction> findCurrentMonthTransactions(@Param("userId") Long userId);
}