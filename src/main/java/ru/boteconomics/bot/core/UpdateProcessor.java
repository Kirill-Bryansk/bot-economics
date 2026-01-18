package ru.boteconomics.bot.core;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.core.session.UserSessionManager;
import ru.boteconomics.bot.core.state.handler.ExpenseStateMachine;
import ru.boteconomics.bot.handlers.HandlerResult;

@Component
public class UpdateProcessor {

    private final ExpenseStateMachine stateMachine;
    private final UserSessionManager sessionManager;

    public UpdateProcessor(ExpenseStateMachine stateMachine,
                           UserSessionManager sessionManager) {
        this.stateMachine = stateMachine;
        this.sessionManager = sessionManager;
        System.out.println("[UPDATE PROCESSOR] Создан");
    }

    public HandlerResult process(Update update) {
        Long chatId = extractChatId(update);
        if (chatId == null) {
            System.out.println("[UPDATE PROCESSOR] Не могу получить chatId");
            return null;
        }

        String userText = extractText(update);
        if (userText == null) {
            System.out.println("[UPDATE PROCESSOR] Нет текста в сообщении");
            return createWelcomeMessage();
        }

        System.out.println("[UPDATE PROCESSOR] chatId=" + chatId + ", text='" + userText + "'");

        // Получаем сессию пользователя
        UserSession session = sessionManager.getSession();
        sessionManager.printSession();

        // Передаем в State Machine
        HandlerResult result = stateMachine.process(userText, session);

        // Логируем результат
        if (result != null) {
            System.out.println("[UPDATE PROCESSOR] Результат: состояние=" +
                               result.getNextStateId() + ", сообщение=" +
                               (result.getMessage().length() > 30 ?
                                       result.getMessage().substring(0, 30) + "..." :
                                       result.getMessage()));
        }

        return result;
    }

    private HandlerResult createWelcomeMessage() {
        return HandlerResult.stay(
                "👋 Добро пожаловать! Используйте кнопки меню для навигации.",
                null
        );
    }

    private String extractText(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            return update.getMessage().getText();
        }
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getData();
        }
        return null;
    }

    public Long extractChatId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getChatId();
        }
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getChatId();
        }
        return null;
    }
}