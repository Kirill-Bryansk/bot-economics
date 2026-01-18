
        package ru.boteconomics.bot.core.state.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.buttons.ChildButton;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;

/**
 * Обработчик выбора ребенка (заглушка)
 * Состояние: CHILD_SELECTION
 */
@Slf4j
@Component
public class ChildSelectionHandler extends BaseStateHandler {

    @Override
    public String getStateId() {
        return "CHILD_SELECTION";
    }

    @Override
    protected HandlerResponse processValidInput(String input, UserSession session) {
        log.info("Обработка выбора ребенка: '{}'", input);

        // 1. Проверяем действия
        HandlerResponse actionResponse = handleActionIfNeeded(input, session);
        if (actionResponse != null) {
            return actionResponse;
        }

        // 2. Обработка выбора ребенка
        if (ChildButton.isChildButton(input)) {
            String childName = ChildButton.getChildName(input);
            session.setChildName(childName);
            logSelection("ребенок", childName);

            logTransition(getStateId(), "CHILD_CATEGORY_SELECTION");
            return HandlerResponse.next(
                    "Вы выбрали: " + input + "\nТеперь выберите категорию:",
                    "CHILD_CATEGORY_SELECTION"
            );
        }

        // 3. Не должно происходить
        log.error("Непредвиденный ввод в ChildSelectionHandler: '{}'", input);
        return HandlerResponse.stay(
                "Пожалуйста, выберите ребенка из списка",
                getStateId()
        );
    }
}
