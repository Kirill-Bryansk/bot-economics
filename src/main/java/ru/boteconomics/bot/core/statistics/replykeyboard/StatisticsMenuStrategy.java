package ru.boteconomics.bot.core.statistics.replykeyboard;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.boteconomics.bot.core.buttons.ActionButton;
import ru.boteconomics.bot.core.replykeyboard.strategy.KeyboardStrategy;
import ru.boteconomics.bot.core.statistics.buttons.StatisticsButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Стратегия создания клавиатуры для меню статистики.
 * Создает reply-клавиатуру с кнопками выбора периода.
 */
@Slf4j
@Component
public class StatisticsMenuStrategy implements KeyboardStrategy {

    /**
     * Создать клавиатуру для меню статистики.
     */
    @Override
    public ReplyKeyboardMarkup buildKeyboard(Object... context) {
        log.debug("Создание клавиатуры для меню статистики");

        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первый ряд: кнопки периодов
        KeyboardRow periodRow = new KeyboardRow();
        periodRow.add(StatisticsButton.CURRENT_WEEK);
        periodRow.add(StatisticsButton.CURRENT_MONTH);
        keyboard.add(periodRow);

        // Второй ряд: кнопка "Назад"
        KeyboardRow backRow = new KeyboardRow();
        backRow.add(ActionButton.BACK);
        keyboard.add(backRow);

        // Создаем клавиатуру
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);
        keyboardMarkup.setSelective(true);

        log.info("Клавиатура статистики создана: {} кнопок",
                periodRow.size() + backRow.size());
        return keyboardMarkup;
    }

    /**
     * Создать компактную клавиатуру (все кнопки в одном ряду).
     * Может использоваться для мобильных устройств.
     */
    public ReplyKeyboardMarkup buildCompactKeyboard() {
        log.debug("Создание компактной клавиатуры статистики");

        List<KeyboardRow> keyboard = new ArrayList<>();

        // Все кнопки в одном ряду
        KeyboardRow row = new KeyboardRow();
        row.add(StatisticsButton.CURRENT_WEEK);
        row.add(StatisticsButton.CURRENT_MONTH);
        row.add(ActionButton.BACK);
        keyboard.add(row);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        log.debug("Компактная клавиатура создана: {} кнопок", row.size());
        return keyboardMarkup;
    }

    /**
     * Создать клавиатуру без кнопки "Назад".
     * Для случаев когда навигация не нужна.
     */
    public ReplyKeyboardMarkup buildKeyboardWithoutBack() {
        log.debug("Создание клавиатуры статистики без кнопки 'Назад'");

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow weekRow = new KeyboardRow();
        weekRow.add(StatisticsButton.CURRENT_WEEK);
        keyboard.add(weekRow);

        KeyboardRow monthRow = new KeyboardRow();
        monthRow.add(StatisticsButton.CURRENT_MONTH);
        keyboard.add(monthRow);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);

        log.debug("Клавиатура без 'Назад' создана: 2 кнопки");
        return keyboardMarkup;
    }
}