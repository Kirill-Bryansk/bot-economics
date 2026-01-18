package ru.boteconomics.bot.core.state.refactored.subcategory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.buttons.ProductsCategoryButton;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.core.state.refactored.base.BaseSubcategoryHandler;
import ru.boteconomics.bot.core.state.refactored.service.SubcategoryProcessor;

import java.util.function.Consumer;
import java.util.function.Predicate;

@Slf4j
@Component("refactoredProductsCategoryHandler")
public class RefactoredProductsCategoryHandler extends BaseSubcategoryHandler {

    public RefactoredProductsCategoryHandler(SubcategoryProcessor subcategoryProcessor) {
        super(subcategoryProcessor);
        log.info("Создан RefactoredProductsCategoryHandler");
    }

    @Override
    public String getStateId() {
        return "PRODUCTS_CATEGORY_SELECTION";
    }

    @Override
    protected Predicate<String> getValidator() {
        return ProductsCategoryButton::isProductsCategory;
    }

    @Override
    protected Consumer<UserSession> getSaver(String input) {
        return session -> {
            session.setProductsCategory(input);
            log.debug("Сохранена подкатегория 'Продукты': {}", input);
        };
    }

    @Override
    protected String getDescription() {
        return "подкатегория продуктов";
    }
}