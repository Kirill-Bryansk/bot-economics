package ru.boteconomics.bot.core.state;

import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;

/**
 * Интерфейс состояния диалога с пользователем.
 * Каждое состояние обрабатывает ввод пользователя и определяет следующий шаг.
 */
public interface State {

    /**
     * Обработать ввод пользователя в текущем состоянии.
     *
     * @param input текст, введенный пользователем (текст кнопки или ручной ввод)
     * @param session сессия пользователя с текущими данными
     * @return HandlerResponse с сообщением и следующим состоянием (без UI)
     */
    HandlerResponse handle(String input, UserSession session);

    /**
     * Получить уникальный идентификатор состояния.
     * Должен совпадать с одним из значений в Callback.java или строковым представлением.
     *
     * @return идентификатор состояния (например, "MAIN_MENU", "CATEGORY_SELECTION")
     */
    String getStateId();
}