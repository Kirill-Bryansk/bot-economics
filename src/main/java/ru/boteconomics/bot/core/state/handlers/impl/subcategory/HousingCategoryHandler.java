package ru.boteconomics.bot.core.state.handlers.impl.subcategory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.buttons.HousingCategoryButton;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.core.state.handlers.base.BaseSubcategoryHandler;
import ru.boteconomics.bot.core.state.handlers.processors.SubcategoryProcessor;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Переработанный обработчик для подкатегорий "Жилье".
 * Использует BaseSubcategoryHandler для устранения дублирования кода.
 */
@Slf4j
@Component
public class HousingCategoryHandler extends BaseSubcategoryHandler {

    public HousingCategoryHandler(SubcategoryProcessor subcategoryProcessor) {
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