package ru.boteconomics.bot.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.boteconomics.bot.domain.Transaction;

import java.util.ArrayList;
import java.util.List;

@Component
public class HistoryKeyboardFactory {

    /**
     * Создать клавиатуру для страницы истории (3 транзакции + навигация)
     */
    public InlineKeyboardMarkup createHistoryPage(List<Transaction> transactions, int currentPage, int totalPages) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        // 1. Кнопки транзакций (максимум 3)
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            String emoji = t.getType().name().equals("INCOME") ? "➕" : "➖";
            String buttonText = String.format("%d. %s %.2f₽",
                    i + 1, emoji, t.getAmount());

            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(createButton(buttonText, "VIEW_TRANSACTION_" + t.getId()));
            keyboard.add(row);
        }

        // 2. Навигация по страницам (если есть больше 1 страницы)
        if (totalPages > 1) {
            List<InlineKeyboardButton> navRow = new ArrayList<>();

            // Кнопка "Назад"
            if (currentPage > 0) {
                navRow.add(createButton("⬅️ Назад", "HISTORY_PAGE_" + (currentPage - 1)));
            }

            // Номер страницы (не кликабельный)
            navRow.add(createButton((currentPage + 1) + "/" + totalPages, "NO_ACTION"));

            // Кнопка "Вперед"
            if (currentPage < totalPages - 1) {
                navRow.add(createButton("Вперед ➡️", "HISTORY_PAGE_" + (currentPage + 1)));
            }

            keyboard.add(navRow);
        }

        // 3. Кнопка возврата в главное меню
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(createButton("🏠 Главное меню", "CANCEL"));
        keyboard.add(backRow);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);
        return markup;
    }

    /**
     * Создать меню для работы с транзакцией (редактирование/удаление)
     */
    public InlineKeyboardMarkup createTransactionMenu(Long transactionId) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        // 1. Редактировать сумму
        List<InlineKeyboardButton> editAmountRow = new ArrayList<>();
        editAmountRow.add(createButton("✏️ Изменить сумму", "EDIT_AMOUNT_" + transactionId));
        keyboard.add(editAmountRow);

        // 2. Изменить категорию
        List<InlineKeyboardButton> editCategoryRow = new ArrayList<>();
        editCategoryRow.add(createButton("🔄 Изменить категорию", "EDIT_CATEGORY_" + transactionId));
        keyboard.add(editCategoryRow);

        // 3. Удалить транзакцию
        List<InlineKeyboardButton> deleteRow = new ArrayList<>();
        deleteRow.add(createButton("🗑️ Удалить", "DELETE_" + transactionId));
        keyboard.add(deleteRow);

        // 4. Назад к истории (первая страница)
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(createButton("⬅️ Назад к истории", "SHOW_HISTORY_0"));
        keyboard.add(backRow);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);
        return markup;
    }

    /**
     * Создать клавиатуру подтверждения удаления
     */
    public InlineKeyboardMarkup createDeleteConfirmation(Long transactionId) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> confirmRow = new ArrayList<>();
        confirmRow.add(createButton("✅ Да, удалить", "CONFIRM_DELETE_" + transactionId));
        confirmRow.add(createButton("❌ Нет, отменить", "VIEW_TRANSACTION_" + transactionId));
        keyboard.add(confirmRow);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);
        return markup;
    }

    /**
     * Создать клавиатуру для возврата к меню транзакции
     */
    public InlineKeyboardMarkup createBackToTransactionMenu(Long transactionId) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(createButton("⬅️ Назад к транзакции", "VIEW_TRANSACTION_" + transactionId));
        keyboard.add(backRow);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);
        return markup;
    }

    /**
     * Создать простую клавиатуру с кнопкой "Назад в историю"
     */
    public InlineKeyboardMarkup createBackToHistory() {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(createButton("⬅️ Назад к истории", "SHOW_HISTORY_0"));
        keyboard.add(backRow);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);
        return markup;
    }

    /**
     * Вспомогательный метод для создания кнопки
     */
    private InlineKeyboardButton createButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }
}