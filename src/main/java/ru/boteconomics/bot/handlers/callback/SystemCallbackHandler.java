package ru.boteconomics.bot.handlers.callback;

import org.springframework.stereotype.Component;
import ru.boteconomics.bot.handlers.HandlerResult;
import ru.boteconomics.bot.keyboard.MainKeyboardFactory;
import ru.boteconomics.bot.service.HistoryService;
import ru.boteconomics.bot.state.UserStateService;

@Component
public class SystemCallbackHandler extends BaseCallbackHandler {

    private final UserStateService userStateService;

    public SystemCallbackHandler(UserStateService userStateService,
                                 HistoryService historyService,
                                 MainKeyboardFactory mainKeyboardFactory) {
        super(historyService, mainKeyboardFactory);
        this.userStateService = userStateService;
    }

    @Override
    public boolean canHandle(String callbackData) {
        return callbackData.equals("CANCEL") ||
               callbackData.equals("NO_ACTION");
    }

    @Override
    public HandlerResult handle(Long chatId, String callbackData) {
        if (callbackData.equals("CANCEL")) {
            return handleCancel(chatId);
        } else if (callbackData.equals("NO_ACTION")) {
            return handleNoAction();
        }

        return error("Неизвестная системная команда");
    }

    private HandlerResult handleCancel(Long chatId) {
        userStateService.reset(chatId);
        return success("Операция отменена");
    }

    private HandlerResult handleNoAction() {
        // Пустой ответ для неактивных кнопок
        return new HandlerResult("", null);
    }
}