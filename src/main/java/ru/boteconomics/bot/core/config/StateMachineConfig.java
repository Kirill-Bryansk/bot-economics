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
        log.info("=================== РЕГИСТРАЦИЯ СОСТОЯНИЙ ===================");
        log.info("Начинаю регистрацию состояний в StateMachine...");
        log.info("Найдено всего {} состояний в контексте Spring", allStates.size());

        int registered = 0;
        int errors = 0;

        for (State state : allStates) {
            String stateClassName = state.getClass().getSimpleName();
            try {
                String stateId = state.getStateId();
                stateMachine.registerState(state);
                log.info("✅ Успешно зарегистрировано состояние: {} [ID: {}]",
                        stateClassName, stateId);
                registered++;

            } catch (Exception e) {
                log.error("❌ Ошибка регистрации состояния {}: {}",
                        stateClassName, e.getMessage(), e);
                errors++;
            }
        }

        log.info("=================== ИТОГ РЕГИСТРАЦИИ ===================");
        log.info("Всего состояний для регистрации: {}", allStates.size());
        log.info("Успешно зарегистрировано: {}", registered);
        log.info("Ошибок при регистрации: {}", errors);

        if (errors == 0) {
            log.info("✅ Все состояния успешно зарегистрированы!");
        } else {
            log.warn("⚠️  Зарегистрировано не все состояния! Есть {} ошибок", errors);
        }

        log.info("Вывод списка зарегистрированных состояний:");
        stateMachine.printRegisteredStates();
        log.info("=================== КОНЕЦ РЕГИСТРАЦИИ ===================");
    }
}