package ru.boteconomics.bot.core.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.error.exception.AmountParseException;
import ru.boteconomics.bot.core.parser.AmountParser;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmountValidator {

    private final AmountParser amountParser;

    private static final BigDecimal MAX_AMOUNT = new BigDecimal("1000000");
    private static final BigDecimal MIN_AMOUNT = new BigDecimal("0.01");

    /**
     * Основной метод: валидирует и парсит сумму.
     * Делегирует всю работу парсеру.
     */
    public BigDecimal validateAndParse(String input) throws AmountParseException {
        log.debug("Валидация суммы: '{}'", input);

        // Всю логику выполняет парсер
        return amountParser.parseWithValidation(input, MIN_AMOUNT, MAX_AMOUNT);
    }

    /**
     * Только валидация (для InputErrorHandler).
     */
    public String validate(String input) {
        try {
            validateAndParse(input);
            return null;
        } catch (AmountParseException e) {
            return e.getUserMessage();
        }
    }

    /**
     * Быстрая проверка.
     */
    public boolean isValid(String input) {
        return validate(input) == null;
    }

    public BigDecimal getMaxAmount() {
        return MAX_AMOUNT;
    }

    public BigDecimal getMinAmount() {
        return MIN_AMOUNT;
    }
}