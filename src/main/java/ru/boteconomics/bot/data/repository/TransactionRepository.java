package ru.boteconomics.bot.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.boteconomics.bot.data.entity.TransactionEntity;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findByTelegramUserIdOrderByCreatedAtDesc(Long telegramUserId);
}