package ru.boteconomics.bot.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.boteconomics.bot.data.entity.TransactionEntryEntity;

@Repository
public interface TransactionEntryRepository extends JpaRepository<TransactionEntryEntity, TransactionEntryEntity.TransactionEntryId> {
    // Методы для работы с записями транзакций
}