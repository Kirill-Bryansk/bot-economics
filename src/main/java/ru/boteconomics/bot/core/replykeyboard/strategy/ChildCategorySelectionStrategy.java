package ru.boteconomics.bot.core.replykeyboard.strategy;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.boteconomics.bot.core.buttons.ChildCategoryButton;
import ru.boteconomics.bot.core.buttons.ActionButton;
import java.util.ArrayList;
import java.util.List;

/**
 * Стратегия выбора категории для ребенка
 */
public class ChildCategorySelectionStrategy implements KeyboardStrategy {

    @Override
    public ReplyKeyboardMarkup buildKeyboard(Object... context) {
        List<KeyboardRow> rows = new ArrayList<>();

        // Категории ребенка (используем новые константы)
        KeyboardRow row1 = new KeyboardRow();
        row1.add(ChildCategoryButton.SCHOOL);
        rows.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(ChildCategoryButton.SECTIONS);
        rows.add(row2);

        KeyboardRow row3 = new KeyboardRow();
        row3.add(ChildCategoryButton.CLOTHES);
        rows.add(row3);

        // Навигация
        KeyboardRow row4 = new KeyboardRow();
        row4.add(ActionButton.BACK);
        row4.add(ActionButton.CANCEL);
        rows.add(row4);

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