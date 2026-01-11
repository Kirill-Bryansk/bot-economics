package ru.boteconomics.bot.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class MainKeyboardFactory {

    public ReplyKeyboardMarkup createMainMenu() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строка
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("💸 Добавить расход"));
        row1.add(new KeyboardButton("💰 Добавить доход"));

        // Вторая строка
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("📊 Отчет за сегодня"));
        row2.add(new KeyboardButton("📈 Отчет за месяц"));

        // Третья строка - ИЗМЕНЕНО: "История операций" вместо "Категории"
        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton("📋 История операций"));
        row3.add(new KeyboardButton("❓ Помощь"));

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    /**
     * Дополнительная клавиатура для удобства
     */
    public ReplyKeyboardMarkup createCancelMenu() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("🏠 Главное меню"));

        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }
}