package ru.boteconomics.bot.core.buttons;

public final class HealthCategoryButton {
    private HealthCategoryButton() {
        throw new AssertionError("Нельзя создать экземпляр HealthcareCategoryButton");
    }

    // Подкатегории для категории Здоровье
    public static final String HOSPITAL = "🏥 Больница";
    public static final String PHARMACY = "💊 Аптека";

    //Проверка, является ли текст подкатегорией здоровья
    public static boolean isHealthCategory(String text) {
        return  text.equals(HOSPITAL) || text.equals(PHARMACY);
    }

    // Получить все подкатегории
    public static  String[] getAll() {
        return new String[]{HOSPITAL, PHARMACY};
    }
}
