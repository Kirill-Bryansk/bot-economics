package ru.boteconomics.bot.core.state.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.buttons.CategoryButton;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;

/**
 * Обработчик выбора категории
 * Состояние: CATEGORY_SELECTION
 */
@Slf4j
@Component
public class CategorySelectionHandler extends BaseStateHandler {

    @Override
    public String getStateId() {
        return "CATEGORY_SELECTION";
    }

    @Override
    protected HandlerResponse processValidInput(String input, UserSession session) {
        log.info("Обработка выбора категории: '{}'", input);

        // 1. Проверяем действия
        HandlerResponse actionResponse = handleActionIfNeeded(input, session);
        if (actionResponse != null) {
            return actionResponse;
        }

        // 2. Обработка выбора категории
        if (CategoryButton.isCategory(input)) {
            session.setCategory(input);
            logSelection("категория", input);

            // Проверяем, выбрана ли категория "Дети"
            if (CategoryButton.CHILDREN.equals(input)) {
                logTransition(getStateId(), "CHILD_SELECTION");
                return HandlerResponse.next(
                        "Вы выбрали категорию: " + input + "\nТеперь выберите ребенка:",
                        "CHILD_SELECTION"
                );
            }
            // Проверяем, выбрана ли категория "Жилье"
            else if (CategoryButton.HOUSING.equals(input)) {
                logTransition(getStateId(), "HOUSING_CATEGORY_SELECTION");
                return HandlerResponse.next(
                        "Вы выбрали категорию: " + input + "\nТеперь выберите подкатегорию:",
                        "HOUSING_CATEGORY_SELECTION"
                );
            }
            // Проверяем, выбрана ли категория "Транспорт"
            else if (CategoryButton.TRANSPORT.equals(input)) {
                logTransition(getStateId(), "TRANSPORT_CATEGORY_SELECTION");
                return HandlerResponse.next(
                        "Вы выбрали категорию: " + input + "\nТеперь выберите подкатегорию:",
                        "TRANSPORT_CATEGORY_SELECTION"
                );
            }
            // Проверяем, выбрана ли категория "Продукты"
            else if (CategoryButton.FOOD.equals(input)) {
                logTransition(getStateId(), "PRODUCTS_CATEGORY_SELECTION");
                return HandlerResponse.next(
                        "Вы выбрали категорию: " + input + "\nТеперь выберите подкатегорию:",
                        "PRODUCTS_CATEGORY_SELECTION"
                );
            }
            // НОВОЕ: Проверяем, выбрана ли категория "Разное"
            else if (CategoryButton.OTHER.equals(input)) {
                logTransition(getStateId(), "MISCELLANEOUS_CATEGORY_SELECTION");
                return HandlerResponse.next(
                        "Вы выбрали категорию: " + input + "\nТеперь выберите подкатегорию:",
                        "MISCELLANEOUS_CATEGORY_SELECTION"
                );
            }
            else {
                // Для обычных категорий переходим к вводу суммы
                logTransition(getStateId(), "AMOUNT_INPUT");
                return HandlerResponse.next(
                        "Вы выбрали категорию: " + input + "\nТеперь введите сумму:",
                        "AMOUNT_INPUT"
                );
            }
        }

        // 3. Не должно происходить
        log.error("Непредвиденный ввод в CategorySelectionHandler: '{}'", input);
        return HandlerResponse.stay(
                "Пожалуйста, выберите категорию из предложенных кнопок",
                getStateId()
        );
    }
}