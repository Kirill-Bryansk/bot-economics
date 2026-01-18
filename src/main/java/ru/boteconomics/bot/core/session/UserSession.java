package ru.boteconomics.bot.core.session;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Сессия пользователя для хранения данных во время диалога.
 * Является простым контейнером данных без бизнес-логики.
 */
@Getter
@Setter
@ToString
public class UserSession {

    // Текущее состояние диалога
    private String currentStateId = "MAIN_MENU";

    // Данные о расходе
    private String category;           // Выбранная категория
    private String childName;          // Имя ребенка (если категория = дети)
    private String childCategory;      // Категория ребенка (школа/секции/одежда)
    private String housingCategory;    // Подкатегория жилья (если категория = жилье)
    private String transportCategory;  // Подкатегория транспорта (если категория = транспорт)
    private String productsCategory;   // Подкатегория продуктов (если категория = продукты)
    private String miscellaneousCategory; // Подкатегория "Разное" (если категория = разное) // НОВОЕ
    private BigDecimal amount;         // Введенная сумма
    private LocalDateTime timestamp;   // Время создания сессии

    public UserSession() {
        this.timestamp = LocalDateTime.now();
    }

    // ========== МЕТОДЫ СБРОСА ==========

    /**
     * Полный сброс всех данных (при отмене или завершении)
     */
    public void resetAll() {
        this.currentStateId = "MAIN_MENU";
        this.category = null;
        this.childName = null;
        this.childCategory = null;
        this.housingCategory = null;
        this.transportCategory = null;
        this.productsCategory = null;
        this.miscellaneousCategory = null; // НОВОЕ
        this.amount = null;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Сброс данных для возврата к выбору категории
     */
    public void resetForCategorySelection() {
        this.childName = null;
        this.childCategory = null;
        this.housingCategory = null;
        this.transportCategory = null;
        this.productsCategory = null;
        this.miscellaneousCategory = null; // НОВОЕ
        this.amount = null;
    }

    /**
     * Сброс данных для возврата к выбору ребенка
     */
    public void resetForChildSelection() {
        this.childCategory = null;
        this.amount = null;
    }

    /**
     * Сброс данных для возврата к выбору категории ребенка
     */
    public void resetForChildCategorySelection() {
        this.amount = null;
    }

    /**
     * Сброс данных для возврата к выбору подкатегории жилья
     */
    public void resetForHousingCategorySelection() {
        this.amount = null;
    }

    /**
     * Сброс данных для возврата к выбору подкатегории транспорта
     */
    public void resetForTransportCategorySelection() {
        this.amount = null;
    }

    /**
     * Сброс данных для возврата к выбору подкатегории продуктов
     */
    public void resetForProductsCategorySelection() {
        this.amount = null;
    }

    /**
     * Сброс данных для возврата к выбору подкатегории "Разное" // НОВОЕ
     */
    public void resetForMiscellaneousCategorySelection() {
        this.amount = null;
    }

    /**
     * Сброс только суммы (для повторного ввода)
     */
    public void resetAmount() {
        this.amount = null;
    }

    /**
     * Сброс только данных ребенка
     */
    public void resetChildData() {
        this.childName = null;
        this.childCategory = null;
    }

    // ========== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ==========

    /**
     * Проверяет, выбрана ли категория "Дети"
     */
    public boolean isChildCategory() {
        return category != null && category.equals("👶 Дети");
    }

    /**
     * Проверяет, выбрана ли категория "Жилье"
     */
    public boolean isHousingCategory() {
        return category != null && category.equals("🏠 Жилье");
    }

    /**
     * Проверяет, выбрана ли категория "Транспорт"
     */
    public boolean isTransportCategory() {
        return category != null && category.equals("🚗 Транспорт");
    }

    /**
     * Проверяет, выбрана ли категория "Продукты"
     */
    public boolean isProductsCategory() {
        return category != null && category.equals("🛒 Продукты");
    }

    /**
     * Проверяет, выбрана ли категория "Разное" // НОВОЕ
     */
    public boolean isMiscellaneousCategory() {
        return category != null && category.equals("📦 Разное");
    }

    /**
     * Проверяет, все ли обязательные данные для сохранения заполнены
     */
    public boolean isReadyForSaving() {
        if (category == null || amount == null) {
            return false;
        }

        // Если категория "Дети", проверяем дополнительные поля
        if (isChildCategory()) {
            return childName != null && childCategory != null;
        }

        // Если категория "Жилье", проверяем подкатегорию
        if (isHousingCategory()) {
            return housingCategory != null;
        }

        // Если категория "Транспорт", проверяем подкатегорию
        if (isTransportCategory()) {
            return transportCategory != null;
        }

        // Если категория "Продукты", проверяем подкатегорию
        if (isProductsCategory()) {
            return productsCategory != null;
        }

        // Если категория "Разное", проверяем подкатегорию // НОВОЕ
        if (isMiscellaneousCategory()) {
            return miscellaneousCategory != null;
        }

        return true;
    }

    /**
     * Возвращает строковое представление данных для отладки
     */
    public String toDebugString() {
        return String.format(
                "UserSession{state=%s, category=%s, childName=%s, childCategory=%s, housingCategory=%s, transportCategory=%s, productsCategory=%s, miscellaneousCategory=%s, amount=%s}", // НОВОЕ: добавлен miscellaneousCategory
                currentStateId, category, childName, childCategory, housingCategory, transportCategory, productsCategory, miscellaneousCategory, amount
        );
    }
}