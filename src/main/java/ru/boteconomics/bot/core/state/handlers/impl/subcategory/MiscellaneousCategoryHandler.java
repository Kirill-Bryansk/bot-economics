package ru.boteconomics.bot.core.state.handlers.impl.subcategory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.buttons.MiscellaneousCategoryButton;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.core.state.handlers.base.BaseStateHandler;
import ru.boteconomics.bot.core.state.handlers.processors.SubcategoryProcessor;

/**
 * Обработчик для подкатегорий "Разное".
 * Наследует напрямую от BaseStateHandler для устранения избыточных уровней абстракции.
 */
@Slf4j
@Component
public class MiscellaneousCategoryHandler extends BaseStateHandler {

    private final SubcategoryProcessor subcategoryProcessor;

    public MiscellaneousCategoryHandler(SubcategoryProcessor subcategoryProcessor) {
        this.subcategoryProcessor = subcategoryProcessor;
        log.info("Создан MiscellaneousCategoryHandler с прямой зависимостью от BaseStateHandler");
    }

    @Override
    public String getStateId() {
        return "MISCELLANEOUS_CATEGORY_SELECTION";
    }

    @Override
    protected HandlerResponse processValidInput(String input, UserSession session) {
        log.debug("MiscellaneousCategoryHandler: обработка ввода '{}'", input);

        // 1. Проверяем валидность ввода
        if (!MiscellaneousCategoryButton.isMiscellaneousCategory(input)) {
            log.warn("Ввод '{}' не является валидной подкатегорией 'Разное'", input);
            return HandlerResponse.stay(
                    "Пожалуйста, выберите подкатегорию 'Разное' из списка",
                    getStateId()
            );
        }

        // 2. Сохраняем подкатегорию в сессию
        session.setMiscellaneousCategory(input);
        log.info("Выбрана и сохранена подкатегория 'Разное': {}", input);

        // 3. Делегируем процессору стандартную обработку
        return subcategoryProcessor.process(input, session, getStateId());
    }

    @Override
    protected HandlerResponse handleBackAction(UserSession session) {
        log.debug("Действие 'Назад' в MiscellaneousCategoryHandler");
        return super.handleBackAction(session);
    }
}