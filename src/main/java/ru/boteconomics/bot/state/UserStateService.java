package ru.boteconomics.bot.state;

import org.springframework.stereotype.Service;
import ru.boteconomics.bot.domain.Category;
import ru.boteconomics.bot.domain.Child;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserStateService {

    public enum State {
        IDLE,               // Ничего не делает
        WAITING_AMOUNT,     // Ждет ввода суммы
        WAITING_CATEGORY,   // Ждет выбора категории
        WAITING_CHILD,      // Ждет выбора ребенка
        EDITING_TRANSACTION // Редактируем существующую транзакцию
    }

    public enum ActionType {
        ADD_EXPENSE,        // Добавление расхода
        ADD_INCOME,         // Добавление дохода
        EDIT_TRANSACTION    // Редактирование транзакции
    }

    private static class UserData {
        State state = State.IDLE;
        ActionType actionType;
        Double pendingAmount;
        Category pendingCategory; // ← Изменено с String на Category
        Child pendingChild;       // ← Изменено с String на Child
        Long editingTransactionId; // ← ID транзакции для редактирования

        void reset() {
            state = State.IDLE;
            actionType = null;
            pendingAmount = null;
            pendingCategory = null;
            pendingChild = null;
            editingTransactionId = null;
        }
    }

    private final Map<Long, UserData> userStates = new HashMap<>();

    // Начать добавление расхода
    public void startAddExpense(Long userId) {
        UserData data = getUserData(userId);
        data.reset();
        data.state = State.WAITING_AMOUNT;
        data.actionType = ActionType.ADD_EXPENSE;
    }

    // Начать добавление дохода
    public void startAddIncome(Long userId) {
        UserData data = getUserData(userId);
        data.reset();
        data.state = State.WAITING_AMOUNT;
        data.actionType = ActionType.ADD_INCOME;
    }

    // Начать редактирование транзакции
    public void startEditingTransaction(Long userId, Long transactionId) {
        UserData data = getUserData(userId);
        data.reset();
        data.state = State.EDITING_TRANSACTION;
        data.actionType = ActionType.EDIT_TRANSACTION;
        data.editingTransactionId = transactionId;
        // Можно предзаполнить amount и category из существующей транзакции
    }

    // Сохранить сумму
    public void setAmount(Long userId, Double amount) {
        UserData data = getUserData(userId);
        data.pendingAmount = amount;
        if (data.state == State.EDITING_TRANSACTION) {
            // При редактировании сразу применяем изменение
            data.state = State.IDLE;
        } else {
            data.state = State.WAITING_CATEGORY;
        }
    }

    // Сохранить категорию
    public void setCategory(Long userId, Category category) {
        UserData data = getUserData(userId);
        data.pendingCategory = category;

        // Проверяем, нужно ли выбрать ребенка
        if (Category.isChildCategory(category)) {
            data.state = State.WAITING_CHILD;
        } else {
            data.state = State.IDLE;
        }
    }

    // Сохранить выбор ребенка
    public void setChild(Long userId, Child child) {
        UserData data = getUserData(userId);
        data.pendingChild = child;
        data.state = State.IDLE;
    }

    // Получить текущее состояние
    public State getState(Long userId) {
        return getUserData(userId).state;
    }

    // Получить действие
    public ActionType getActionType(Long userId) {
        return getUserData(userId).actionType;
    }

    // Получить сохраненную сумму
    public Double getPendingAmount(Long userId) {
        return getUserData(userId).pendingAmount;
    }

    // Получить сохраненную категорию
    public Category getPendingCategory(Long userId) {
        return getUserData(userId).pendingCategory;
    }

    // Получить ID редактируемой транзакции
    public Long getEditingTransactionId(Long userId) {
        return getUserData(userId).editingTransactionId;
    }

    // Получить данные для завершения транзакции
    public TransactionData completeTransaction(Long userId) {
        UserData data = getUserData(userId);
        TransactionData result = new TransactionData(
                data.actionType,
                data.pendingAmount,
                data.pendingCategory,
                data.pendingChild,
                data.editingTransactionId
        );
        data.reset();
        return result;
    }

    // Сбросить состояние
    public void reset(Long userId) {
        getUserData(userId).reset();
    }

    private UserData getUserData(Long userId) {
        return userStates.computeIfAbsent(userId, k -> new UserData());
    }

    // Данные для транзакции
    public static class TransactionData {
        public final ActionType actionType;
        public final Double amount;
        public final Category category;
        public final Child child;
        public final Long transactionId; // Для редактирования

        public TransactionData(ActionType actionType, Double amount,
                               Category category, Child child, Long transactionId) {
            this.actionType = actionType;
            this.amount = amount;
            this.category = category;
            this.child = child;
            this.transactionId = transactionId;
        }
    }
}