package ru.boteconomics.bot.core.response;

/**
 * Ответ от Handler'а State.
 * Содержит только бизнес-информацию без UI-элементов.
 */
public class HandlerResponse {
    private final String message;      // Сообщение пользователю
    private final String nextStateId;  // ID следующего состояния
    private final String stayInStateId; // ID состояния, если остаёмся в нём (для stay)

    // Приватный конструктор для stay
    private HandlerResponse(String message, String stayInStateId, boolean isStay) {
        this.message = message;
        this.nextStateId = null;
        this.stayInStateId = stayInStateId;
    }

    // Приватный конструктор для next
    private HandlerResponse(String message, String nextStateId) {
        this.message = message;
        this.nextStateId = nextStateId;
        this.stayInStateId = null;
    }

    // ========== ФАБРИЧНЫЕ МЕТОДЫ ==========

    /**
     * Остаться в текущем состоянии
     * @param message сообщение пользователю
     * @param currentStateId ID текущего состояния (в котором остаёмся)
     */
    public static HandlerResponse stay(String message, String currentStateId) {
        if (currentStateId == null || currentStateId.trim().isEmpty()) {
            throw new IllegalArgumentException("currentStateId не может быть пустым для stay");
        }
        return new HandlerResponse(message, currentStateId, true);
    }

    /**
     * Перейти в следующее состояние
     */
    public static HandlerResponse next(String message, String nextStateId) {
        if (nextStateId == null || nextStateId.trim().isEmpty()) {
            throw new IllegalArgumentException("nextStateId не может быть пустым для next");
        }
        return new HandlerResponse(message, nextStateId);
    }

    // ========== ГЕТТЕРЫ ==========

    public String getMessage() {
        return message;
    }

    public String getNextStateId() {
        return nextStateId;
    }

    /**
     * Получить состояние для которого нужна клавиатура
     * Если nextStateId != null - возвращает его
     * Иначе возвращает stayInStateId (остаёмся в текущем)
     */
    public String getStateForKeyboard() {
        return nextStateId != null ? nextStateId : stayInStateId;
    }

    public boolean hasNextState() {
        return nextStateId != null;
    }

    public boolean isStay() {
        return nextStateId == null && stayInStateId != null;
    }

    // ========== ДЛЯ ОТЛАДКИ ==========

    @Override
    public String toString() {
        if (hasNextState()) {
            return String.format(
                    "HandlerResponse{message='%s', NEXT_STATE=%s}",
                    message.length() > 30 ? message.substring(0, 30) + "..." : message,
                    nextStateId
            );
        } else {
            return String.format(
                    "HandlerResponse{message='%s', STAY_IN=%s}",
                    message.length() > 30 ? message.substring(0, 30) + "..." : message,
                    stayInStateId
            );
        }
    }
}