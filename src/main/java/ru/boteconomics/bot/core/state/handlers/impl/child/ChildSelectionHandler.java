package ru.boteconomics.bot.core.state.handlers.impl.child;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.buttons.ChildButton;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.core.state.handlers.base.BaseChildHandler;
import ru.boteconomics.bot.core.state.handlers.processors.ChildProcessor;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Переработанный обработчик выбора ребенка.
 * Использует BaseChildHandler для устранения дублирования кода.
 */
@Slf4j
@Component
public class ChildSelectionHandler extends BaseChildHandler {

    public ChildSelectionHandler(ChildProcessor childProcessor) {
        super(childProcessor);
        log.info("Создан RefactoredChildSelectionHandler");
    }

    @Override
    public String getStateId() {
        return "CHILD_SELECTION";
    }

    @Override
    protected Predicate<String> getValidator() {
        return ChildButton::isChildButton;
    }

    @Override
    protected Consumer<UserSession> getSaver(String input) {
        return session -> {
            String childName = ChildButton.getChildName(input);
            session.setChildName(childName);
            log.debug("Сохранен ребенок: {}", childName);
        };
    }

    @Override
    protected String getDescription() {
        return "ребенок";
    }

    @Override
    protected String getNextState() {
        return "CHILD_CATEGORY_SELECTION";
    }

    @Override
    protected String getSelectionMessage(String input) {
        return "Вы выбрали: " + input + "\nТеперь выберите категорию:";
    }

    @Override
    protected ru.boteconomics.bot.core.response.HandlerResponse handleBackAction(
            ru.boteconomics.bot.core.session.UserSession session) {
        log.debug("Действие 'Назад' в ChildSelectionHandler");
        session.resetForCategorySelection();
        return ru.boteconomics.bot.core.response.HandlerResponse.next(
                "Возврат к выбору категории",
                "CATEGORY_SELECTION"
        );
    }
}