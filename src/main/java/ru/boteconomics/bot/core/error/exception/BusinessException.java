package ru.boteconomics.bot.core.error.exception;

public class BusinessException extends BotException {
    public BusinessException(String userMessage) {
        super(userMessage, "Business error: " + userMessage);
    }

    public BusinessException(String userMessage, Throwable cause) {
        super(userMessage, "Business error: " + userMessage, cause);
    }
}