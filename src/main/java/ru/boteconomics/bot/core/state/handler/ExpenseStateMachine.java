package ru.boteconomics.bot.core.state.handler;

import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.handlers.HandlerResult;

@Component
public class ExpenseStateMachine {

    private final MainMenuHandler mainMenuHandler;

    public ExpenseStateMachine(MainMenuHandler mainMenuHandler) {
        this.mainMenuHandler = mainMenuHandler;
        System.out.println("[STATE MACHINE] Создан");
    }

    public HandlerResult process(String userInput, UserSession session) {
        String currentState = session.getCurrentScreen();
        System.out.println("[STATE MACHINE] Текущее состояние: " + currentState);

        // Пока обрабатываем только главное меню
        if ("MAIN_MENU".equals(currentState)) {
            System.out.println("[STATE MACHINE] Передаю в MainMenuHandler");
            return mainMenuHandler.handle(userInput, session);
        }

        // Если состояние неизвестно - возвращаем в главное меню
        System.out.println("[STATE MACHINE] Неизвестное состояние, возвращаю в главное меню");
        session.resetToMainMenu();
        return mainMenuHandler.handle("MAIN_MENU", session);
    }
}