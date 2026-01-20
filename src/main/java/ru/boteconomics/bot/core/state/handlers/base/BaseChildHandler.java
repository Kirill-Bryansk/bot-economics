/*
package ru.boteconomics.bot.core.state.handlers.base;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.core.state.handlers.processors.ChildProcessor;

import java.util.function.Consumer;
import java.util.function.Predicate;

*/
/**
 * Базовый класс для всех обработчиков, связанных с детьми.
 * Содержит общую логику и делегирует специфичные части наследникам.
 *//*

@Slf4j
@RequiredArgsConstructor
public abstract class BaseChildHandler extends BaseStateHandler {

    protected final ChildProcessor childProcessor;

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

    */
/**
     * Получить следующее состояние
     *//*

    protected abstract String getNextState();

    */
/**
     * Получить сообщение о выборе
     *//*

    protected abstract String getSelectionMessage(String input);

    @Override
    protected HandlerResponse processValidInput(String input, UserSession session) {
        log.debug("BaseChildHandler: обработка ввода '{}' для состояния {}",
                input, getStateId());

        // 1. Проверяем действия (Назад/Отмена)
        HandlerResponse actionResponse = handleActionIfNeeded(input, session);
        if (actionResponse != null) {
            return actionResponse;
        }

        // 2. Делегируем обработку процессору
        return childProcessor.process(
                input,
                session,
                getStateId(),
                getValidator(),
                getSaver(input),
                getDescription(),
                getNextState(),
                getSelectionMessage(input)
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
