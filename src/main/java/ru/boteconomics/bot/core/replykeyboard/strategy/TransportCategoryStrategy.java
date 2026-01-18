package ru.boteconomics.bot.core.replykeyboard.strategy;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.boteconomics.bot.core.buttons.TransportCategoryButton;
import ru.boteconomics.bot.core.buttons.ActionButton;
import java.util.ArrayList;
import java.util.List;

/**
 * Стратегия выбора подкатегории для транспорта
 */
public class TransportCategoryStrategy implements KeyboardStrategy {

    @Override
    public ReplyKeyboardMarkup buildKeyboard(Object... context) {
        List<KeyboardRow> rows = new ArrayList<>();

        // Подкатегории транспорта
        KeyboardRow row1 = new KeyboardRow();
        row1.add(TransportCategoryButton.TAXI);
        rows.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(TransportCategoryButton.PUBLIC_TRANSPORT);
        rows.add(row2);

        // Навигация
        KeyboardRow row3 = new KeyboardRow();
        row3.add(ActionButton.BACK);
        row3.add(ActionButton.CANCEL);
        rows.add(row3);

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