package ru.boteconomics.bot.core.replykeyboard.strategy;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.boteconomics.bot.core.buttons.HousingCategoryButton;
import ru.boteconomics.bot.core.buttons.ActionButton;
import java.util.ArrayList;
import java.util.List;

/**
 * Стратегия выбора подкатегории для жилья
 */
public class HousingCategoryStrategy implements KeyboardStrategy {

    @Override
    public ReplyKeyboardMarkup buildKeyboard(Object... context) {
        List<KeyboardRow> rows = new ArrayList<>();

        // Подкатегории жилья (используем константы из HousingCategoryButton)
        KeyboardRow row1 = new KeyboardRow();
        row1.add(HousingCategoryButton.UTILITIES);
        rows.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(HousingCategoryButton.REPAIR);
        rows.add(row2);

        KeyboardRow row3 = new KeyboardRow();
        row3.add(HousingCategoryButton.FURNITURE);
        rows.add(row3);

        KeyboardRow row4 = new KeyboardRow();
        row4.add(HousingCategoryButton.APPLIANCES);
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