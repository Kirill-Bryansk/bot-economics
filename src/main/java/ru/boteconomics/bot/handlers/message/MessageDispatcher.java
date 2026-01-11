package ru.boteconomics.bot.handlers.message;

import org.springframework.stereotype.Component;
import ru.boteconomics.bot.handlers.HandlerResult;
import ru.boteconomics.bot.keyboard.MainKeyboardFactory;

import java.util.List;

@Component
public class MessageDispatcher {

    private final List<BaseMessageHandler> handlers;
    private final MainKeyboardFactory mainKeyboardFactory;

    public MessageDispatcher(List<BaseMessageHandler> handlers,
                             MainKeyboardFactory mainKeyboardFactory) {
        this.handlers = handlers;
        this.mainKeyboardFactory = mainKeyboardFactory;
    }

    public HandlerResult dispatch(Long chatId, String text) {
        for (BaseMessageHandler handler : handlers) {
            if (handler.canHandle(chatId, text)) {
                return handler.handle(chatId, text);
            }
        }

        // Fallback - если ни один обработчик не подошел
        return new HandlerResult(
                "Неизвестная команда. Используйте меню ниже 👇",
                mainKeyboardFactory.createMainMenu()
        );
    }
}