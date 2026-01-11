package ru.boteconomics.bot.config;

import org.springframework.context.annotation.Configuration;
import ru.boteconomics.bot.domain.Category;
import ru.boteconomics.bot.domain.TransactionType;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CategoryConfig {

    private final Map<String, String> categoryIcons = new HashMap<>();

    public CategoryConfig() {
        // Можно добавить дополнительную конфигурацию категорий
        // Например, иконки для отображения в отчетах
        for (Category category : Category.values()) {
            categoryIcons.put(category.name(), category.getEmoji());
        }
    }

    public String getIconForCategory(String categoryName) {
        return categoryIcons.getOrDefault(categoryName, "📋");
    }

    public String getIconForType(TransactionType type) {
        return type == TransactionType.INCOME ? "💰" : "💸";
    }
}