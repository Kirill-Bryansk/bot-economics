package ru.boteconomics.bot.core.state.handlers.processors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.buttons.CategoryButton;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;

/**
 * Сервис для обработки выбора категории.
 * Теперь содержит только бизнес-логику определения следующего состояния.
 */
@Slf4j
@Component
public class CategoryProcessor {

    /**
     * Обработка выбора категории.
     * Только определяет следующее состояние на основе выбранной категории.
     * Валидация и сохранение вынесены в обработчик.
     */
    public HandlerResponse process(
            String input,
            UserSession session,
            String stateId) {

        log.debug("CategoryProcessor: обработка категории '{}'", input);

        // Определяем следующее состояние на основе выбранной категории
        return determineNextState(input, session);
    }

    /**
     * Определяет следующее состояние на основе выбранной категории
     */
    private HandlerResponse determineNextState(String category, UserSession session) {
        // Проверяем, выбрана ли категория "Дети"
        if (CategoryButton.CHILDREN.equals(category)) {
            log.info("Переход: CATEGORY_SELECTION → CHILD_SELECTION");
            return HandlerResponse.next(
                    "Вы выбрали категорию: " + category + "\nТеперь выберите ребенка:",
                    "CHILD_SELECTION"
            );
        }
        // Проверяем, выбрана ли категория "Жилье"
        else if (CategoryButton.HOUSING.equals(category)) {
            log.info("Переход: CATEGORY_SELECTION → HOUSING_CATEGORY_SELECTION");
            return HandlerResponse.next(
                    "Вы выбрали категорию: " + category + "\nТеперь выберите подкатегорию:",
                    "HOUSING_CATEGORY_SELECTION"
            );
        }
        // Проверяем, выбрана ли категория "Транспорт"
        else if (CategoryButton.TRANSPORT.equals(category)) {
            log.info("Переход: CATEGORY_SELECTION → TRANSPORT_CATEGORY_SELECTION");
            return HandlerResponse.next(
                    "Вы выбрали категорию: " + category + "\nТеперь выберите подкатегорию:",
                    "TRANSPORT_CATEGORY_SELECTION"
            );
        }
        // Проверяем, выбрана ли категория "Продукты"
        else if (CategoryButton.FOOD.equals(category)) {
            log.info("Переход: CATEGORY_SELECTION → PRODUCTS_CATEGORY_SELECTION");
            return HandlerResponse.next(
                    "Вы выбрали категорию: " + category + "\nТеперь выберите подкатегорию:",
                    "PRODUCTS_CATEGORY_SELECTION"
            );
        }
        // Проверяем, выбрана ли категория "Здоровье"
        else if (CategoryButton.HEALTH.equals(category)) {
            log.info("Переход: CATEGORY_SELECTION → HEALTH_CATEGORY_SELECTION");
            return HandlerResponse.next(
                    "Вы выбрали категорию: " + category + "\nТеперь выберите подкатегорию:",
                    "HEALTH_CATEGORY_SELECTION"
            );
        }
        // Проверяем, выбрана ли категория "Разное"
        else if (CategoryButton.OTHER.equals(category)) {
            log.info("Переход: CATEGORY_SELECTION → MISCELLANEOUS_CATEGORY_SELECTION");
            return HandlerResponse.next(
                    "Вы выбрали категорию: " + category + "\nТеперь выберите подкатегорию:",
                    "MISCELLANEOUS_CATEGORY_SELECTION"
            );
        }
        else {
            // Для обычных категорий переходим к вводу суммы
            log.info("Переход: CATEGORY_SELECTION → AMOUNT_INPUT");
            return HandlerResponse.next(
                    "Вы выбрали категорию: " + category + "\nТеперь введите сумму:",
                    "AMOUNT_INPUT"
            );
        }
    }
}