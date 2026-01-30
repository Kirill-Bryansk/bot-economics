package ru.boteconomics.bot.core.buttons;

public final class CategoryButton {
    private CategoryButton() {
        throw new AssertionError("Нельзя создать экземпляр CategoryButton");
    }

    // Категории расходов
    public static final String HOUSING = "🏠 Жилье";
    public static final String FOOD = "🛒 Продукты";
    public static final String TRANSPORT = "🚗 Транспорт";
    public static final String HEALTH = "🏥 Здоровье";
    public static final String PERSONAL = "👚 Личное";
    public static final String OTHER = "📦 Разное";
    public static final String CHILDREN = "👶 Дети";

    // Методы для работы
    public static boolean isCategory(String text) {
        return text.equals(HOUSING) ||
               text.equals(FOOD) ||
               text.equals(TRANSPORT) ||
               text.equals(HEALTH) ||
               text.equals(PERSONAL) ||
               text.equals(OTHER) ||
               text.equals(CHILDREN);
    }
}