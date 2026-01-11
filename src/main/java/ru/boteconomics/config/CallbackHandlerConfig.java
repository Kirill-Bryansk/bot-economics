package ru.boteconomics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.boteconomics.bot.handlers.callback.*;

import java.util.List;

@Configuration
public class CallbackHandlerConfig {

    @Bean
    public List<BaseCallbackHandler> callbackHandlers(
            HistoryCallbackHandler historyHandler,
            TransactionCallbackHandler transactionHandler,
            CategoryCallbackHandler categoryHandler,
            SystemCallbackHandler systemHandler) {
        return List.of(
                historyHandler,
                transactionHandler,
                categoryHandler,
                systemHandler
        );
    }
}