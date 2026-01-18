package ru.boteconomics.bot.core.error.exception;

/**
 * Исключение при парсинге суммы.
 * Является BusinessException (ошибка ввода пользователя).
 */
public class AmountParseException extends BusinessException {

    public AmountParseException(String userMessage) {
        super(userMessage);
    }

    public AmountParseException(String userMessage, Throwable cause) {
        super(userMessage, cause);
    }
}