package ru.boteconomics.bot.core.state.handlers.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Сервис для обработки детских категорий.
 * Содержит общую логику для всех обработчиков, связанных с детьми.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChildProcessor {

    /**
     * Обработка выбора (общая логика для всех детских обработчиков)
     */
    public HandlerResponse process(
            String input,
            UserSession session,
            String stateId,
            Predicate<String> validator,
            Consumer<UserSession> saver,
            String description,
            String nextState,
            String selectionMessage) {

        // Проверяем валидность ввода
        if (!validator.test(input)) {
            log.warn("Невалидный ввод для {}: {}", description, input);
            return HandlerResponse.stay(
                    "Пожалуйста, выберите " + description + " из списка",
                    stateId
            );
        }

        // Сохраняем в сессию
        saver.accept(session);

        // Логируем
        log.info("Выбрана {}: {}", description, input);

        // Возвращаем ответ с переходом в следующее состояние
        return HandlerResponse.next(selectionMessage, nextState);
    }
}