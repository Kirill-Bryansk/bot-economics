package ru.boteconomics.bot.core.state.refactored.subcategory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.buttons.HousingCategoryButton;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.core.state.refactored.base.BaseSubcategoryHandler;
import ru.boteconomics.bot.core.state.refactored.service.SubcategoryProcessor;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Переработанный обработчик для подкатегорий "Жилье".
 * Использует BaseSubcategoryHandler для устранения дублирования кода.
 */
@Slf4j
@Component("refactoredHousingCategoryHandler") // Уникальное имя бина!
public class RefactoredHousingCategoryHandler extends BaseSubcategoryHandler {

    public RefactoredHousingCategoryHandler(SubcategoryProcessor subcategoryProcessor) {
        super(subcategoryProcessor);
        log.info("Создан RefactoredHousingCategoryHandler");
    }

    @Override
    public String getStateId() {
        return "HOUSING_CATEGORY_SELECTION";
    }

    @Override
    protected Predicate<String> getValidator() {
        return HousingCategoryButton::isHousingCategory;
    }

    @Override
    protected Consumer<UserSession> getSaver(String input) {
        return session -> {
            session.setHousingCategory(input);
            log.debug("Сохранена подкатегория 'Жилье': {}", input);
        };
    }

    @Override
    protected String getDescription() {
        return "подкатегория жилья";
    }

    // Можно переопределить handleBackAction если нужно особое поведение
    // @Override
    // protected HandlerResponse handleBackAction(UserSession session) {
    //     // Специфичная логика для жилья
    // }
}