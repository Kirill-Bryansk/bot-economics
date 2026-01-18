package ru.boteconomics.bot.core.buttons;

public final class HousingCategoryButton {
    private HousingCategoryButton() {
        throw new AssertionError("Нельзя создать экземпляр HousingCategoryButton");
    }

    // Подкатегории для "🏠 Жилье"
    public static final String UTILITIES = "💡 Коммуналка";
    public static final String REPAIR = "🛠️ Ремонт";
    public static final String FURNITURE = "🛋️ Мебель";
    public static final String APPLIANCES = "🏗️ Техника";

    public static boolean isHousingCategory(String text) {
        return text.equals(UTILITIES) ||
               text.equals(REPAIR) ||
               text.equals(FURNITURE) ||
               text.equals(APPLIANCES);
    }

    public static String[] getAll() {
        return new String[] {UTILITIES, REPAIR, FURNITURE, APPLIANCES};
    }
}