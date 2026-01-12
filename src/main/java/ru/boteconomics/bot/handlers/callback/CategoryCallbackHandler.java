package ru.boteconomics.bot.handlers.callback;

import org.springframework.stereotype.Component;
import ru.boteconomics.bot.domain.Category;
import ru.boteconomics.bot.domain.Child;
import ru.boteconomics.bot.domain.TransactionType;
import ru.boteconomics.bot.handlers.HandlerResult;
import ru.boteconomics.bot.keyboard.InlineKeyboardFactory;
import ru.boteconomics.bot.keyboard.MainKeyboardFactory;
import ru.boteconomics.bot.service.HistoryService;
import ru.boteconomics.bot.service.TransactionService;
import ru.boteconomics.bot.state.UserStateService;

import java.math.BigDecimal;

@Component
public class CategoryCallbackHandler extends BaseCallbackHandler {

    private final InlineKeyboardFactory inlineKeyboardFactory;
    private final UserStateService userStateService;
    private final TransactionService transactionService;

    public CategoryCallbackHandler(InlineKeyboardFactory inlineKeyboardFactory,
                                   UserStateService userStateService,
                                   TransactionService transactionService,
                                   HistoryService historyService,
                                   MainKeyboardFactory mainKeyboardFactory) {
        super(historyService, mainKeyboardFactory);
        this.inlineKeyboardFactory = inlineKeyboardFactory;
        this.userStateService = userStateService;
        this.transactionService = transactionService;
    }

    @Override
    public boolean canHandle(String callbackData) {
        return callbackData.startsWith("CATEGORY_") ||
               callbackData.startsWith("SHOW_CHILD_") ||
               callbackData.equals("BACK_TO_CATEGORIES") ||
               callbackData.equals("BACK_TO_CHILDREN") ||
               callbackData.equals("SHOW_CHILDREN");
    }

    @Override
    public HandlerResult handle(Long chatId, String callbackData) {
        if (callbackData.startsWith("CATEGORY_")) {
            return handleCategorySelection(chatId, callbackData);
        } else if (callbackData.startsWith("SHOW_CHILD_")) {
            return handleChildSelection(chatId, callbackData);
        } else if (callbackData.equals("BACK_TO_CATEGORIES")) {
            return backToCategories();
        } else if (callbackData.equals("BACK_TO_CHILDREN") ||
                   callbackData.equals("SHOW_CHILDREN")) {
            return showChildren();
        }

        return error("Неизвестная команда категории");
    }

    private HandlerResult handleCategorySelection(Long chatId, String callbackData) {
        Category category = Category.fromCallback(callbackData);

        if (category == null) {
            return error("Категория не найдена");
        }

        System.out.println("DEBUG: Category selected: " + category.getFullName());

        // Сохраняем категорию в состояние
        userStateService.setCategory(chatId, category);

        // Проверяем, нужно ли выбрать ребенка
        if (Category.isChildCategory(category)) {
            // Извлекаем ребенка из категории
            Child child = Category.extractChildFromCategory(category);

            if (child != null) {
                System.out.println("DEBUG: Extracted child from category: " + child.name());

                // Проверяем, выбран ли уже ребенок
                Child savedChild = userStateService.getPendingChild(chatId); // ← Нужно добавить метод!

                if (savedChild == null) {
                    // Если ребенок не выбран - показываем список детей
                    return showChildren();
                } else {
                    // Если ребенок уже выбран - завершаем транзакцию
                    return completeTransaction(chatId, category);
                }
            } else {
                System.out.println("ERROR: Cannot extract child from category: " + category.name());
                return showChildren();
            }
        }

        // Если ребенок не нужен - завершаем транзакцию
        return completeTransaction(chatId, category);
    }

    private HandlerResult handleChildSelection(Long chatId, String callbackData) {
        System.out.println("DEBUG: Processing child callback: " + callbackData);

        // Парсим ребенка
        Child child = Child.fromCallback(callbackData);

        if (child == null) {
            System.out.println("ERROR: Child is null for callback: " + callbackData);
            return error("Ребенок не найден");
        }

        System.out.println("DEBUG: Child selected: " + child.getFullName());

        // Сохраняем ребенка в состояние (НЕ вызываем completeTransaction!)
        userStateService.setChild(chatId, child);

        // Получаем категорию из состояния (она уже должна быть сохранена)
        Category category = userStateService.getPendingCategory(chatId);

        if (category == null) {
            // Если категория не сохранена, показываем категории для ребенка
            return withKeyboard(
                    "👨‍👩‍👧‍👦 Выберите категорию для " + child.getFullName() + ":",
                    inlineKeyboardFactory.createChildCategories(child)
            );
        } else {
            // Если категория уже есть (например, пользователь вернулся назад) - завершаем транзакцию
            return completeTransaction(chatId, category);
        }
    }

    private HandlerResult showChildren() {
        return withKeyboard(
                "👨‍👩‍👧‍👦 Выберите ребенка:",
                inlineKeyboardFactory.createChildrenCategories()
        );
    }

    private HandlerResult backToCategories() {
        return withKeyboard(
                "💸 Выберите категорию расхода:",
                inlineKeyboardFactory.createCategories(TransactionType.EXPENSE)
        );
    }

    private HandlerResult completeTransaction(Long chatId, Category category) {
        var data = userStateService.completeTransaction(chatId);

        TransactionType type = data.actionType == UserStateService.ActionType.ADD_EXPENSE
                ? TransactionType.EXPENSE
                : TransactionType.INCOME;

        if (data.actionType == UserStateService.ActionType.EDIT_TRANSACTION &&
            data.transactionId != null) {
            // Обновляем существующую транзакцию
            transactionService.updateTransaction(
                    data.transactionId,
                    BigDecimal.valueOf(data.amount),
                    category
            );

            return success(String.format("Транзакция обновлена!\n\nСумма: %.2f руб.\nКатегория: %s",
                    data.amount, category.getFullName()));
        } else {
            // Сохраняем новую транзакцию
            transactionService.saveTransaction(
                    chatId,
                    type,
                    BigDecimal.valueOf(data.amount),
                    category,
                    null
            );

            String typeText = data.actionType == UserStateService.ActionType.ADD_EXPENSE
                    ? "Расход" : "Доход";

            return success(String.format("Успешно добавлено!\n\nТип: %s\nСумма: %.2f руб.\nКатегория: %s",
                    typeText, data.amount, category.getFullName()));
        }
    }
}