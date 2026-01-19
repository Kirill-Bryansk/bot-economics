package ru.boteconomics.bot.core.state.handlers.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.error.exception.AmountParseException;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;

import java.math.BigDecimal;
import java.util.function.Function;

/**
 * Сервис для обработки ввода суммы.
 * Содержит сложную логику валидации и обработки "Назад".
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AmountProcessor {

    /**
     * Обработка ввода суммы
     */
    public HandlerResponse process(
            String input,
            UserSession session,
            String stateId,
            Function<String, BigDecimal> amountParser) {

        try {
            // Парсим сумму
            BigDecimal amount = amountParser.apply(input);

            // Сохраняем сумму в сессию
            session.setAmount(amount);
            log.info("Сумма сохранена: {}", amount);

            // Переходим к подтверждению
            log.info("Переход: AMOUNT_INPUT → CONFIRMATION");
            return HandlerResponse.next(
                    "✅ Сумма сохранена: " + amount + " ₽\n\nПереход к подтверждению...",
                    "CONFIRMATION"
            );

        } catch (AmountParseException e) {
            log.warn("Ошибка валидации суммы '{}': {}", input, e.getMessage());
            return HandlerResponse.stay(
                    "❌ " + e.getUserMessage(),
                    stateId
            );
        }
    }

    /**
     * Обработка действия "Назад" для AmountInputHandler
     */
    public HandlerResponse handleBackAction(UserSession session) {
        log.debug("Действие 'Назад' из состояния AMOUNT_INPUT");

        // Очищаем сумму
        session.resetAmount();

        // Определяем куда возвращаться
        if (session.isChildCategory()) {
            if (session.getChildName() != null && session.getChildCategory() != null) {
                session.resetForChildCategorySelection();
                return HandlerResponse.next(
                        "Выберите подкатегорию:",
                        "CHILD_CATEGORY_SELECTION"
                );
            } else if (session.getChildName() != null) {
                session.resetForChildSelection();
                return HandlerResponse.next(
                        "Выберите ребенка:",
                        "CHILD_SELECTION"
                );
            }
        }
        // Обработка для категории "Жилье"
        else if (session.isHousingCategory()) {
            if (session.getHousingCategory() != null) {
                session.resetForHousingCategorySelection();
                return HandlerResponse.next(
                        "Выберите подкатегорию:",
                        "HOUSING_CATEGORY_SELECTION"
                );
            }
        }
        // Обработка для категории "Транспорт"
        else if (session.isTransportCategory()) {
            if (session.getTransportCategory() != null) {
                session.resetForTransportCategorySelection();
                return HandlerResponse.next(
                        "Выберите подкатегорию:",
                        "TRANSPORT_CATEGORY_SELECTION"
                );
            }
        }
        // Обработка для категории "Продукты"
        else if (session.isProductsCategory()) {
            if (session.getProductsCategory() != null) {
                session.resetForProductsCategorySelection();
                return HandlerResponse.next(
                        "Выберите подкатегорию:",
                        "PRODUCTS_CATEGORY_SELECTION"
                );
            }
        }
        // Обработка для категории "Разное"
        else if (session.isMiscellaneousCategory()) {
            if (session.getMiscellaneousCategory() != null) {
                session.resetForMiscellaneousCategorySelection();
                return HandlerResponse.next(
                        "Выберите подкатегорию:",
                        "MISCELLANEOUS_CATEGORY_SELECTION"
                );
            }
        }

        // Для обычных категорий
        session.resetForCategorySelection();
        return HandlerResponse.next(
                "Выберите категорию:",
                "CATEGORY_SELECTION"
        );
    }
}