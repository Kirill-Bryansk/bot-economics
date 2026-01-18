package ru.boteconomics.bot.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.boteconomics.bot.core.response.BotResponse;
import ru.boteconomics.bot.core.session.UserSession;
import ru.boteconomics.bot.core.session.UserSessionManager;
import ru.boteconomics.bot.core.state.ExpenseStateMachine;

@Slf4j
@Component
public class UpdateProcessor {

    private final ExpenseStateMachine stateMachine;
    private final UserSessionManager sessionManager;
    private final MessageSender messageSender;

    public UpdateProcessor(ExpenseStateMachine stateMachine,
                           UserSessionManager sessionManager,
                           MessageSender messageSender) {
        this.stateMachine = stateMachine;
        this.sessionManager = sessionManager;
        this.messageSender = messageSender;
        log.info("UpdateProcessor создан");
    }

    public void process(Update update) {
        Long chatId = extractChatId(update);
        if (chatId == null) {
            log.warn("Не могу получить chatId из update");
            return;
        }

        String userText = extractText(update);
        if (userText == null) {
            log.debug("Нет текста в сообщении, отправляем приветствие");
            sendWelcomeMessage(chatId);
            return;
        }

        log.debug("Обработка: chatId={}, text='{}'", chatId, userText);

        // Получаем или создаем сессию пользователя
        UserSession session = sessionManager.getOrCreateSession(chatId);

        // Обрабатываем через State Machine
        BotResponse botResponse = stateMachine.process(userText, session, chatId);

        // Отправляем ответ пользователю
        if (botResponse != null) {
            sendResponse(chatId, botResponse);
        } else {
            log.error("StateMachine вернул null для chatId={}", chatId);
            sendErrorMessage(chatId);
        }
    }

    private void sendWelcomeMessage(Long chatId) {
        // Создаем сессию при приветственном сообщении
        UserSession session = sessionManager.getOrCreateSession(chatId);
        session.setCurrentStateId("MAIN_MENU");

        // Получаем клавиатуру для главного меню
        var keyboardManager = new ru.boteconomics.bot.core.replykeyboard.ReplyKeyboardManager();
        var keyboard = keyboardManager.getKeyboardForState("MAIN_MENU", session);

        BotResponse response = BotResponse.create(
                "👋 Добро пожаловать в бот для учета расходов!\n\n" +
                "Используйте кнопки меню для навигации.",
                keyboard,
                "MAIN_MENU"
        );

        sendResponse(chatId, response);
    }

    private void sendResponse(Long chatId, BotResponse response) {
        log.debug("Отправка ответа chatId={}: {}", chatId,
                response.getMessage().length() > 50 ?
                        response.getMessage().substring(0, 50) + "..." :
                        response.getMessage());

        messageSender.send(chatId, response.getMessage(), response.getKeyboard());

        // Для отладки
        sessionManager.printSession(chatId);
    }

    private void sendErrorMessage(Long chatId) {
        messageSender.send(
                chatId,
                "❌ Произошла ошибка при обработке запроса. Попробуйте снова.",
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