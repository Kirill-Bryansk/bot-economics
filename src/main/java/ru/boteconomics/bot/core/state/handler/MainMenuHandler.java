package ru.boteconomics.bot.core.state.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.buttons.MenuButton;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;

/**
 * Обработчик главного меню
 * Состояние: MAIN_MENU
 */
@Slf4j
@Component
public class MainMenuHandler extends BaseStateHandler {

    @Override
    public String getStateId() {
        return "MAIN_MENU";
    }

    @Override
    protected HandlerResponse processValidInput(String input, UserSession session) {
        log.info("Обработка валидного ввода в главном меню: '{}'", input);

        // 1. Проверяем, не является ли ввод действием (Назад/Отмена)
        HandlerResponse actionResponse = handleActionIfNeeded(input, session);
        if (actionResponse != null) {
            return actionResponse;
        }

        // 2. Обработка кнопок главного меню
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
                    getStateId()  // <-- ДОБАВЛЕНО: передаём текущее состояние
            );
        }

        if (MenuButton.STATISTICS.equals(input)) {
            log.info("Пользователь выбрал 'Статистика'");
            return HandlerResponse.stay(
                    "📊 Статистика\n\n" +
                    "Функция находится в разработке. Скоро здесь появится статистика по вашим расходам.",
                    getStateId()  // <-- ДОБАВЛЕНО: передаём текущее состояние
            );
        }

        // 3. Если попали сюда - значит InputErrorHandler не сработал как должен
        log.error("Непредвиденный ввод в MainMenuHandler: '{}'", input);
        return HandlerResponse.stay(
                "Пожалуйста, используйте кнопки меню",
                getStateId()  // <-- ДОБАВЛЕНО: передаём текущее состояние
        );
    }

    @Override
    protected HandlerResponse handleBackAction(UserSession session) {
        log.debug("Действие 'Назад' в главном меню - игнорируем");
        return HandlerResponse.stay("Вы в главном меню", getStateId()); // <-- ДОБАВЛЕНО
    }

    @Override
    protected HandlerResponse handleCancelAction(UserSession session) {
        log.debug("Действие 'Отмена' в главном меню - очистка сессии");
        session.resetAll();
        return HandlerResponse.stay(
                "Сессия очищена. Вы в главном меню.",
                getStateId()  // <-- ДОБАВЛЕНО
        );
    }
}