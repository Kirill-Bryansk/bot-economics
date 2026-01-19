package ru.boteconomics.bot.core.state.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.error.exception.AmountParseException;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.core.state.handlers.base.BaseStateHandler;
import ru.boteconomics.bot.core.state.handlers.processors.AmountProcessor;
import ru.boteconomics.bot.core.validation.AmountValidator;

import java.math.BigDecimal;
import java.util.function.Function;

/**
 * Обработчик ввода суммы.
 * Наследует напрямую от BaseStateHandler для устранения избыточных уровней абстракции.
 * Использует AmountProcessor для обработки бизнес-логики.
 */
@Slf4j
@Component
public class AmountInputHandler extends BaseStateHandler {

    private final AmountProcessor amountProcessor;
    private final AmountValidator amountValidator;

    public AmountInputHandler(AmountProcessor amountProcessor, AmountValidator amountValidator) {
        this.amountProcessor = amountProcessor;
        this.amountValidator = amountValidator;
        log.info("Создан AmountInputHandler с прямой зависимостью от BaseStateHandler");
    }

    @Override
    public String getStateId() {
        return "AMOUNT_INPUT";
    }

    @Override
    protected HandlerResponse processValidInput(String input, UserSession session) {
        log.debug("AmountInputHandler: обработка ввода '{}'", input);

        // Создаем парсер суммы на основе AmountValidator
        Function<String, BigDecimal> amountParser = this::parseAmount;

        // Делегируем обработку процессору
        return amountProcessor.process(input, session, getStateId(), amountParser);
    }

    /**
     * Парсит сумму с использованием AmountValidator
     */
    private BigDecimal parseAmount(String input) throws AmountParseException {
        try {
            return amountValidator.validateAndParse(input);
        } catch (AmountParseException e) {
            // Пробрасываем исключение для обработки в процессоре
            throw e;
        }
    }

    @Override
    protected HandlerResponse handleBackAction(UserSession session) {
        log.debug("Действие 'Назад' в AmountInputHandler - делегируем AmountProcessor");
        return amountProcessor.handleBackAction(session);
    }

    @Override
    protected HandlerResponse handleCancelAction(UserSession session) {
        log.debug("Действие 'Отмена' в AmountInputHandler");
        session.resetAll();
        return HandlerResponse.next(
                "Операция отменена",
                "MAIN_MENU"
        );
    }
}