package ru.boteconomics.bot.data.mapper;

import org.springframework.stereotype.Component;
import ru.boteconomics.bot.data.entity.*;
import ru.boteconomics.bot.core.expense.dto.ExpenseDTO;
import ru.boteconomics.bot.core.buttons.*;

@Component
public class TransactionMapper {

    public TransactionEntity toTransactionEntity(ExpenseDTO expenseDto) {
        TransactionEntity entity = new TransactionEntity();
        entity.setTelegramUserId(expenseDto.getUserId());
        entity.setAmount(expenseDto.getAmount());
        return entity;
    }

    public TransactionEntryEntity toTransactionEntryEntity(ExpenseDTO expenseDto, TransactionEntity transaction, CategoryEntity category) {
        TransactionEntryEntity entry = new TransactionEntryEntity();
        entry.setTransactionId(transaction.getId());
        entry.setCategoryId(category.getId());

        // Устанавливаем подкатегорию в зависимости от основной категории
        if (CategoryButton.CHILDREN.equals(expenseDto.getCategory())) {
            if (expenseDto.getChildCategory() != null) {
                // Подкатегория для детей
                // В реальной реализации нужно будет получить ID подкатегории
            }
        } else if (CategoryButton.HOUSING.equals(expenseDto.getCategory())) {
            if (expenseDto.getHousingCategory() != null) {
                // В реальной реализации нужно будет установить ID подкатегории жилья
            }
        } else if (CategoryButton.TRANSPORT.equals(expenseDto.getCategory())) {
            if (expenseDto.getTransportCategory() != null) {
                // В реальной реализации нужно будет установить ID подкатегории транспорта
            }
        } else if (CategoryButton.FOOD.equals(expenseDto.getCategory())) {
            if (expenseDto.getProductsCategory() != null) {
                // В реальной реализации нужно будет установить ID подкатегории продуктов
            }
        } else if (CategoryButton.HEALTH.equals(expenseDto.getCategory())) {
            if (expenseDto.getHealthCategory() != null) {
                // В реальной реализации нужно будет установить ID подкатегории здоровья
            }
        } else if (CategoryButton.OTHER.equals(expenseDto.getCategory())) {
            if (expenseDto.getMiscellaneousCategory() != null) {
                // В реальной реализации нужно будет установить ID подкатегории "Разное"
            }
        } else if (CategoryButton.PERSONAL.equals(expenseDto.getCategory())) {
            // Для категории "Личное" нет подкатегорий
        }

        // Устанавливаем ребенка, если есть
        if (expenseDto.getChildName() != null) {
            // В реальной реализации нужно будет установить ID ребенка
        }

        return entry;
    }

    public ExpenseDTO toDto(TransactionEntity entity) {
        // Реализация в обратном направлении
        ExpenseDTO dto = new ExpenseDTO();
        dto.setUserId(entity.getTelegramUserId());
        dto.setAmount(entity.getAmount());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
}