package ru.boteconomics.bot.core.state.handlers.impl.subcategory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.buttons.TransportCategoryButton;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.core.state.handlers.base.BaseStateHandler;
import ru.boteconomics.bot.core.state.handlers.processors.SubcategoryProcessor;

/**
 * Обработчик для подкатегорий "Транспорт".
 * Наследует напрямую от BaseStateHandler для устранения избыточных уровней абстракции.
 */
@Slf4j
@Component
public class TransportCategoryHandler extends BaseStateHandler {

    private final SubcategoryProcessor subcategoryProcessor;

    public TransportCategoryHandler(SubcategoryProcessor subcategoryProcessor) {
        this.subcategoryProcessor = subcategoryProcessor;
        log.info("Создан TransportCategoryHandler с прямой зависимостью от BaseStateHandler");
    }

    @Override
    public String getStateId() {
        return "TRANSPORT_CATEGORY_SELECTION";
    }

    @Override
    protected HandlerResponse processValidInput(String input, UserSession session) {
        log.debug("TransportCategoryHandler: обработка ввода '{}'", input);

        // 1. Проверяем валидность ввода
        if (!TransportCategoryButton.isTransportCategory(input)) {
            log.warn("Ввод '{}' не является валидной подкатегорией транспорта", input);
            return HandlerResponse.stay(
                    "Пожалуйста, выберите подкатегорию транспорта из списка",
                    getStateId()
            );
        }

        // 2. Сохраняем подкатегорию в сессию
        session.setTransportCategory(input);
        log.info("Выбрана и сохранена подкатегория транспорта: {}", input);

        // 3. Делегируем процессору стандартную обработку
        return subcategoryProcessor.process(input, session, getStateId());
    }
}