
        package ru.boteconomics.bot.core.state;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.error.handler.GlobalErrorHandler;
import ru.boteconomics.bot.core.error.handler.InputErrorHandler;
import ru.boteconomics.bot.core.replykeyboard.ReplyKeyboardManager;
import ru.boteconomics.bot.core.response.BotResponse;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.session.UserSession;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ExpenseStateMachine {

    private final Map<String, State> states = new HashMap<>();
    private final InputErrorHandler inputErrorHandler;
    private final GlobalErrorHandler globalErrorHandler;
    private final ReplyKeyboardManager keyboardManager;

    public ExpenseStateMachine(InputErrorHandler inputErrorHandler,
                               GlobalErrorHandler globalErrorHandler,
                               ReplyKeyboardManager keyboardManager) {
        this.inputErrorHandler = inputErrorHandler;
        this.globalErrorHandler = globalErrorHandler;
        this.keyboardManager = keyboardManager;

        log.info("ExpenseStateMachine инициализирован");
    }

    /**
     * Регистрация состояния в машине
     */
    public void registerState(State state) {
        states.put(state.getStateId(), state);
        log.debug("Зарегистрировано состояние: {}", state.getStateId());
    }

    /**
     * Основной метод обработки - возвращает BotResponse (готовый к отправке)
     */
    public BotResponse process(String input, UserSession session, Long userId) {
        log.debug("Обработка ввода: '{}', текущее состояние: {}",
                input, session.getCurrentStateId());

        try {
            // 1. Получаем текущее состояние
            State currentState = states.get(session.getCurrentStateId());
            if (currentState == null) {
                log.warn("Состояние не найдено: {}, используем MAIN_MENU", session.getCurrentStateId());
                currentState = states.get("MAIN_MENU");
                if (currentState == null) {
                    return createErrorResponse("Системная ошибка: состояние не найдено", session);
                }
            }

            // 2. Обрабатываем ввод через Handler (он сам проверит валидность)
            HandlerResponse handlerResponse = currentState.handle(input, session);
            if (handlerResponse == null) {
                throw new IllegalStateException(
                        String.format("Handler %s вернул null", currentState.getClass().getSimpleName())
                );
            }

            log.debug("HandlerResponse: {}", handlerResponse);

            // 3. Обновляем состояние в сессии
            if (handlerResponse.hasNextState()) {
                String oldState = session.getCurrentStateId();
                session.setCurrentStateId(handlerResponse.getNextStateId());
                log.debug("Переход: {} → {}", oldState, handlerResponse.getNextStateId());
            }

            // 4. Создаём BotResponse с клавиатурой
            return createBotResponse(handlerResponse, session);

        } catch (Exception e) {
            log.error("Ошибка в StateMachine: {}", e.getMessage(), e);
            return handleException(e, session);
        }
    }

    /**
     * Создать BotResponse из HandlerResponse с добавлением клавиатуры
     */
    private BotResponse createBotResponse(HandlerResponse handlerResponse, UserSession session) {
        // Определяем, для какого состояния нужна клавиатура
        String stateForKeyboard = handlerResponse.hasNextState()
                ? handlerResponse.getNextStateId()
                : session.getCurrentStateId();

        // Получаем клавиатуру для этого состояния
        var keyboard = keyboardManager.getKeyboardForState(stateForKeyboard, session);

        // Создаём BotResponse
        return BotResponse.fromHandler(handlerResponse, keyboard);
    }

    /**
     * Обработка исключений через GlobalErrorHandler
     */
    private BotResponse handleException(Exception e, UserSession session) {
        // GlobalErrorHandler должен быть обновлён для работы с BotResponse
        // Пока заглушка:
        session.resetAll();
        return BotResponse.withoutKeyboard(
                "⚠️ Произошла ошибка. Возврат в главное меню.",
                "MAIN_MENU"
        );
    }

    /**
     * Создать ответ об ошибке
     */
    private BotResponse createErrorResponse(String message, UserSession session) {
        session.resetAll();
        return BotResponse.withoutKeyboard("❌ " + message, "MAIN_MENU");
    }

    /**
     * Для отладки: список зарегистрированных состояний
     */
    public void printRegisteredStates() {
        log.info("Зарегистрированные состояния ({}): {}",
                states.size(), String.join(", ", states.keySet()));
    }

    /**
     * Получить количество зарегистрированных состояний
     */
    public int getStatesCount() {
        return states.size();
    }
}
