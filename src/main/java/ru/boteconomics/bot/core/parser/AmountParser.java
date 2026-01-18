package ru.boteconomics.bot.core.parser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.error.exception.AmountParseException;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Component
public class AmountParser {

    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    /**
     * Базовый парсинг: проверка формата, > 0, округление.
     */
    public BigDecimal parse(String input) throws AmountParseException {
        log.debug("Парсинг суммы: '{}'", input);

        if (input == null || input.trim().isEmpty()) {
            throw new AmountParseException("Введите сумму");
        }

        String normalized = normalizeInput(input);

        try {
            BigDecimal amount = new BigDecimal(normalized);

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new AmountParseException("Сумма должна быть больше нуля");
            }

            return amount.setScale(SCALE, ROUNDING_MODE);

        } catch (NumberFormatException e) {
            throw new AmountParseException(
                    "Пожалуйста, введите число (например: 1500 или 99.50)",
                    e
            );
        }
    }

    /**
     * Расширенный парсинг с проверкой min/max.
     * Основной метод для использования в AmountValidator.
     */
    public BigDecimal parseWithValidation(String input,
                                          BigDecimal minAmount,
                                          BigDecimal maxAmount) throws AmountParseException {
        log.debug("Парсинг суммы с проверкой диапазона: '{}'", input);

        // 1. Базовый парсинг
        BigDecimal amount = parse(input);

        // 2. Проверка максимальной суммы
        if (amount.compareTo(maxAmount) > 0) {
            throw new AmountParseException(
                    String.format("Максимальная сумма: %s ₽", maxAmount)
            );
        }

        // 3. Проверка минимальной суммы
        if (amount.compareTo(minAmount) < 0) {
            throw new AmountParseException(
                    String.format("Минимальная сумма: %s ₽", minAmount)
            );
        }

        return amount;
    }

    private String normalizeInput(String input) {
        return input.trim()
                .replace(",", ".")
                .replace(" ", "");
    }
}