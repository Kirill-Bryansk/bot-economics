package ru.boteconomics.bot.core.buttons;

public final class MiscellaneousCategoryButton {
    private MiscellaneousCategoryButton() {
        throw new AssertionError("Нельзя создать экземпляр MiscellaneousCategoryButton");
    }

    // Подкатегории для "📦 Разное"
    public static final String CAFE = "☕ Кафе";
    public static final String GIFTS = "🎁 Подарки";
    public static final String HOBBIES = "🎨 Хобби";
    public static final String ENTERTAINMENT = "🎬 Развлечения";

    public static boolean isMiscellaneousCategory(String text) {
        return text.equals(CAFE) ||
               text.equals(GIFTS) ||
               text.equals(HOBBIES) ||
               text.equals(ENTERTAINMENT);
    }

    public static String[] getAll() {
        return new String[] {
                CAFE,
                GIFTS,
                HOBBIES,
                ENTERTAINMENT
        };
    }
}