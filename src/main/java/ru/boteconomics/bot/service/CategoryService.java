package ru.boteconomics.bot.service;

import org.springframework.stereotype.Service;
import ru.boteconomics.bot.domain.Category;
import ru.boteconomics.bot.domain.TransactionType;

import java.util.List;

@Service
public class CategoryService {

    public Category getCategoryByCallback(String callbackData) {
        return Category.fromCallback(callbackData);
    }

    public String getCategoryDisplayName(String callbackData) {
        Category category = getCategoryByCallback(callbackData);
        return category != null ? category.getFullName() : "Неизвестная категория";
    }

    public List<Category> getCategoriesByType(TransactionType type) {
        return Category.getByType(type);
    }
}