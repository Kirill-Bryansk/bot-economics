package ru.boteconomics.bot.core.replykeyboard.strategy;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.boteconomics.bot.core.buttons.MiscellaneousCategoryButton;
import ru.boteconomics.bot.core.buttons.ActionButton;
import java.util.ArrayList;
import java.util.List;

/**
 * Стратегия выбора подкатегории для "Разное"
 */
public class MiscellaneousCategoryStrategy implements KeyboardStrategy {

    @Override
    public ReplyKeyboardMarkup buildKeyboard(Object... context) {
        List<KeyboardRow> rows = new ArrayList<>();

        // Подкатегории "Разное"
        KeyboardRow row1 = new KeyboardRow();
        row1.add(MiscellaneousCategoryButton.CAFE);
        rows.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(MiscellaneousCategoryButton.GIFTS);
        rows.add(row2);

        KeyboardRow row3 = new KeyboardRow();
        row3.add(MiscellaneousCategoryButton.HOBBIES);
        rows.add(row3);

        KeyboardRow row4 = new KeyboardRow();
        row4.add(MiscellaneousCategoryButton.ENTERTAINMENT);
        rows.add(row4);

        // Навигация
        KeyboardRow row5 = new KeyboardRow();
        row5.add(ActionButton.BACK);
        row5.add(ActionButton.CANCEL);
        rows.add(row5);

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