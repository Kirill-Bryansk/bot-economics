package ru.boteconomics.bot.core.state.handlers.processors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.buttons.MenuButton;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;

/**
 * Сервис для обработки главного меню.
 */
@Slf4j
@Component
public class MenuProcessor {

    /**
     * Обработка ввода в главном меню
     */
    public HandlerResponse process(String input, UserSession session, String stateId) {
        log.info("Обработка валидного ввода в главном меню: '{}'", input);

        // Обработка кнопок главного меню
        if (MenuButton.ADD_EXPENSE.equals(input)) {
            log.info("Пользователь выбрал 'Добавить расход'");
            return HandlerResponse.next(
                    "Выберите категорию расхода:",
                    "CATEGORY_SELECTION"
            );
        }

        if (MenuButton.HISTORY.equals(input)) {
            log.info("Пользователь выбрал 'История операций'");
            return HandlerResponse.stay(
                    "📋 История операций\n\n" +
                    "Функция находится в разработке. Скоро здесь появится история ваших расходов.",
                    stateId
            );
        }

        if (MenuButton.STATISTICS.equals(input)) {
            log.info("Пользователь выбрал 'Статистика'");
            return HandlerResponse.stay(
                    "📊 Статистика\n\n" +
                    "Функция находится в разработке. Скоро здесь появится статистика по вашим расходам.",
                    stateId
            );
        }

        // Не должно происходить
        log.error("Непредвиденный ввод в MainMenuHandler: '{}'", input);
        return HandlerResponse.stay(
                "Пожалуйста, используйте кнопки меню",
                stateId
        );
    }

    /**
     * Обработка действия 'Назад' в главном меню
     */
    public HandlerResponse handleBackAction(UserSession session, String stateId) {
        log.debug("Действие 'Назад' в главном меню - игнорируем");
        return HandlerResponse.stay("Вы в главном меню", stateId);
    }

    /**
     * Обработка действия 'Отмена' в главном меню
     */
    public HandlerResponse handleCancelAction(UserSession session, String stateId) {
        log.debug("Действие 'Отмена' в главном меню - очистка сессии");
        session.resetAll();
        return HandlerResponse.stay(
                "Сессия очищена. Вы в главном меню.",
                stateId
        );
    }
}