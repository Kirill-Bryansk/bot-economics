package ru.boteconomics.bot.core.state.handlers.impl.child;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.buttons.ChildCategoryButton;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.core.state.handlers.base.BaseStateHandler;
import ru.boteconomics.bot.core.state.handlers.processors.ChildProcessor;

/**
 * Обработчик выбора категории для ребенка.
 * Наследует напрямую от BaseStateHandler для устранения избыточных уровней абстракции.
 */
@Slf4j
@Component
public class ChildCategoryHandler extends BaseStateHandler {

    private final ChildProcessor childProcessor;

    public ChildCategoryHandler(ChildProcessor childProcessor) {
        this.childProcessor = childProcessor;
        log.info("Создан ChildCategoryHandler с прямой зависимостью от BaseStateHandler");
    }

    @Override
    public String getStateId() {
        return "CHILD_CATEGORY_SELECTION";
    }

    @Override
    protected HandlerResponse processValidInput(String input, UserSession session) {
        log.debug("ChildCategoryHandler: обработка ввода '{}'", input);

        // 1. Проверяем валидность ввода
        if (!ChildCategoryButton.isChildCategory(input)) {
            log.warn("Ввод '{}' не является валидной категорией ребенка", input);
            return HandlerResponse.stay(
                    "Пожалуйста, выберите категорию ребенка из списка",
                    getStateId()
            );
        }

        // 2. Сохраняем категорию ребенка в сессию
        session.setChildCategory(input);
        log.info("Выбрана и сохранена категория ребенка: {}", input);

        // 3. Определяем параметры для перехода
        String nextState = "AMOUNT_INPUT";
        String selectionMessage = "Вы выбрали: " + input + "\nТеперь введите сумму:";

        // 4. Делегируем процессору
        return childProcessor.process(input, session, nextState, selectionMessage);
    }

    @Override
    protected HandlerResponse handleBackAction(UserSession session) {
        log.debug("Действие 'Назад' в ChildCategoryHandler");
        session.resetForChildSelection();
        return HandlerResponse.next(
                "Возврат к выбору ребенка",
                "CHILD_SELECTION"
        );
    }
}