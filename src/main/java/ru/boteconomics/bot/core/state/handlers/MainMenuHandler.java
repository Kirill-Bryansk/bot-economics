package ru.boteconomics.bot.core.state.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.core.state.handlers.base.BaseStateHandler;
import ru.boteconomics.bot.core.state.handlers.processors.MenuProcessor;

/**
 * Обработчик главного меню.
 * Наследует напрямую от BaseStateHandler для устранения избыточных уровней абстракции.
 * Использует MenuProcessor для обработки бизнес-логики.
 */
@Slf4j
@Component
public class MainMenuHandler extends BaseStateHandler {

    private final MenuProcessor menuProcessor;

    public MainMenuHandler(MenuProcessor menuProcessor) {
        this.menuProcessor = menuProcessor;
        log.info("Создан MainMenuHandler с прямой зависимостью от BaseStateHandler");
    }

    @Override
    public String getStateId() {
        return "MAIN_MENU";
    }

    @Override
    protected HandlerResponse processValidInput(String input, UserSession session) {
        return menuProcessor.process(input, session, getStateId());
    }
}