package ru.boteconomics.bot.core.state.handlers.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;

/**
 * Сервис для обработки детских категорий.
 * Содержит общую логику для всех обработчиков, связанных с детьми.
 * Теперь принимает только сообщение и следующее состояние - остальная логика в обработчиках.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChildProcessor {

    /**
     * Обработка выбора (общая логика для всех детских обработчиков)
     * Теперь принимает только 4 аргумента - валидация и сохранение вынесены в обработчики
     */
    public HandlerResponse process(
            String input,
            UserSession session,
            String nextState,
            String selectionMessage) {

        log.debug("ChildProcessor: обработка ввода '{}', переход в состояние '{}'",
                input, nextState);

        // Логируем выбор
        log.info("Выбран ребенок/подкатегория: {}", input);

        // Возвращаем ответ с переходом в следующее состояние
        return HandlerResponse.next(selectionMessage, nextState);
    }
}