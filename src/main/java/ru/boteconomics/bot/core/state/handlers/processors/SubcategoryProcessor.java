package ru.boteconomics.bot.core.state.handlers.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;

/**
 * Сервис для обработки подкатегорий.
 * Содержит общую логику для всех обработчиков подкатегорий.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SubcategoryProcessor {

    /**
     * Обработка выбора подкатегории (общая логика для всех подкатегорий)
     * Теперь принимает только 3 аргумента - валидация и сохранение вынесены в обработчики
     */
    public HandlerResponse process(
            String input,
            UserSession session,
            String stateId) {

        log.debug("SubcategoryProcessor: обработка подкатегории '{}'", input);

        // Логируем выбор
        log.info("Выбрана подкатегория: {}", input);

        // Возвращаем стандартный ответ (переход к вводу суммы)
        return HandlerResponse.next(
                "Вы выбрали: " + input + "\nТеперь введите сумму:",
                "AMOUNT_INPUT"
        );
    }
}