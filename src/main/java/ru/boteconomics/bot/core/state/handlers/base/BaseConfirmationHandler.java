/*
package ru.boteconomics.bot.core.state.handlers.base;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.core.state.handlers.processors.ConfirmationProcessor;

*/
/**
 * Базовый класс для обработчика подтверждения.
 *//*

@Slf4j
@RequiredArgsConstructor
public abstract class BaseConfirmationHandler extends BaseStateHandler {

    protected final ConfirmationProcessor confirmationProcessor;

    @Override
    protected HandlerResponse processValidInput(String input, UserSession session) {
        log.debug("BaseConfirmationHandler: обработка ввода '{}' для состояния {}",
                input, getStateId());

        // Делегируем обработку процессору
        return confirmationProcessor.process(input, session, getStateId());
    }
}*/
