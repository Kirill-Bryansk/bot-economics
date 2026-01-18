package ru.boteconomics.bot.core.replykeyboard.strategy;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.boteconomics.bot.core.buttons.ActionButton;
import java.util.ArrayList;
import java.util.List;

/**
 * Стратегия ввода суммы (без кнопок быстрых сумм)
 */
public class AmountInputStrategy implements KeyboardStrategy {

    @Override
    public ReplyKeyboardMarkup buildKeyboard(Object... context) {
        List<KeyboardRow> rows = new ArrayList<>();

        // Только навигация
        KeyboardRow row = new KeyboardRow();
        row.add(ActionButton.BACK);
        row.add(ActionButton.CANCEL);
        rows.add(row);

        return createMarkup(rows);
    }

    private ReplyKeyboardMarkup createMarkup(List<KeyboardRow> rows) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(false);
        markup.setKeyboard(rows);
        return markup;
    }
}