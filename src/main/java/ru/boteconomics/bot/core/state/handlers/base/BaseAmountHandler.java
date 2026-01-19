package ru.boteconomics.bot.core.state.handlers.base;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.core.state.handlers.processors.AmountProcessor;

import java.math.BigDecimal;
import java.util.function.Function;

/**
 * Базовый класс для обработчика ввода суммы.
 */
@Slf4j
@RequiredArgsConstructor
public abstract class BaseAmountHandler extends BaseStateHandler {

    protected final AmountProcessor amountProcessor;

    /**
     * Получить функцию для валидации и парсинга суммы
     */
    protected abstract Function<String, BigDecimal> getAmountParser();

    @Override
    protected HandlerResponse processValidInput(String input, UserSession session) {
        log.debug("BaseAmountHandler: обработка ввода '{}' для состояния {}",
                input, getStateId());

        // 1. Проверяем действия (Назад/Отмена)
        HandlerResponse actionResponse = handleActionIfNeeded(input, session);
        if (actionResponse != null) {
            return actionResponse;
        }

        // 2. Делегируем обработку процессору
        return amountProcessor.process(
                input,
                session,
                getStateId(),
                getAmountParser()
        );
    }

    @Override
    protected HandlerResponse handleBackAction(UserSession session) {
        return amountProcessor.handleBackAction(session);
    }
}