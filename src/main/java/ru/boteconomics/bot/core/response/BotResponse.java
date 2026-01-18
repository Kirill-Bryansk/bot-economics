package ru.boteconomics.bot.core.response;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

/**
 * Финальный ответ, готовый к отправке пользователю.
 * Содержит все UI-элементы (клавиатуру).
 */
public class BotResponse {
    private final String message;
    private final ReplyKeyboardMarkup keyboard;
    private final String nextStateId;  // Для обновления сессии (может быть null при stay)

    // Приватный конструктор
    private BotResponse(String message, ReplyKeyboardMarkup keyboard, String nextStateId) {
        this.message = message;
        this.keyboard = keyboard;
        this.nextStateId = nextStateId;
    }

    // ========== ФАБРИЧНЫЕ МЕТОДЫ ==========

    /**
     * Создать BotResponse из HandlerResponse с добавлением клавиатуры
     */
    public static BotResponse fromHandler(HandlerResponse handlerResponse,
                                          ReplyKeyboardMarkup keyboard) {
        String nextStateId = handlerResponse.hasNextState()
                ? handlerResponse.getNextStateId()
                : null; // При stay nextStateId = null

        return new BotResponse(
                handlerResponse.getMessage(),
                keyboard,
                nextStateId
        );
    }

    /**
     * Прямое создание с клавиатурой (для особых случаев)
     */
    public static BotResponse create(String message,
                                     ReplyKeyboardMarkup keyboard,
                                     String nextStateId) {
        return new BotResponse(message, keyboard, nextStateId);
    }

    /**
     * Ответ без клавиатуры (например, при ошибках)
     */
    public static BotResponse withoutKeyboard(String message, String nextStateId) {
        return new BotResponse(message, null, nextStateId);
    }

    /**
     * Быстрое создание ответа с stay (остаться в состоянии)
     */
    public static BotResponse stayWithKeyboard(String message,
                                               ReplyKeyboardMarkup keyboard,
                                               String stateId) {
        return new BotResponse(message, keyboard, null); // nextStateId = null при stay
    }

    /**
     * Быстрое создание ответа с переходом
     */
    public static BotResponse nextWithKeyboard(String message,
                                               ReplyKeyboardMarkup keyboard,
                                               String nextStateId) {
        return new BotResponse(message, keyboard, nextStateId);
    }

    // ========== ГЕТТЕРЫ ==========

    public String getMessage() {
        return message;
    }

    public ReplyKeyboardMarkup getKeyboard() {
        return keyboard;
    }

    public String getNextStateId() {
        return nextStateId;
    }

    public boolean hasKeyboard() {
        return keyboard != null;
    }

    public boolean hasNextState() {
        return nextStateId != null;
    }

    public boolean isStay() {
        return nextStateId == null;
    }

    // ========== ДЛЯ ОТЛАДКИ ==========

    @Override
    public String toString() {
        return String.format(
                "BotResponse{message='%s', hasKeyboard=%s, nextStateId=%s}",
                message.length() > 30 ? message.substring(0, 30) + "..." : message,
                hasKeyboard(),
                nextStateId != null ? nextStateId : "STAY"
        );
    }
}