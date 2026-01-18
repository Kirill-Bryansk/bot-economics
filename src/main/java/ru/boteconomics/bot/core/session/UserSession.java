package ru.boteconomics.bot.core.session;

/**
 * Простая сессия для одного пользователя.
 * Пока храним только текущий экран.
 */
public class UserSession {
    private String currentScreen = "MAIN_MENU";

    public String getCurrentScreen() {
        return currentScreen;
    }

    public void setCurrentScreen(String screen) {
        this.currentScreen = screen;
    }

    public void resetToMainMenu() {
        this.currentScreen = "MAIN_MENU";
    }

    @Override
    public String toString() {
        return "UserSession{currentScreen='" + currentScreen + "'}";
    }
}