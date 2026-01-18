package ru.boteconomics.bot.core.state.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.buttons.HousingCategoryButton;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;

/**
 * Обработчик выбора подкатегории для жилья
 * Состояние: HOUSING_CATEGORY_SELECTION
 */
@Slf4j
@Component
public class HousingCategoryHandler extends BaseStateHandler {

    @Override
    public String getStateId() {
        return "HOUSING_CATEGORY_SELECTION";
    }

    @Override
    protected HandlerResponse processValidInput(String input, UserSession session) {
        log.info("Обработка выбора подкатегории жилья: '{}'", input);

        // 1. Проверяем действия (Назад/Отмена)
        HandlerResponse actionResponse = handleActionIfNeeded(input, session);
        if (actionResponse != null) {
            return actionResponse;
        }

        // 2. Обработка выбора подкатегории жилья
        if (HousingCategoryButton.isHousingCategory(input)) {
            session.setHousingCategory(input);
            logSelection("подкатегория жилья", input);

            logTransition(getStateId(), "AMOUNT_INPUT");
            return HandlerResponse.next(
                    "Вы выбрали: " + input + "\nТеперь введите сумму:",
                    "AMOUNT_INPUT"
            );
        }

        // 3. Не должно происходить (валидация должна отсечь на предыдущем этапе)
        log.error("Непредвиденный ввод в HousingCategoryHandler: '{}'", input);
        return HandlerResponse.stay(
                "Пожалуйста, выберите подкатегорию из списка",
                getStateId()
        );
    }
}