package ru.boteconomics.bot.core.buttons;

public final class TransportCategoryButton {
    private TransportCategoryButton() {
        throw new AssertionError("Нельзя создать экземпляр TransportCategoryButton");
    }

    // Подкатегории для "🚗 Транспорт"
    public static final String TAXI = "🚕 Такси";
    public static final String PUBLIC_TRANSPORT = "🚌 Общественный транспорт";

    public static boolean isTransportCategory(String text) {
        return text.equals(TAXI) || text.equals(PUBLIC_TRANSPORT);
    }

    public static String[] getAll() {
        return new String[] {TAXI, PUBLIC_TRANSPORT};
    }
}