package ru.boteconomics.bot.core.state.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.buttons.CategoryButton;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.core.state.handlers.base.BaseStateHandler;
import ru.boteconomics.bot.core.state.handlers.processors.CategoryProcessor;

/**
 * Обработчик выбора категории.
 * Наследует напрямую от BaseStateHandler для устранения избыточных уровней абстракции.
 * Использует CategoryProcessor для обработки бизнес-логики переходов.
 */
@Slf4j
@Component
public class CategorySelectionHandler extends BaseStateHandler {

    private final CategoryProcessor categoryProcessor;

    public CategorySelectionHandler(CategoryProcessor categoryProcessor) {
        this.categoryProcessor = categoryProcessor;
        log.info("Создан CategorySelectionHandler с прямой зависимостью от BaseStateHandler");
    }

    @Override
    public String getStateId() {
        return "CATEGORY_SELECTION";
    }

    @Override
    protected HandlerResponse processValidInput(String input, UserSession session) {
        log.debug("CategorySelectionHandler: обработка валидного ввода '{}'", input);

        // 1. Проверяем, что выбранная категория действительно является категорией
        if (!CategoryButton.isCategory(input)) {
            log.warn("Ввод '{}' не является валидной категорией", input);
            return HandlerResponse.stay(
                    "Пожалуйста, выберите категорию из предложенных кнопок",
                    getStateId()
            );
        }

        // 2. Сохраняем категорию в сессию
        session.setCategory(input);
        log.info("Выбрана и сохранена категория: {}", input);

        // 3. Делегируем процессору определение следующего состояния
        return categoryProcessor.process(input, session, getStateId());
    }

    @Override
    protected HandlerResponse handleBackAction(UserSession session) {
        log.debug("Действие 'Назад' в CategorySelectionHandler");
        return HandlerResponse.next(
                "Возврат в главное меню",
                "MAIN_MENU"
        );
    }

    @Override
    protected HandlerResponse handleCancelAction(UserSession session) {
        log.debug("Действие 'Отмена' в CategorySelectionHandler");
        session.resetAll();
        return HandlerResponse.next(
                "Операция отменена. Вы в главном меню.",
                "MAIN_MENU"
        );
    }
}