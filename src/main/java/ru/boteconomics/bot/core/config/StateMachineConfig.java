package ru.boteconomics.bot.core.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import ru.boteconomics.bot.core.state.ExpenseStateMachine;
import ru.boteconomics.bot.core.state.State;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StateMachineConfig {

    private final ExpenseStateMachine stateMachine;
    private final List<State> allStates; // Spring соберет все бины, реализующие State

    @EventListener(ContextRefreshedEvent.class)
    public void registerAllStates() {
        log.info("Регистрация состояний в StateMachine...");

        int registered = 0;
        for (State state : allStates) {
            try {
                stateMachine.registerState(state);
                log.debug("Зарегистрировано состояние: {}", state.getStateId());
                registered++;
            } catch (Exception e) {
                log.error("Ошибка регистрации состояния {}: {}",
                        state.getClass().getSimpleName(), e.getMessage());
            }
        }

        log.info("Успешно зарегистрировано {} состояний из {}", registered, allStates.size());
        stateMachine.printRegisteredStates();
    }
}