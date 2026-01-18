package ru.boteconomics.bot.core.buttons;

public final class ChildCategoryButton {
    private ChildCategoryButton() {
        throw new AssertionError("Нельзя создать экземпляр ChildCategoryButton");
    }

    // Категории расходов детей (одинаковые для всех детей)
    public static final String SCHOOL = "📚 Школа";
    public static final String SECTIONS = "⚽ Секции";
    public static final String CLOTHES = "👕 Одежда";

    // Методы для работы
    public static boolean isChildCategory(String text) {
        return text.equals(SCHOOL) ||
               text.equals(SECTIONS) ||
               text.equals(CLOTHES);
    }
}