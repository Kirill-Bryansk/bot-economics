
        package ru.boteconomics.bot.core.state.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.buttons.ChildCategoryButton;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;

/**
 * Обработчик выбора категории для ребенка (заглушка)
 * Состояние: CHILD_CATEGORY_SELECTION
 */
@Slf4j
@Component
public class ChildCategoryHandler extends BaseStateHandler {

    @Override
    public String getStateId() {
        return "CHILD_CATEGORY_SELECTION";
    }

    @Override
    protected HandlerResponse processValidInput(String input, UserSession session) {
        log.info("Обработка выбора категории ребенка: '{}'", input);

        // 1. Проверяем действия
        HandlerResponse actionResponse = handleActionIfNeeded(input, session);
        if (actionResponse != null) {
            return actionResponse;
        }

        // 2. Обработка выбора категории ребенка
        if (ChildCategoryButton.isChildCategory(input)) {
            session.setChildCategory(input);
            logSelection("категория ребенка", input);

            logTransition(getStateId(), "AMOUNT_INPUT");
            return HandlerResponse.next(
                    "Вы выбрали: " + input + "\nТеперь введите сумму:",
                    "AMOUNT_INPUT"
            );
        }

        // 3. Не должно происходить
        log.error("Непредвиденный ввод в ChildCategoryHandler: '{}'", input);
        return HandlerResponse.stay(
                "Пожалуйста, выберите категорию из списка",
                getStateId()
        );
    }
}
