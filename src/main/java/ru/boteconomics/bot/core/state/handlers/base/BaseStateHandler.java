package ru.boteconomics.bot.core.state.handlers.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.boteconomics.bot.core.error.handler.InputErrorHandler;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.core.state.State;

/**
 * Базовый абстрактный класс для всех обработчиков состояний.
 * Содержит общую логику проверки ввода и обработки действий.
 *
 * Порядок обработки:
 * 1. Проверка действий (Назад/Отмена/Подтвердить) - если действие, обрабатываем сразу
 * 2. Валидация обычного ввода через InputErrorHandler
 * 3. Обработка валидного ввода в processValidInput()
 */
@Slf4j
public abstract class BaseStateHandler implements State {

    @Autowired
    protected InputErrorHandler inputErrorHandler;

    /**
     * Основной метод обработки ввода.
     * Порядок обработки:
     * 1. Проверяет, является ли ввод действием (Назад/Отмена/Подтвердить)
     * 2. Если нет - проверяет валидность ввода через InputErrorHandler
     * 3. Если ввод валиден - вызывает processValidInput
     */
    @Override
    public HandlerResponse handle(String input, UserSession session) {
        log.debug("{}: обработка ввода '{}'", getClass().getSimpleName(), input);

        // 1. Сначала проверяем действия (Назад/Отмена/Подтвердить)
        HandlerResponse actionResponse = handleActionIfNeeded(input, session);
        if (actionResponse != null) {
            log.debug("Обработка действия в состоянии {}: {}", getStateId(), input);
            return actionResponse;
        }

        // 2. Если не действие - проверяем валидность обычного ввода
        HandlerResponse errorResponse = inputErrorHandler.validateAndCreateError(input, getStateId());
        if (errorResponse != null) {
            log.debug("Ошибка ввода в состоянии {}: {}", getStateId(), errorResponse.getMessage());
            return errorResponse;
        }

        // 3. Ввод валиден и не является действием - обрабатываем бизнес-логику
        try {
            return processValidInput(input, session);
        } catch (Exception e) {
            log.error("Ошибка обработки ввода в состоянии {}: {}", getStateId(), e.getMessage(), e);
            return HandlerResponse.stay(
                    "❌ Произошла ошибка при обработке. Попробуйте снова.",
                    getStateId()
            );
        }
    }

    /**
     * Абстрактный метод для обработки валидного ввода.
     * Реализуется в конкретных обработчиках.
     * СЮДА попадают только НЕ-действия (обычный ввод).
     */
    protected abstract HandlerResponse processValidInput(String input, UserSession session);

    /**
     * Обработка действия "Назад".
     * Базовая реализация - возврат в MAIN_MENU.
     * Можно переопределить в наследниках.
     */
    protected HandlerResponse handleBackAction(UserSession session) {
        log.debug("Обработка действия 'Назад' в состоянии {}", getStateId());
        return HandlerResponse.next("Возврат в главное меню", "MAIN_MENU");
    }

    /**
     * Обработка действия "Отмена".
     * Базовая реализация - сброс сессии и возврат в MAIN_MENU.
     */
    protected HandlerResponse handleCancelAction(UserSession session) {
        log.debug("Обработка действия 'Отмена' в состоянии {}", getStateId());
        session.resetAll();
        return HandlerResponse.next("Операция отменена", "MAIN_MENU");
    }

    /**
     * Обработка действия "Подтвердить".
     * Базовая реализация - заглушка, должна быть переопределена в ConfirmationHandler.
     */
    protected HandlerResponse handleConfirmAction(UserSession session) {
        log.warn("Действие 'Подтвердить' не обработано в состоянии {}", getStateId());
        return HandlerResponse.stay(
                "Действие 'Подтвердить' не поддерживается в этом состоянии.",
                getStateId()
        );
    }

    /**
     * Обработка действий (Назад/Отмена/Подтвердить).
     * Возвращает HandlerResponse если ввод является действием, иначе null.
     */
    protected HandlerResponse handleActionIfNeeded(String input, UserSession session) {
        if (ru.boteconomics.bot.core.buttons.ActionButton.BACK.equals(input)) {
            return handleBackAction(session);
        }
        if (ru.boteconomics.bot.core.buttons.ActionButton.CANCEL.equals(input)) {
            return handleCancelAction(session);
        }
        if (ru.boteconomics.bot.core.buttons.ActionButton.CONFIRM.equals(input)) {
            return handleConfirmAction(session);
        }
        return null; // Не действие
    }

    /**
     * Вспомогательный метод для логирования перехода
     */
    protected void logTransition(String fromState, String toState) {
        log.info("Переход: {} → {}", fromState, toState);
    }

    /**
     * Вспомогательный метод для логирования выбора
     */
    protected void logSelection(String what, String value) {
        log.info("Выбран {}: {}", what, value);
    }
}