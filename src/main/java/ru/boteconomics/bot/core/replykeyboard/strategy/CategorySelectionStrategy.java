package ru.boteconomics.bot.core.replykeyboard.strategy;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.boteconomics.bot.core.buttons.CategoryButton;
import ru.boteconomics.bot.core.buttons.ActionButton;
import java.util.ArrayList;
import java.util.List;

/**
 * Стратегия выбора категории
 */
public class CategorySelectionStrategy implements KeyboardStrategy {

    @Override
    public ReplyKeyboardMarkup buildKeyboard(Object... context) {
        List<KeyboardRow> rows = new ArrayList<>();

        // 1-й ряд: Жилье и Продукты
        KeyboardRow row1 = new KeyboardRow();
        row1.add(CategoryButton.HOUSING);
        row1.add(CategoryButton.FOOD);
        rows.add(row1);

        // 2-й ряд: Транспорт и Здоровье
        KeyboardRow row2 = new KeyboardRow();
        row2.add(CategoryButton.TRANSPORT);
        row2.add(CategoryButton.HEALTH);
        rows.add(row2);

        // 3-й ряд: Личное и Разное
        KeyboardRow row3 = new KeyboardRow();
        row3.add(CategoryButton.PERSONAL);
        row3.add(CategoryButton.OTHER);
        rows.add(row3);

        // 4-й ряд: Дети и Навигация
        KeyboardRow row4 = new KeyboardRow();
        row4.add(CategoryButton.CHILDREN);
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