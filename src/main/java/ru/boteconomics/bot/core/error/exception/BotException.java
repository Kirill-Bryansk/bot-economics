package ru.boteconomics.bot.core.error.exception;

public abstract class BotException extends RuntimeException {
    private final String userMessage;

    public BotException(String userMessage, String logMessage) {
        super(logMessage);
        this.userMessage = userMessage;
    }

    public BotException(String userMessage, String logMessage, Throwable cause) {
        super(logMessage, cause);
        this.userMessage = userMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }
}