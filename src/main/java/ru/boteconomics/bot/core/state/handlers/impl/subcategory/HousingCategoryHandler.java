package ru.boteconomics.bot.core.state.handlers.impl.subcategory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.buttons.HousingCategoryButton;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.core.state.handlers.base.BaseStateHandler;
import ru.boteconomics.bot.core.state.handlers.processors.SubcategoryProcessor;

/**
 * Обработчик для подкатегорий "Жилье".
 * Наследует напрямую от BaseStateHandler для устранения избыточных уровней абстракции.
 */
@Slf4j
@Component
public class HousingCategoryHandler extends BaseStateHandler {

    private final SubcategoryProcessor subcategoryProcessor;

    public HousingCategoryHandler(SubcategoryProcessor subcategoryProcessor) {
        this.subcategoryProcessor = subcategoryProcessor;
        log.info("Создан HousingCategoryHandler с прямой зависимостью от BaseStateHandler");
    }

    @Override
    public String getStateId() {
        return "HOUSING_CATEGORY_SELECTION";
    }

    @Override
    protected HandlerResponse processValidInput(String input, UserSession session) {
        log.debug("HousingCategoryHandler: обработка ввода '{}'", input);

        // 1. Проверяем валидность ввода
        if (!HousingCategoryButton.isHousingCategory(input)) {
            log.warn("Ввод '{}' не является валидной подкатегорией жилья", input);
            return HandlerResponse.stay(
                    "Пожалуйста, выберите подкатегорию жилья из списка",
                    getStateId()
            );
        }

        // 2. Сохраняем подкатегорию в сессию
        session.setHousingCategory(input);
        log.info("Выбрана и сохранена подкатегория жилья: {}", input);

        // 3. Делегируем процессору стандартную обработку
        return subcategoryProcessor.process(input, session, getStateId());
    }
}