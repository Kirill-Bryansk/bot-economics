package ru.boteconomics.bot.core.state.handler;

import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.handlers.HandlerResult;
import ru.boteconomics.bot.core.replykeyboard.ReplyKeyboardManager;
import ru.boteconomics.bot.core.replykeyboard.ReplyKeyboardType;
import ru.boteconomics.bot.core.buttons.MenuButton;

@Component
public class MainMenuHandler {

    private final ReplyKeyboardManager keyboardManager;

    public MainMenuHandler(ReplyKeyboardManager keyboardManager) {
        this.keyboardManager = keyboardManager;
        System.out.println("[HANDLER] MainMenuHandler создан");
    }

    public HandlerResult handle(String userInput, UserSession session) {
        System.out.println("[HANDLER] MainMenuHandler обрабатывает: '" + userInput + "'");

        // Обрабатываем ввод пользователя
        if (MenuButton.ADD_EXPENSE.equals(userInput)) {
            System.out.println("[HANDLER] Пользователь нажал 'Добавить расход'");
            session.setCurrentScreen("CATEGORY_SELECTION");

            return HandlerResult.next(
                    "Выберите категорию расхода:",
                    keyboardManager.getKeyboard(ReplyKeyboardType.CATEGORY_SELECTION),
                    "CATEGORY_SELECTION"
            );
        }

        if (MenuButton.HISTORY.equals(userInput)) {
            System.out.println("[HANDLER] Пользователь нажал 'История'");
            return HandlerResult.stay(
                    "📋 История операций (функция в разработке)",
                    keyboardManager.getKeyboard(ReplyKeyboardType.MAIN_MENU)
            );
        }

        if (MenuButton.STATISTICS.equals(userInput)) {
            System.out.println("[HANDLER] Пользователь нажал 'Статистика'");
            return HandlerResult.stay(
                    "📊 Статистика расходов (функция в разработке)",
                    keyboardManager.getKeyboard(ReplyKeyboardType.MAIN_MENU)
            );
        }

        if ("/start".equals(userInput) || MenuButton.MAIN_MENU.equals(userInput)) {
            System.out.println("[HANDLER] Команда /start или 'Главное меню'");
            session.resetToMainMenu();
            return HandlerResult.stay(
                    "🏠 Главное меню",
                    keyboardManager.getKeyboard(ReplyKeyboardType.MAIN_MENU)
            );
        }

        // Неизвестная команда
        System.out.println("[HANDLER] Неизвестная команда: " + userInput);
        return HandlerResult.stay(
                "Я не понимаю эту команду. Используйте кнопки меню.",
                keyboardManager.getKeyboard(ReplyKeyboardType.MAIN_MENU)
        );
    }
}