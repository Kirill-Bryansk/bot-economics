package ru.boteconomics.bot.core.state.handlers.impl.subcategory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.buttons.TransportCategoryButton;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.core.state.handlers.base.BaseSubcategoryHandler;
import ru.boteconomics.bot.core.state.handlers.processors.SubcategoryProcessor;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Переработанный обработчик для подкатегорий "Транспорт".
 * Использует BaseSubcategoryHandler для устранения дублирования кода.
 */
@Slf4j
@Component
public class TransportCategoryHandler extends BaseSubcategoryHandler {

    public TransportCategoryHandler(SubcategoryProcessor subcategoryProcessor) {
        super(subcategoryProcessor);
        log.info("Создан RefactoredTransportCategoryHandler");
    }

    @Override
    public String getStateId() {
        return "TRANSPORT_CATEGORY_SELECTION";
    }

    @Override
    protected Predicate<String> getValidator() {
        return TransportCategoryButton::isTransportCategory;
    }

    @Override
    protected Consumer<UserSession> getSaver(String input) {
        return session -> {
            session.setTransportCategory(input);
            log.debug("Сохранена подкатегория 'Транспорт': {}", input);
        };
    }

    @Override
    protected String getDescription() {
        return "подкатегория транспорта";
    }

    // Можно переопределить handleBackAction если нужно особое поведение
    // @Override
    // protected HandlerResponse handleBackAction(UserSession session) {
    //     // Специфичная логика для транспорта
    // }
}