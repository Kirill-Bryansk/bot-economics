package ru.boteconomics.bot.core.state.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.expense.dto.ExpenseDTO;
import ru.boteconomics.bot.core.expense.service.ExpenseService;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;

@Slf4j
@Component
public class ConfirmationHandler extends BaseStateHandler {

    private final ExpenseService expenseService;

    public ConfirmationHandler(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @Override
    public String getStateId() {
        return "CONFIRMATION";
    }

    @Override
    protected HandlerResponse processValidInput(String input, UserSession session) {
        log.debug("Обработка ввода в состоянии подтверждения: '{}'", input);

        HandlerResponse actionResponse = handleActionIfNeeded(input, session);
        if (actionResponse != null) {
            return actionResponse;
        }

        log.error("Непредвиденный ввод в ConfirmationHandler: '{}'", input);
        return HandlerResponse.stay(
                "Пожалуйста, используйте кнопки 'Подтвердить' или 'Отмена'",
                getStateId()
        );
    }

    @Override
    protected HandlerResponse handleConfirmAction(UserSession session) {
        log.info("Подтверждение операции для сессии: {}", session.toDebugString());

        // Проверяем готовность
        if (!session.isReadyForSaving()) {
            log.warn("Попытка подтвердить неполные данные: {}", session.toDebugString());
            return HandlerResponse.stay(
                    "❌ Не все данные заполнены. Пожалуйста, начните заново.",
                    getStateId()
            );
        }

        try {
            // Сохраняем через сервис
            ExpenseDTO savedExpense = expenseService.saveExpense(session);

            // Получаем сообщение об успехе от сервиса
            String successMessage = expenseService.generateSuccessMessage(savedExpense);

            // Сбрасываем сессию
            session.resetAll();

            logTransition(getStateId(), "MAIN_MENU");
            return HandlerResponse.next(successMessage, "MAIN_MENU");

        } catch (Exception e) {
            log.error("Ошибка сохранения расхода: {}", e.getMessage(), e);
            return HandlerResponse.stay(
                    "❌ Ошибка сохранения. Попробуйте снова.",
                    getStateId()
            );
        }
    }

    @Override
    protected HandlerResponse handleBackAction(UserSession session) {
        log.debug("Действие 'Назад' из подтверждения");
        session.resetAmount();
        logTransition(getStateId(), "AMOUNT_INPUT");
        return HandlerResponse.next(
                "Возврат к вводу суммы",
                "AMOUNT_INPUT"
        );
    }

    @Override
    protected HandlerResponse handleCancelAction(UserSession session) {
        log.info("Отмена операции для сессии: {}", session.toDebugString());
        session.resetAll();
        logTransition(getStateId(), "MAIN_MENU");
        return HandlerResponse.next(
                "Операция отменена",
                "MAIN_MENU"
        );
    }
}