package ru.boteconomics.bot.handlers.callback;

import org.springframework.stereotype.Component;
import ru.boteconomics.bot.handlers.HandlerResult;

import java.util.List;

@Component
public class CallbackDispatcher {

    private final List<BaseCallbackHandler> handlers;

    public CallbackDispatcher(List<BaseCallbackHandler> handlers) {
        this.handlers = handlers;
    }

    public HandlerResult dispatch(Long chatId, String callbackData) {
        for (BaseCallbackHandler handler : handlers) {
            if (handler.canHandle(callbackData)) {
                return handler.handle(chatId, callbackData);
            }
        }

        // Если ни один обработчик не подошел
        return new HandlerResult(
                "❌ Неизвестная операция",
                null // Можно передать mainKeyboardFactory.createMainMenu()
        );
    }
}