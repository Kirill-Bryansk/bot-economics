package ru.boteconomics.bot.core.replykeyboard.strategy;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.boteconomics.bot.core.buttons.MenuButton;
import java.util.ArrayList;
import java.util.List;

/**
 * Стратегия главного меню
 */
public class MainMenuStrategy implements KeyboardStrategy {

    @Override
    public ReplyKeyboardMarkup buildKeyboard(Object... context) {
        List<KeyboardRow> rows = new ArrayList<>();

        // 1-й ряд: Добавить расход
        KeyboardRow row1 = new KeyboardRow();
        row1.add(MenuButton.ADD_EXPENSE);
        rows.add(row1);

        // 2-й ряд: История и Статистика
        KeyboardRow row2 = new KeyboardRow();
        row2.add(MenuButton.HISTORY);
        row2.add(MenuButton.STATISTICS);
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