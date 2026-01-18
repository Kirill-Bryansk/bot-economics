package ru.boteconomics.bot.core.buttons;

public final class ChildButton {
    private ChildButton() {
        throw new AssertionError("Нельзя создать экземпляр ChildButton");
    }

    // Дети
    public static final String ARTEMIY = "👦 Артемий";
    public static final String ARINA = "👧 Арина";
    public static final String EKATERINA = "👧 Екатерина";

    // Методы
    public static boolean isChildButton(String text) {
        return text.equals(ARTEMIY) ||
               text.equals(ARINA) ||
               text.equals(EKATERINA);
    }

    public static String getChildName(String buttonText) {
        return buttonText.replace("👦 ", "")
                .replace("👧 ", "");
    }
}