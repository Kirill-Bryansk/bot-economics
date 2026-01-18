package ru.boteconomics.bot.core.buttons;

public final class ActionButton {
    private ActionButton() {
        throw new AssertionError("Нельзя создать экземпляр ActionButton");
    }

    // Действия
    public static final String BACK = "⬅️ Назад";
    public static final String CANCEL = "❌ Отмена";
    public static final String CONFIRM = "✅ Подтвердить";

    // Универсальный метод для инлайн-клавиатур
    public static boolean isAction(String text) {
        return text.equals(BACK) ||
               text.equals(CANCEL) ||
               text.equals(CONFIRM);
    }
}