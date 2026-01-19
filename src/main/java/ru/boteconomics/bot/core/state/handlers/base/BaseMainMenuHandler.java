/*
package ru.boteconomics.bot.core.state.handlers.base;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.core.state.handlers.processors.MenuProcessor;

*/
/**
 * Базовый класс для обработчика главного меню.
 *//*

@Slf4j
@RequiredArgsConstructor
public abstract class BaseMainMenuHandler extends BaseStateHandler {

    protected final MenuProcessor menuProcessor;

    @Override
    protected HandlerResponse processValidInput(String input, UserSession session) {
        log.debug("BaseMainMenuHandler: обработка ввода '{}' для состояния {}",
                input, getStateId());

        // Делегируем обработку процессору
        return menuProcessor.process(input, session, getStateId());
    }

    @Override
    protected HandlerResponse handleBackAction(UserSession session) {
        return menuProcessor.handleBackAction(session, getStateId());
    }

    @Override
    protected HandlerResponse handleCancelAction(UserSession session) {
        return menuProcessor.handleCancelAction(session, getStateId());
    }
}*/
