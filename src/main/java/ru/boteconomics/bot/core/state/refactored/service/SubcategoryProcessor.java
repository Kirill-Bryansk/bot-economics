package ru.boteconomics.bot.core.state.refactored.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;

import java.util.function.Consumer;
import java.util.function.Predicate;

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
     */
    public HandlerResponse process(
            String input,
            UserSession session,
            String stateId,
            Predicate<String> validator,
            Consumer<UserSession> saver,
            String description) {

        // Проверяем валидность ввода
        if (!validator.test(input)) {
            log.warn("Невалидный ввод для подкатегории: {}", input);
            return HandlerResponse.stay(
                    "Пожалуйста, выберите подкатегорию из списка",
                    stateId
            );
        }

        // Сохраняем в сессию
        saver.accept(session);

        // Логируем
        log.info("Выбрана {}: {}", description, input);

        // Возвращаем стандартный ответ
        return HandlerResponse.next(
                "Вы выбрали: " + input + "\nТеперь введите сумму:",
                "AMOUNT_INPUT"
        );
    }

    // Удаляем метод createValidator, т.к. не используем его
    // public Predicate<String> createValidator(String buttonType) { ... }
}