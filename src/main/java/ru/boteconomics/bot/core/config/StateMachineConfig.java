package ru.boteconomics.bot.core.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import ru.boteconomics.bot.core.state.ExpenseStateMachine;
import ru.boteconomics.bot.core.state.State;
import ru.boteconomics.bot.core.state.handler.HousingCategoryHandler;
import ru.boteconomics.bot.core.state.handler.MiscellaneousCategoryHandler;
import ru.boteconomics.bot.core.state.handler.ProductsCategoryHandler;
import ru.boteconomics.bot.core.state.handler.TransportCategoryHandler;
import ru.boteconomics.bot.core.state.refactored.subcategory.RefactoredHousingCategoryHandler;
import ru.boteconomics.bot.core.state.refactored.subcategory.RefactoredMiscellaneousCategoryHandler;
import ru.boteconomics.bot.core.state.refactored.subcategory.RefactoredProductsCategoryHandler;
import ru.boteconomics.bot.core.state.refactored.subcategory.RefactoredTransportCategoryHandler;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StateMachineConfig {

    private final ExpenseStateMachine stateMachine;
    private final List<State> allStates; // Spring соберет все бины, реализующие State

    // Инжектим новые обработчики по именам бинов
    private final RefactoredMiscellaneousCategoryHandler refactoredMiscellaneousCategoryHandler;
    private final RefactoredHousingCategoryHandler refactoredHousingCategoryHandler;
    private final RefactoredTransportCategoryHandler refactoredTransportCategoryHandler;
    private final RefactoredProductsCategoryHandler refactoredProductsCategoryHandler;

    @EventListener(ContextRefreshedEvent.class)
    public void registerAllStates() {
        log.info("Регистрация состояний в StateMachine...");

        int registered = 0;
        int skipped = 0;

        for (State state : allStates) {
            try {
                String stateId = state.getStateId();

                // Пропускаем старые обработчики, если есть новые
                if (state instanceof MiscellaneousCategoryHandler) {
                    log.info("Пропускаем старый MiscellaneousCategoryHandler, используем переработанный");
                    skipped++;
                    continue;
                }

                if (state instanceof HousingCategoryHandler) {
                    log.info("Пропускаем старый HousingCategoryHandler, используем переработанный");
                    skipped++;
                    continue;
                }

                if (state instanceof TransportCategoryHandler) {
                    log.info("Пропускаем старый TransportCategoryHandler, используем переработанный");
                    skipped++;
                    continue;
                }

                if (state instanceof ProductsCategoryHandler) {
                    log.info("Пропускаем старый ProductsCategoryHandler, используем переработанный");
                    skipped++;
                    continue;
                }

                stateMachine.registerState(state);
                log.debug("Зарегистрировано состояние: {}", stateId);
                registered++;

            } catch (Exception e) {
                log.error("Ошибка регистрации состояния {}: {}",
                        state.getClass().getSimpleName(), e.getMessage());
            }
        }

        // Регистрируем новые переработанные обработчики
        registerRefactoredHandler(refactoredMiscellaneousCategoryHandler);
        registerRefactoredHandler(refactoredHousingCategoryHandler);
        registerRefactoredHandler(refactoredTransportCategoryHandler);
        registerRefactoredHandler(refactoredProductsCategoryHandler);

        log.info("Успешно зарегистрировано {} состояний, пропущено: {}",
                registered, skipped);
        stateMachine.printRegisteredStates();
    }

    private void registerRefactoredHandler(State handler) {
        try {
            stateMachine.registerState(handler);
            log.debug("Зарегистрировано переработанное состояние: {}",
                    handler.getStateId());
        } catch (Exception e) {
            log.error("Ошибка регистрации переработанного обработчика {}: {}",
                    handler.getClass().getSimpleName(), e.getMessage());
        }
    }
}