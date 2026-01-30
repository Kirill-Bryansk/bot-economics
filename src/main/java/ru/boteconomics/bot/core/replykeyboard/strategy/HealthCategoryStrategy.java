package ru.boteconomics.bot.core.replykeyboard.strategy;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.boteconomics.bot.core.buttons.ActionButton;
import ru.boteconomics.bot.core.buttons.HealthCategoryButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Стратегия выбора подкатегории для "Здоровье"
 */
public class HealthCategoryStrategy implements KeyboardStrategy {

    @Override
    public ReplyKeyboardMarkup buildKeyboard(Object... context) {
        List<KeyboardRow> rows = new ArrayList<>();

        // Подкатегории "Здоровье" - двухрядная клавиатура
        KeyboardRow row1 = new KeyboardRow();
        row1.add(HealthCategoryButton.HOSPITAL);
        row1.add(HealthCategoryButton.PHARMACY);
        rows.add(row1);

        // Навигация
        KeyboardRow row2 = new KeyboardRow();
        row2.add(ActionButton.BACK);
        row2.add(ActionButton.CANCEL);
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
