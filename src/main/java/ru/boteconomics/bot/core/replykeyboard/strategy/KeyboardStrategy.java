package ru.boteconomics.bot.core.replykeyboard.strategy;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

/**
 * Интерфейс стратегии создания клавиатур
 */
public interface KeyboardStrategy {

    /**
     * Создать клавиатуру
     * @param context дополнительный контекст
     */
    ReplyKeyboardMarkup buildKeyboard(Object... context);
}