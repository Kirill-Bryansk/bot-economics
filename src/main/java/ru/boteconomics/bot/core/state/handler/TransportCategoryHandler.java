package ru.boteconomics.bot.core.state.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.buttons.TransportCategoryButton;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;

/**
 * Обработчик выбора подкатегории для транспорта
 * Состояние: TRANSPORT_CATEGORY_SELECTION
 */
@Slf4j
@Component
public class TransportCategoryHandler extends BaseStateHandler {

    @Override
    public String getStateId() {
        return "TRANSPORT_CATEGORY_SELECTION";
    }

    @Override
    protected HandlerResponse processValidInput(String input, UserSession session) {
        log.info("Обработка выбора подкатегории транспорта: '{}'", input);

        // 1. Проверяем действия (Назад/Отмена)
        HandlerResponse actionResponse = handleActionIfNeeded(input, session);
        if (actionResponse != null) {
            return actionResponse;
        }

        // 2. Обработка выбора подкатегории транспорта
        if (TransportCategoryButton.isTransportCategory(input)) {
            session.setTransportCategory(input);
            logSelection("подкатегория транспорта", input);

            logTransition(getStateId(), "AMOUNT_INPUT");
            return HandlerResponse.next(
                    "Вы выбрали: " + input + "\nТеперь введите сумму:",
                    "AMOUNT_INPUT"
            );
        }

        // 3. Не должно происходить (валидация должна отсечь на предыдущем этапе)
        log.error("Непредвиденный ввод в TransportCategoryHandler: '{}'", input);
        return HandlerResponse.stay(
                "Пожалуйста, выберите подкатегорию из списка",
                getStateId()
        );
    }
}