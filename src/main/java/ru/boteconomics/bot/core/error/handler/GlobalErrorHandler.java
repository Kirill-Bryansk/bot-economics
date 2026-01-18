package ru.boteconomics.bot.core.error.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.error.exception.BotException;
import ru.boteconomics.bot.core.error.exception.BusinessException;
import ru.boteconomics.bot.core.error.exception.SystemException;
import ru.boteconomics.bot.core.response.BotResponse;
import ru.boteconomics.bot.core.session.UserSession;

@Slf4j
@Component
public class GlobalErrorHandler {

    public BotResponse handleException(Exception e, UserSession session) {
        if (e instanceof BotException) {
            return handleBotException((BotException) e, session);
        }

        // Неизвестное исключение
        log.error("Непредвиденная ошибка: {}", e.getMessage(), e);
        session.resetAll();

        return BotResponse.withoutKeyboard(
                "⚠️ Произошла непредвиденная ошибка. Попробуйте снова.",
                "MAIN_MENU"
        );
    }

    private BotResponse handleBotException(BotException e, UserSession session) {
        log.warn("BotException: {}", e.getMessage());

        if (e instanceof BusinessException) {
            // Бизнес-ошибка: сбрасываем и показываем сообщение
            session.resetAll();
            return BotResponse.withoutKeyboard("❌ " + e.getUserMessage(), "MAIN_MENU");

        } else if (e instanceof SystemException) {
            // Системная ошибка: сбрасываем, общее сообщение
            session.resetAll();
            return BotResponse.withoutKeyboard(
                    "⚠️ Временные технические проблемы. Попробуйте позже.",
                    "MAIN_MENU"
            );

        } else {
            // Другие BotException
            session.resetAll();
            return BotResponse.withoutKeyboard("❌ " + e.getUserMessage(), "MAIN_MENU");
        }
    }
}