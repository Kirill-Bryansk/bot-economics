package ru.boteconomics.bot.core.session;

import org.springframework.stereotype.Component;

@Component
public class UserSessionManager {

    // Для одного пользователя - храним одну сессию
    private UserSession session;

    public UserSession getSession() {
        if (session == null) {
            session = new UserSession();
            System.out.println("[SESSION] Создана новая сессия");
        }
        return session;
    }

    public void clearSession() {
        session = null;
        System.out.println("[SESSION] Сессия очищена");
    }

    public void printSession() {
        if (session != null) {
            System.out.println("[SESSION] Текущая: " + session);
        } else {
            System.out.println("[SESSION] Сессия отсутствует");
        }
    }
}