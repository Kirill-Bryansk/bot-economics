package ru.boteconomics.bot.core.statistics.state;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.buttons.ActionButton;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.core.state.handlers.base.BaseStateHandler;
import ru.boteconomics.bot.core.statistics.buttons.StatisticsButton;
import ru.boteconomics.bot.core.statistics.processor.StatisticsProcessor;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatisticsMenuHandler extends BaseStateHandler {

    private final StatisticsProcessor statisticsProcessor;

    @Override
    public String getStateId() {
        return "STATISTICS_MENU";
    }

    /**
     * Переопределяем handle(), чтобы пропустить проверку InputErrorHandler.
     * В меню статистики все кнопки известны заранее.
     */
    @Override
    public HandlerResponse handle(String input, UserSession session) {
        log.info("StatisticsMenuHandler.handle() для ввода: '{}'", input);

        // 1. Сначала проверяем действия (Назад/Отмена/Подтвердить)
        HandlerResponse actionResponse = handleActionIfNeeded(input, session);
        if (actionResponse != null) {
            log.debug("Обработка действия в StatisticsMenuHandler: {}", input);
            return actionResponse;
        }

        // 2. Проверяем кнопки статистики
        if (StatisticsButton.isStatisticsButton(input)) {
            return processValidInput(input, session);
        }

        // 3. Неизвестный ввод
        log.warn("Неизвестный ввод в StatisticsMenuHandler: '{}'", input);
        return HandlerResponse.stay(
                "Пожалуйста, используйте кнопки меню статистики",
                getStateId()
        );
    }

    @Override
    protected HandlerResponse processValidInput(String input, UserSession session) {
        log.info("Обработка ввода в StatisticsMenuHandler: '{}' для userId={}",
                input, session.getUserId());

        Long userId = session.getUserId();
        if (userId == null) {
            log.error("userId не найден в сессии: {}", session);
            return HandlerResponse.stay(
                    "Ошибка сессии. Попробуйте начать заново.",
                    getStateId()
            );
        }

        HandlerResponse response = statisticsProcessor.process(input, userId, getStateId());
        log.debug("StatisticsProcessor вернул: {}", response);

        return response;
    }

    @Override
    protected HandlerResponse handleBackAction(UserSession session) {
        log.info("Действие 'Назад' в StatisticsMenuHandler");
        return HandlerResponse.next("Возврат в главное меню", "MAIN_MENU");
    }

    @Override
    protected HandlerResponse handleCancelAction(UserSession session) {
        log.info("Действие 'Отмена' в StatisticsMenuHandler");
        session.resetAll();
        return HandlerResponse.next("Операция отменена", "MAIN_MENU");
    }

    @Override
    protected HandlerResponse handleConfirmAction(UserSession session) {
        log.warn("Действие 'Подтвердить' вызвано в StatisticsMenuHandler");
        return HandlerResponse.stay(
                "В меню статистики нет действий для подтверждения",
                getStateId()
        );
    }
}