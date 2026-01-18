package ru.boteconomics.bot.core.replykeyboard.strategy;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.boteconomics.bot.core.buttons.ActionButton;
import java.util.ArrayList;
import java.util.List;

/**
 * Универсальная стратегия подтверждения
 */
public class ConfirmationStrategy implements KeyboardStrategy {

    @Override
    public ReplyKeyboardMarkup buildKeyboard(Object... context) {
        List<KeyboardRow> rows = new ArrayList<>();

        // 1-й ряд: Подтвердить и Отмена
        KeyboardRow row1 = new KeyboardRow();
        row1.add(ActionButton.CONFIRM);
        row1.add(ActionButton.CANCEL);
        rows.add(row1);

        // 2-й ряд: Назад
        KeyboardRow row2 = new KeyboardRow();
        row2.add(ActionButton.BACK);
        rows.add(row2);

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