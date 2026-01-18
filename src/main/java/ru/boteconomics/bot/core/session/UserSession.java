package ru.boteconomics.bot.core.session;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Сессия пользователя для хранения данных во время диалога.
 * Является простым контейнером данных без бизнес-логики.
 */
public class UserSession {

    // Текущее состояние диалога
    private String currentStateId = "MAIN_MENU";

    // Данные о расходе
    private String category;           // Выбранная категория
    private String childName;          // Имя ребенка (если категория = дети)
    private String childCategory;      // Категория ребенка (школа/секции/одежда)
    private BigDecimal amount;         // Введенная сумма
    private LocalDateTime timestamp;   // Время создания сессии

    // Временные данные (опционально, для расширения)
    // private Map<String, Object> temporaryData = new HashMap<>();

    public UserSession() {
        this.timestamp = LocalDateTime.now();
    }

    // ========== ГЕТТЕРЫ И СЕТТЕРЫ ==========

    public String getCurrentStateId() {
        return currentStateId;
    }

    public void setCurrentStateId(String currentStateId) {
        this.currentStateId = currentStateId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getChildCategory() {
        return childCategory;
    }

    public void setChildCategory(String childCategory) {
        this.childCategory = childCategory;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
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
        this.amount = null;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Сброс данных для возврата к выбору категории
     */
    public void resetForCategorySelection() {
        this.childName = null;
        this.childCategory = null;
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

        return true;
    }

    /**
     * Возвращает строковое представление данных для отладки
     */
    public String toDebugString() {
        return String.format(
                "UserSession{state=%s, category=%s, childName=%s, childCategory=%s, amount=%s}",
                currentStateId, category, childName, childCategory, amount
        );
    }

    @Override
    public String toString() {
        return toDebugString();
    }
}
