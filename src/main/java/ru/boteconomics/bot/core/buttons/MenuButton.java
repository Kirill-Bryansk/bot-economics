package ru.boteconomics.bot.core.buttons;

public final class MenuButton {
    private MenuButton() {
        throw new AssertionError("Нельзя создать экземпляр MenuButton");
    }

    // Основные команды
    public static final String ADD_EXPENSE = "💸 Добавить расход";
    public static final String HISTORY = "📋 История операций";
    public static final String STATISTICS = "📊 Статистика";
    public static final String MAIN_MENU = "🏠 Главное меню";

    // Вспомогательные методы
    public static boolean isMenuButton(String text) {
        return text.equals(ADD_EXPENSE) ||
               text.equals(HISTORY) ||
               text.equals(STATISTICS) ||
               text.equals(MAIN_MENU);
    }

    public static String[] getAll() {
        return new String[] {ADD_EXPENSE, HISTORY, STATISTICS, MAIN_MENU};
    }
}