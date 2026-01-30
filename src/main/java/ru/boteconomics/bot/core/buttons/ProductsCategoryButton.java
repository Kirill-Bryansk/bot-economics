package ru.boteconomics.bot.core.buttons;

public final class ProductsCategoryButton {
    private ProductsCategoryButton() {
        throw new AssertionError("Нельзя создать экземпляр ProductsCategoryButton");
    }

    // Подкатегории для "🛒 Продукты"
    public static final String FOOD_FOR_PEOPLE = "🍽️ Питание";
    public static final String PETS = "🐾 Питомцы";
    public static final String HOUSEHOLD_GOODS = "🧰 Хозтовары";

    public static boolean isProductsCategory(String text) {
        return text.equals(FOOD_FOR_PEOPLE) ||
               text.equals(PETS) ||
               text.equals(HOUSEHOLD_GOODS);
    }

    public static String[] getAll() {
        return new String[] {FOOD_FOR_PEOPLE, PETS, HOUSEHOLD_GOODS};
    }
}