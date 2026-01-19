/*
package ru.boteconomics.bot.core.state.handlers.base;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.core.state.handlers.processors.CategoryProcessor;

import java.util.function.Consumer;
import java.util.function.Predicate;

*/
/**
 * Базовый класс для обработчика категорий.
 *//*

@Slf4j
@RequiredArgsConstructor
public abstract class BaseCategoryHandler extends BaseStateHandler {

    protected final CategoryProcessor categoryProcessor;

    */
/**
     * Получить валидатор для проверки ввода
     *//*

    protected abstract Predicate<String> getValidator();

    */
/**
     * Получить функцию для сохранения данных в сессию
     *//*

    protected abstract Consumer<UserSession> getSaver(String input);

    */
/**
     * Получить описание для логирования
     *//*

    protected abstract String getDescription();

    @Override
    protected HandlerResponse processValidInput(String input, UserSession session) {
        log.debug("BaseCategoryHandler: обработка ввода '{}' для состояния {}",
                input, getStateId());

        // 1. Проверяем действия (Назад/Отмена)
        HandlerResponse actionResponse = handleActionIfNeeded(input, session);
        if (actionResponse != null) {
            return actionResponse;
        }

        // 2. Делегируем обработку процессору
        return categoryProcessor.process(
                input,
                session,
                getStateId(),
                getValidator(),
                getSaver(input),
                getDescription()
        );
    }

    */
/**
     * Вспомогательный метод для логирования
     *//*

    protected void logSelection(String value) {
        log.info("[{}] Выбрана {}: {}", getStateId(), getDescription(), value);
    }
}*/
