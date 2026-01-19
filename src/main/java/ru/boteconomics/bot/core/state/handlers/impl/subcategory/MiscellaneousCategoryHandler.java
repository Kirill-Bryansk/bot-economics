package ru.boteconomics.bot.core.state.handlers.impl.subcategory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.buttons.MiscellaneousCategoryButton;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.core.state.handlers.base.BaseSubcategoryHandler;
import ru.boteconomics.bot.core.state.handlers.processors.SubcategoryProcessor;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Переработанный обработчик для подкатегорий "Разное".
 * Использует BaseSubcategoryHandler для устранения дублирования кода.
 */
@Slf4j
@Component
public class MiscellaneousCategoryHandler extends BaseSubcategoryHandler {

    public MiscellaneousCategoryHandler(SubcategoryProcessor subcategoryProcessor) {
        super(subcategoryProcessor);
        log.info("Создан RefactoredMiscellaneousCategoryHandler");
    }

    @Override
    public String getStateId() {
        return "MISCELLANEOUS_CATEGORY_SELECTION";
    }

    @Override
    protected Predicate<String> getValidator() {
        return MiscellaneousCategoryButton::isMiscellaneousCategory;
    }

    @Override
    protected Consumer<UserSession> getSaver(String input) {
        return session -> {
            session.setMiscellaneousCategory(input);
            log.debug("Сохранена подкатегория 'Разное': {}", input);
        };
    }

    @Override
    protected String getDescription() {
        return "подкатегория 'Разное'";
    }

    @Override
    protected HandlerResponse handleBackAction(UserSession session) {
        // Можно переопределить при необходимости
        log.debug("Действие 'Назад' в RefactoredMiscellaneousCategoryHandler");
        return super.handleBackAction(session);
    }
}