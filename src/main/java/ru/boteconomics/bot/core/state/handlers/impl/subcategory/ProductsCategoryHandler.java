package ru.boteconomics.bot.core.state.handlers.impl.subcategory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.buttons.ProductsCategoryButton;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.core.state.handlers.base.BaseStateHandler;
import ru.boteconomics.bot.core.state.handlers.processors.SubcategoryProcessor;

@Slf4j
@Component
public class ProductsCategoryHandler extends BaseStateHandler {

    private final SubcategoryProcessor subcategoryProcessor;

    public ProductsCategoryHandler(SubcategoryProcessor subcategoryProcessor) {
        this.subcategoryProcessor = subcategoryProcessor;
        log.info("Создан ProductsCategoryHandler с прямой зависимостью от BaseStateHandler");
    }

    @Override
    public String getStateId() {
        return "PRODUCTS_CATEGORY_SELECTION";
    }

    @Override
    protected HandlerResponse processValidInput(String input, UserSession session) {
        log.debug("ProductsCategoryHandler: обработка ввода '{}'", input);

        // 1. Проверяем валидность ввода
        if (!ProductsCategoryButton.isProductsCategory(input)) {
            log.warn("Ввод '{}' не является валидной подкатегорией продуктов", input);
            return HandlerResponse.stay(
                    "Пожалуйста, выберите подкатегорию продуктов из списка",
                    getStateId()
            );
        }

        // 2. Сохраняем подкатегорию в сессию
        session.setProductsCategory(input);
        log.info("Выбрана и сохранена подкатегория продуктов: {}", input);

        // 3. Делегируем процессору стандартную обработку
        return subcategoryProcessor.process(input, session, getStateId());
    }
}