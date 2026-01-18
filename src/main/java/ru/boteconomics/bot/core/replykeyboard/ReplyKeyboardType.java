package ru.boteconomics.bot.core.replykeyboard;

/**
 * Типы Reply-клавиатур для разных состояний
 */
public enum ReplyKeyboardType {
    MAIN_MENU,
    CATEGORY_SELECTION,
    CHILD_SELECTION,
    CHILD_CATEGORY_SELECTION,
    HOUSING_CATEGORY_SELECTION,
    TRANSPORT_CATEGORY_SELECTION,
    PRODUCTS_CATEGORY_SELECTION,
    MISCELLANEOUS_CATEGORY_SELECTION, // НОВОЕ: для выбора подкатегории "Разное"
    AMOUNT_INPUT,
    CONFIRMATION
}