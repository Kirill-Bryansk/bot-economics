package ru.boteconomics.bot.core.state.handlers.impl.child;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.buttons.ChildButton;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.core.state.handlers.base.BaseStateHandler;
import ru.boteconomics.bot.core.state.handlers.processors.ChildProcessor;

/**
 * Обработчик выбора ребенка.
 * Наследует напрямую от BaseStateHandler для устранения избыточных уровней абстракции.
 */
@Slf4j
@Component
public class ChildSelectionHandler extends BaseStateHandler {

    private final ChildProcessor childProcessor;

    public ChildSelectionHandler(ChildProcessor childProcessor) {
        this.childProcessor = childProcessor;
        log.info("Создан ChildSelectionHandler с прямой зависимостью от BaseStateHandler");
    }

    @Override
    public String getStateId() {
        return "CHILD_SELECTION";
    }

    @Override
    protected HandlerResponse processValidInput(String input, UserSession session) {
        log.debug("ChildSelectionHandler: обработка ввода '{}'", input);

        // 1. Проверяем валидность ввода
        if (!ChildButton.isChildButton(input)) {
            log.warn("Ввод '{}' не является валидным выбором ребенка", input);
            return HandlerResponse.stay(
                    "Пожалуйста, выберите ребенка из списка",
                    getStateId()
            );
        }

        // 2. Сохраняем ребенка в сессию
        String childName = ChildButton.getChildName(input);
        session.setChildName(childName);
        log.info("Выбран и сохранен ребенок: {}", childName);

        // 3. Определяем параметры для перехода
        String nextState = "CHILD_CATEGORY_SELECTION";
        String selectionMessage = "Вы выбрали: " + input + "\nТеперь выберите категорию:";

        // 4. Делегируем процессору
        return childProcessor.process(input, session, nextState, selectionMessage);
    }

    @Override
    protected HandlerResponse handleBackAction(UserSession session) {
        log.debug("Действие 'Назад' в ChildSelectionHandler");
        session.resetForCategorySelection();
        return HandlerResponse.next(
                "Возврат к выбору категории",
                "CATEGORY_SELECTION"
        );
    }
}