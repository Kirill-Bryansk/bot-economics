package ru.boteconomics.bot.core.error.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.buttons.*;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.validation.AmountValidator;

import java.math.BigDecimal;

@Component
public class InputErrorHandler {

    @Autowired
    private AmountValidator amountValidator;

    public String validateInput(String input, String currentStateId) {
        // Действия всегда валидны
        if (ActionButton.isAction(input)) {
            return null;
        }

        switch (currentStateId) {
            case "MAIN_MENU":
                return MenuButton.isMenuButton(input) ? null : "Пожалуйста, используйте кнопки меню";

            case "CATEGORY_SELECTION":
                return CategoryButton.isCategory(input) ? null : "Выберите категорию из предложенных кнопок";

            case "CHILD_SELECTION":
                return ChildButton.isChildButton(input) ? null : "Выберите ребенка из списка";

            case "CHILD_CATEGORY_SELECTION":
                return ChildCategoryButton.isChildCategory(input) ? null : "Выберите категорию для ребенка";

            case "HOUSING_CATEGORY_SELECTION":
                return HousingCategoryButton.isHousingCategory(input) ? null : "Выберите подкатегорию из списка";

            case "TRANSPORT_CATEGORY_SELECTION":
                return TransportCategoryButton.isTransportCategory(input) ? null : "Выберите подкатегорию из списка";

            case "PRODUCTS_CATEGORY_SELECTION":
                return ProductsCategoryButton.isProductsCategory(input) ? null : "Выберите подкатегорию из списка";

            case "MISCELLANEOUS_CATEGORY_SELECTION": // НОВОЕ
                return MiscellaneousCategoryButton.isMiscellaneousCategory(input) ? null : "Выберите подкатегорию из списка";

            case "HEALTH_CATEGORY_SELECTION": // НОВОЕ
                return HealthCategoryButton.isHealthCategory(input) ? null : "Выберите подкатегорию из списка";

            case "AMOUNT_INPUT":
                return validateAmount(input);

            case "CONFIRMATION":
                return (ActionButton.CONFIRM.equals(input) || ActionButton.CANCEL.equals(input))
                        ? null : "Нажмите 'Подтвердить' или 'Отмена'";

            default:
                return "Неизвестное состояние системы";
        }
    }

    private String validateAmount(String input) {
        return amountValidator.validate(input);
    }

    /**
     * Проверить ввод и создать HandlerResponse при ошибке
     * @return null если ввод валиден, иначе HandlerResponse с ошибкой
     */
    public HandlerResponse validateAndCreateError(String input, String currentStateId) {
        String error = validateInput(input, currentStateId);
        if (error == null) {
            return null; // Нет ошибки
        }

        String message = "❌ " + error +
                         "\n\nТекущий шаг: " + getStepDescription(currentStateId);

        return HandlerResponse.stay(message, currentStateId);
    }

    private String getStepDescription(String stateId) {
        switch (stateId) {
            case "MAIN_MENU": return "главное меню";
            case "CATEGORY_SELECTION": return "выбор категории";
            case "CHILD_SELECTION": return "выбор ребенка";
            case "CHILD_CATEGORY_SELECTION": return "выбор категории для ребенка";
            case "HOUSING_CATEGORY_SELECTION": return "выбор подкатегории жилья";
            case "TRANSPORT_CATEGORY_SELECTION": return "выбор подкатегории транспорта";
            case "PRODUCTS_CATEGORY_SELECTION": return "выбор подкатегории продуктов";
            case "MISCELLANEOUS_CATEGORY_SELECTION": return "выбор подкатегории для 'Разное'"; // НОВОЕ
            case "HEALTH_CATEGORY_SELECTION": return "выбор подкатегории здравоохранения"; // НОВОЕ
            case "AMOUNT_INPUT": return "ввод суммы";
            case "CONFIRMATION": return "подтверждение";
            default: return "неизвестный шаг";
        }
    }
}