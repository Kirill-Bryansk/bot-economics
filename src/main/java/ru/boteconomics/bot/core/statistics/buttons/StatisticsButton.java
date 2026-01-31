package ru.boteconomics.bot.core.statistics.buttons;

/**
 * Константы кнопок для меню статистики.
 * Используется в StatisticsMenuStrategy для построения клавиатуры.
 */
public final class StatisticsButton {

    private StatisticsButton() {
        throw new AssertionError("Нельзя создать экземпляр StatisticsButton");
    }

    // Тексты кнопок статистики
    public static final String CURRENT_WEEK = "📊 Текущая неделя";
    public static final String CURRENT_MONTH = "📈 Текущий месяц";

    // Проверка является ли текст кнопкой статистики
    public static boolean isStatisticsButton(String text) {
        return text.equals(CURRENT_WEEK) ||
               text.equals(CURRENT_MONTH);
    }

    // Все кнопки статистики
    public static String[] getAll() {
        return new String[] {CURRENT_WEEK, CURRENT_MONTH};
    }

    // Проверка на кнопку "Текущая неделя"
    public static boolean isCurrentWeek(String text) {
        return CURRENT_WEEK.equals(text);
    }

    // Проверка на кнопку "Текущий месяц"
    public static boolean isCurrentMonth(String text) {
        return CURRENT_MONTH.equals(text);
    }
}