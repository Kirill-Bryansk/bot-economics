package ru.boteconomics.bot.core.state.handlers.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.expense.dto.ExpenseDTO;
import ru.boteconomics.bot.core.expense.service.ExpenseService;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;

/**
 * Сервис для обработки подтверждения.
 * Содержит логику сохранения расхода и обработки действий.
 *
 * Этот процессор вызывается ТОЛЬКО для обработки действий:
 * 1. ✅ Подтвердить - сохранение расхода в БД
 * 2. ⬅️ Назад - возврат к вводу суммы
 * 3. ❌ Отмена - отмена операции
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ConfirmationProcessor {

    private final ExpenseService expenseService;

    /**
     * Обработка действия '✅ Подтвердить'
     * Сохраняет расход в базу данных и сбрасывает сессию.
     */
    public HandlerResponse handleConfirmAction(UserSession session) {
        log.info("Подтверждение операции для сессии: {}", session.toDebugString());

        // Проверяем готовность данных для сохранения
        if (!session.isReadyForSaving()) {
            log.warn("Попытка подтвердить неполные данные: {}", session.toDebugString());
            return HandlerResponse.stay(
                    "❌ Не все данные заполнены. Пожалуйста, начните заново.",
                    "CONFIRMATION"
            );
        }

        try {
            // Сохраняем расход через сервис
            ExpenseDTO savedExpense = expenseService.saveExpense(session);

            // Получаем сообщение об успехе от сервиса
            String successMessage = expenseService.generateSuccessMessage(savedExpense);

            // Сбрасываем сессию после успешного сохранения
            session.resetAll();

            log.info("✅ Расход успешно сохранен. Переход: CONFIRMATION → MAIN_MENU");
            return HandlerResponse.next(successMessage, "MAIN_MENU");

        } catch (Exception e) {
            log.error("❌ Ошибка сохранения расхода: {}", e.getMessage(), e);
            return HandlerResponse.stay(
                    "❌ Ошибка сохранения. Попробуйте снова.",
                    "CONFIRMATION"
            );
        }
    }

    /**
     * Обработка действия '⬅️ Назад'
     * Возвращает пользователя к вводу суммы.
     */
    public HandlerResponse handleBackAction(UserSession session) {
        log.debug("Действие 'Назад' из состояния подтверждения");

        // Очищаем только сумму, чтобы вернуться к её вводу
        session.resetAmount();

        log.info("Возврат к вводу суммы. Переход: CONFIRMATION → AMOUNT_INPUT");
        return HandlerResponse.next(
                "Возврат к вводу суммы",
                "AMOUNT_INPUT"
        );
    }

    /**
     * Обработка действия '❌ Отмена'
     * Полностью отменяет операцию и возвращает в главное меню.
     */
    public HandlerResponse handleCancelAction(UserSession session) {
        log.info("Отмена операции для сессии: {}", session.toDebugString());

        // Полностью сбрасываем сессию
        session.resetAll();

        log.info("Операция отменена. Переход: CONFIRMATION → MAIN_MENU");
        return HandlerResponse.next(
                "Операция отменена",
                "MAIN_MENU"
        );
    }
}