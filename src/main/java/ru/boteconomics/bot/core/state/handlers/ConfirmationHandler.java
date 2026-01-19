package ru.boteconomics.bot.core.state.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.core.state.handlers.base.BaseStateHandler;
import ru.boteconomics.bot.core.state.handlers.processors.ConfirmationProcessor;

/**
 * Обработчик состояния подтверждения операции.
 * Состояние: CONFIRMATION
 *
 * Обрабатывает:
 * 1. Действие "✅ Подтвердить" - сохранение расхода в БД
 * 2. Действие "⬅️ Назад" - возврат к вводу суммы
 * 3. Действие "❌ Отмена" - отмена операции
 * 4. Любой другой ввод - ошибка (ожидаются только действия)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ConfirmationHandler extends BaseStateHandler {

    private final ConfirmationProcessor confirmationProcessor;

    @Override
    public String getStateId() {
        return "CONFIRMATION";
    }

    /**
     * Обработка валидного ввода в состоянии подтверждения.
     * Сюда попадают только НЕ-действия (обычный ввод).
     * В состоянии подтверждения все вводы кроме действий - ошибка.
     */
    @Override
    protected HandlerResponse processValidInput(String input, UserSession session) {
        log.debug("Обработка обычного ввода в состоянии подтверждения: '{}'", input);

        // В состоянии подтверждения не должно быть обычного ввода
        // Все должно быть действиями (Назад/Отмена/Подтвердить)
        log.warn("Непредвиденный ввод в состоянии подтверждения: '{}'", input);
        return HandlerResponse.stay(
                "Пожалуйста, используйте кнопки '✅ Подтвердить' или '❌ Отмена'",
                getStateId()
        );
    }

    /**
     * Обработка действия "✅ Подтвердить".
     * Делегирует сохранение расхода ConfirmationProcessor.
     */
    @Override
    protected HandlerResponse handleConfirmAction(UserSession session) {
        log.debug("Обработка действия 'Подтвердить' в состоянии подтверждения");
        return confirmationProcessor.handleConfirmAction(session);
    }

    /**
     * Обработка действия "⬅️ Назад".
     * Возврат к вводу суммы.
     */
    @Override
    protected HandlerResponse handleBackAction(UserSession session) {
        log.debug("Обработка действия 'Назад' в состоянии подтверждения");
        return confirmationProcessor.handleBackAction(session);
    }

    /**
     * Обработка действия "❌ Отмена".
     * Отмена операции и возврат в главное меню.
     */
    @Override
    protected HandlerResponse handleCancelAction(UserSession session) {
        log.debug("Обработка действия 'Отмена' в состоянии подтверждения");
        return confirmationProcessor.handleCancelAction(session);
    }
}