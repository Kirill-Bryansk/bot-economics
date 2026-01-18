package ru.boteconomics.bot.core.error.exception;

public class SystemException extends BotException {
    public SystemException(String userMessage, Throwable cause) {
        super(
                userMessage,
                "System error: " + cause.getMessage(),
                cause
        );
    }
}