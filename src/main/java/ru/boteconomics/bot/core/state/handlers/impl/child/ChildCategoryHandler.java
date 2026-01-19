package ru.boteconomics.bot.core.state.handlers.impl.child;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.buttons.ChildCategoryButton;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.core.state.handlers.base.BaseChildHandler;
import ru.boteconomics.bot.core.state.handlers.processors.ChildProcessor;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Переработанный обработчик выбора категории для ребенка.
 * Использует BaseChildHandler для устранения дублирования кода.
 */
@Slf4j
@Component
public class ChildCategoryHandler extends BaseChildHandler {

    public ChildCategoryHandler(ChildProcessor childProcessor) {
        super(childProcessor);
        log.info("Создан RefactoredChildCategoryHandler");
    }

    @Override
    public String getStateId() {
        return "CHILD_CATEGORY_SELECTION";
    }

    @Override
    protected Predicate<String> getValidator() {
        return ChildCategoryButton::isChildCategory;
    }

    @Override
    protected Consumer<UserSession> getSaver(String input) {
        return session -> {
            session.setChildCategory(input);
            log.debug("Сохранена категория ребенка: {}", input);
        };
    }

    @Override
    protected String getDescription() {
        return "категория ребенка";
    }

    @Override
    protected String getNextState() {
        return "AMOUNT_INPUT";
    }

    @Override
    protected String getSelectionMessage(String input) {
        return "Вы выбрали: " + input + "\nТеперь введите сумму:";
    }

    @Override
    protected ru.boteconomics.bot.core.response.HandlerResponse handleBackAction(
            ru.boteconomics.bot.core.session.UserSession session) {
        log.debug("Действие 'Назад' в ChildCategoryHandler");
        session.resetForChildSelection();
        return ru.boteconomics.bot.core.response.HandlerResponse.next(
                "Возврат к выбору ребенка",
                "CHILD_SELECTION"
        );
    }
}