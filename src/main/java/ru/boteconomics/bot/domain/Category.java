package ru.boteconomics.bot.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Category {
    // ========== ДОХОДЫ ==========
    INCOME_SALARY("Зарплата", TransactionType.INCOME, "💼"),
    INCOME_RENT("Аренда", TransactionType.INCOME, "🏠"),
    INCOME_GIFTS("Подарки", TransactionType.INCOME, "🎁"),

    // ========== РАСХОДЫ ==========
    // 🏠 Жилье
    HOUSING_UTILITIES("Коммуналка", TransactionType.EXPENSE, "🏠"),
    HOUSING_INTERNET("Интернет/ТВ/связь", TransactionType.EXPENSE, "📡"),
    HOUSING_REPAIR("Ремонт", TransactionType.EXPENSE, "🔧"),

    // 🛒 Продукты
    FOOD("Продукты", TransactionType.EXPENSE, "🛒"),

    // 🚗 Транспорт
    TRANSPORT_PUBLIC("Общественный транспорт", TransactionType.EXPENSE, "🚌"),
    TRANSPORT_TAXI("Такси", TransactionType.EXPENSE, "🚕"),

    // 💊 Здоровье
    HEALTH("Здоровье", TransactionType.EXPENSE, "💊"),

    // 👩 Личное (Анна)
    PERSONAL_CLOTHES("Одежда (Анна)", TransactionType.EXPENSE, "👚"),
    PERSONAL_COSMETICS("Косметика (Анна)", TransactionType.EXPENSE, "💄"),
    PERSONAL_HOBBY("Хобби (Анна)", TransactionType.EXPENSE, "🎨"),

    // 📦 Разное
    OTHER_RESTAURANTS("Рестораны", TransactionType.EXPENSE, "🍽️"),
    OTHER_GIFTS("Подарки", TransactionType.EXPENSE, "🎁"),
    OTHER_MISC("Прочее", TransactionType.EXPENSE, "📦"),

    // 👶 Дети (динамически генерируются)
    CHILD_ARTEMIY_SCHOOL("Артемий - Школа", TransactionType.EXPENSE, "📚"),
    CHILD_ARTEMIY_SECTIONS("Артемий - Секции", TransactionType.EXPENSE, "⚽"),
    CHILD_ARTEMIY_CLOTHES("Артемий - Одежда", TransactionType.EXPENSE, "👕"),

    CHILD_ARINA_SCHOOL("Арина - Школа", TransactionType.EXPENSE, "📚"),
    CHILD_ARINA_SECTIONS("Арина - Секции", TransactionType.EXPENSE, "⚽"),
    CHILD_ARINA_CLOTHES("Арина - Одежда", TransactionType.EXPENSE, "👕"),

    CHILD_EKATERINA_SCHOOL("Екатерина - Школа", TransactionType.EXPENSE, "📚"),
    CHILD_EKATERINA_SECTIONS("Екатерина - Секции", TransactionType.EXPENSE, "⚽"),
    CHILD_EKATERINA_CLOTHES("Екатерина - Одежда", TransactionType.EXPENSE, "👕");

    private final String displayName;
    private final TransactionType type;
    private final String emoji;

    Category(String displayName, TransactionType type, String emoji) {
        this.displayName = displayName;
        this.type = type;
        this.emoji = emoji;
    }

    public String getDisplayName() {
        return displayName;
    }

    public TransactionType getType() {
        return type;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getFullName() {
        return emoji + " " + displayName;
    }

    public String getCallbackData() {
        return "CATEGORY_" + name();
    }

    // ========== УТИЛИТНЫЕ МЕТОДЫ ==========

    public static List<Category> getByType(TransactionType type) {
        return Arrays.stream(values())
                .filter(category -> category.type == type)
                .collect(Collectors.toList());
    }

    public static Category fromCallback(String callbackData) {
        try {
            String enumName = callbackData.replace("CATEGORY_", "");
            return valueOf(enumName);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static boolean isChildCategory(Category category) {
        return category != null && category.name().startsWith("CHILD_");
    }

    public static Child extractChildFromCategory(Category category) {
        if (!isChildCategory(category)) {
            return null;
        }

        String name = category.name();
        String[] parts = name.split("_");

        if (parts.length >= 2) {
            try {
                return Child.valueOf(parts[1]);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        return null;
    }

    public static String extractChildCategoryType(Category category) {
        if (!isChildCategory(category)) {
            return null;
        }

        String name = category.name();
        String[] parts = name.split("_");

        if (parts.length >= 3) {
            return parts[2];
        }

        return null;
    }
}