package ru.boteconomics.bot.core.replykeyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.boteconomics.bot.core.replykeyboard.strategy.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Менеджер Reply-клавиатур (использует стратегии)
 */
@Component
public class ReplyKeyboardManager {

    private final Map<ReplyKeyboardType, KeyboardStrategy> strategies;

    public ReplyKeyboardManager() {
        this.strategies = new HashMap<>();
        registerStrategies();
    }

    /**
     * Регистрация всех стратегий
     */
    private void registerStrategies() {
        strategies.put(ReplyKeyboardType.MAIN_MENU, new MainMenuStrategy());
        strategies.put(ReplyKeyboardType.CATEGORY_SELECTION, new CategorySelectionStrategy());
        strategies.put(ReplyKeyboardType.CHILD_SELECTION, new ChildSelectionStrategy());
        strategies.put(ReplyKeyboardType.CHILD_CATEGORY_SELECTION, new ChildCategorySelectionStrategy());
        strategies.put(ReplyKeyboardType.HOUSING_CATEGORY_SELECTION, new HousingCategoryStrategy());
        strategies.put(ReplyKeyboardType.TRANSPORT_CATEGORY_SELECTION, new TransportCategoryStrategy());
        strategies.put(ReplyKeyboardType.PRODUCTS_CATEGORY_SELECTION, new ProductsCategoryStrategy());
        strategies.put(ReplyKeyboardType.MISCELLANEOUS_CATEGORY_SELECTION, new MiscellaneousCategoryStrategy()); // НОВОЕ
        strategies.put(ReplyKeyboardType.AMOUNT_INPUT, new AmountInputStrategy());
        strategies.put(ReplyKeyboardType.CONFIRMATION, new ConfirmationStrategy());
    }

    /**
     * Получить клавиатуру по типу
     */
    public ReplyKeyboardMarkup getKeyboard(ReplyKeyboardType type, Object... context) {
        KeyboardStrategy strategy = strategies.get(type);
        if (strategy == null) {
            // Fallback на главное меню
            strategy = strategies.get(ReplyKeyboardType.MAIN_MENU);
        }
        return strategy.buildKeyboard(context);
    }

    /**
     * Получить клавиатуру для состояния
     */
    public ReplyKeyboardMarkup getKeyboardForState(String stateId, Object... context) {
        ReplyKeyboardType type = mapStateIdToType(stateId);
        return getKeyboard(type, context);
    }

    /**
     * Маппинг идентификатора состояния на тип клавиатуры
     */
    private ReplyKeyboardType mapStateIdToType(String stateId) {
        if (stateId == null) return ReplyKeyboardType.MAIN_MENU;

        switch (stateId) {
            case "MAIN_MENU":
                return ReplyKeyboardType.MAIN_MENU;

            case "CATEGORY_SELECTION":
                return ReplyKeyboardType.CATEGORY_SELECTION;

            case "CHILD_SELECTION":
                return ReplyKeyboardType.CHILD_SELECTION;

            case "CHILD_CATEGORY_SELECTION":
                return ReplyKeyboardType.CHILD_CATEGORY_SELECTION;

            case "HOUSING_CATEGORY_SELECTION":
                return ReplyKeyboardType.HOUSING_CATEGORY_SELECTION;

            case "TRANSPORT_CATEGORY_SELECTION":
                return ReplyKeyboardType.TRANSPORT_CATEGORY_SELECTION;

            case "PRODUCTS_CATEGORY_SELECTION":
                return ReplyKeyboardType.PRODUCTS_CATEGORY_SELECTION;

            case "MISCELLANEOUS_CATEGORY_SELECTION": // НОВОЕ
                return ReplyKeyboardType.MISCELLANEOUS_CATEGORY_SELECTION;

            case "AMOUNT_INPUT":
                return ReplyKeyboardType.AMOUNT_INPUT;

            case "CONFIRMATION":
                return ReplyKeyboardType.CONFIRMATION;

            default:
                return ReplyKeyboardType.MAIN_MENU; // fallback
        }
    }
}