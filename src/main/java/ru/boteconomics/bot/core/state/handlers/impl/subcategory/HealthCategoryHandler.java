package ru.boteconomics.bot.core.state.handlers.impl.subcategory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.core.state.handlers.base.BaseStateHandler;
import ru.boteconomics.bot.core.state.handlers.processors.SubcategoryProcessor;

/**
 * Обработчик подкатегории здоровья.
 */


@Slf4j
@Component
public class HealthCategoryHandler extends BaseStateHandler {

    private final SubcategoryProcessor subcategoryProcessor;

    public HealthCategoryHandler(SubcategoryProcessor subcategoryProcessor) {
        this.subcategoryProcessor = subcategoryProcessor;
        log.info("Создан HealthCategoryHandler с прямой зависимостью от BaseStateHandler");
    }

    @Override
    public String getStateId() {
        return "HEALTH_CATEGORY_SELECTION";
    }

    @Override
    protected HandlerResponse processValidInput(String input, UserSession session) {
        log.debug("HealthCategoryHandler: обработка валидного ввода '{}'", input);

        // Устанавливаем подкатегорию здоровья в сессии
        if (ru.boteconomics.bot.core.buttons.HealthCategoryButton.HOSPITAL.equals(input)) {
            session.setHealthCategory("🏥 Больница");
            log.info("Выбрана и сохранена подкатегория здоровья: {}", input);
        } else if (ru.boteconomics.bot.core.buttons.HealthCategoryButton.PHARMACY.equals(input)) {
            session.setHealthCategory("💊 Аптека");
            log.info("Выбрана и сохранена подкатегория здоровья: {}", input);
        }

        return subcategoryProcessor.process(input, session, "HEALTH_CATEGORY_SELECTION");
    }
}
