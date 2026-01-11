package ru.boteconomics.bot.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.boteconomics.bot.domain.Category;
import ru.boteconomics.bot.domain.Child;
import ru.boteconomics.bot.domain.TransactionType;
import ru.boteconomics.bot.service.CategoryService;

import java.util.ArrayList;
import java.util.List;

@Component
public class InlineKeyboardFactory {

    private final CategoryService categoryService;

    public InlineKeyboardFactory(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // ========== ОБЩИЕ МЕТОДЫ ==========

    public InlineKeyboardMarkup createCancelButton() {
        return createSingleButton("❌ Отмена", "CANCEL");
    }

    private InlineKeyboardMarkup createSingleButton(String text, String callbackData) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(createButton(text, callbackData));

        rows.add(row);
        markup.setKeyboard(rows);
        return markup;
    }

    // ========== КАТЕГОРИИ ==========

    public InlineKeyboardMarkup createCategories(TransactionType type) {
        List<Category> categories = categoryService.getCategoriesByType(type);

        if (type == TransactionType.EXPENSE) {
            return createExpenseCategories(categories);
        } else {
            return createIncomeCategories(categories);
        }
    }

    private InlineKeyboardMarkup createExpenseCategories(List<Category> categories) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        // Не детские категории (по 2 в ряд)
        List<Category> nonChildCategories = categories.stream()
                .filter(cat -> !Category.isChildCategory(cat))
                .toList();

        for (int i = 0; i < nonChildCategories.size(); i += 2) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(createCategoryButton(nonChildCategories.get(i)));

            if (i + 1 < nonChildCategories.size()) {
                row.add(createCategoryButton(nonChildCategories.get(i + 1)));
            }

            rows.add(row);
        }

        // Кнопка "Дети"
        rows.add(List.of(createButton("👶 Дети", "SHOW_CHILDREN")));

        markup.setKeyboard(rows);
        return markup;
    }

    private InlineKeyboardMarkup createIncomeCategories(List<Category> categories) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (Category category : categories) {
            rows.add(List.of(createCategoryButton(category)));
        }

        markup.setKeyboard(rows);
        return markup;
    }

    // ========== ДЕТИ ==========

    public InlineKeyboardMarkup createChildrenCategories() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (Child child : Child.values()) {
            rows.add(List.of(createButton(child.getFullName(), "SHOW_" + child.name())));
        }

        rows.add(List.of(createButton("⬅️ Назад к категориям", "BACK_TO_CATEGORIES")));
        markup.setKeyboard(rows);
        return markup;
    }

    public InlineKeyboardMarkup createChildCategories(Child child) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        // Получаем категории для ребенка
        List<Category> childCategories = categoryService.getCategoriesByType(TransactionType.EXPENSE)
                .stream()
                .filter(cat -> Category.isChildCategory(cat) &&
                               Category.extractChildFromCategory(cat) == child)
                .toList();

        for (Category category : childCategories) {
            rows.add(List.of(createCategoryButton(category)));
        }

        rows.add(List.of(createButton("⬅️ Назад", "BACK_TO_CHILDREN")));
        markup.setKeyboard(rows);
        return markup;
    }

    // ========== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ==========

    private InlineKeyboardButton createCategoryButton(Category category) {
        return createButton(category.getFullName(), category.getCallbackData());
    }

    private InlineKeyboardButton createButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }
}