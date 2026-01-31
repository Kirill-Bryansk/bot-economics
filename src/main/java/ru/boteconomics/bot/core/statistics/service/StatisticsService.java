package ru.boteconomics.bot.core.statistics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.boteconomics.bot.core.statistics.enums.PeriodType;
import ru.boteconomics.bot.core.statistics.model.Period;
import ru.boteconomics.bot.core.statistics.model.StatsResult;

import java.math.BigDecimal;

/**
 * Основной сервис для расчета статистики.
 * Содержит бизнес-логику агрегации расходов по периодам.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final TimeService timeService;

    // TODO: Заглушка - позже заменить на реальный ExpenseRepository
    // private final ExpenseRepository expenseRepository;

    /**
     * Рассчитать статистику за указанный период для пользователя.
     */
    public StatsResult calculateStatistics(Long userId, Period period) {
        log.info("Начало расчета статистики для userId={}, период: {}", userId, period);

        if (!period.isValid()) {
            log.warn("Некорректный период: {}", period);
            throw new IllegalArgumentException("Некорректный период");
        }

        StatsResult result = new StatsResult();

        // TODO: Заменить заглушку на реальный запрос к БД
        // List<Expense> expenses = expenseRepository.findByUserIdAndDateBetween(
        //     userId, period.getStartDate(), period.getEndDate());

        // Заглушка с тестовыми данными
        simulateExpenses(result);

        log.info("Статистика рассчитана: {} операций, {} руб",
                result.getOperationsCount(), result.getTotalAmount());

        return result;
    }

    /**
     * Рассчитать статистику за текущую неделю.
     */
    public StatsResult calculateCurrentWeek(Long userId) {
        log.info("Расчет статистики за текущую неделю для userId={}", userId);
        Period period = PeriodType.CURRENT_WEEK.calculateForToday();
        return calculateStatistics(userId, period);
    }

    /**
     * Рассчитать статистику за текущий месяц.
     */
    public StatsResult calculateCurrentMonth(Long userId) {
        log.info("Расчет статистики за текущий месяц для userId={}", userId);
        Period period = PeriodType.CURRENT_MONTH.calculateForToday();
        return calculateStatistics(userId, period);
    }

    /**
     * Заглушка для тестирования - симулирует расходы.
     */
    private void simulateExpenses(StatsResult result) {
        log.debug("Использование заглушки с тестовыми данными");

        // Тестовые данные
        result.addToCategory("🏠 Жилье", new BigDecimal("15000.00"));
        result.addToCategory("🍔 Еда", new BigDecimal("8000.00"));
        result.addToCategory("🚗 Транспорт", new BigDecimal("5000.00"));
        result.addToCategory("🏥 Здоровье", new BigDecimal("3000.00"));
        result.addToCategory("👕 Личные расходы", new BigDecimal("2000.00"));

        log.debug("Добавлено тестовых данных: {} категорий", result.getCategoriesCount());
    }

    /**
     * Проверить, есть ли данные за период.
     */
    public boolean hasDataForPeriod(Long userId, Period period) {
        log.debug("Проверка наличия данных для userId={}, период: {}", userId, period);

        // TODO: Реализовать проверку через expenseRepository
        // return expenseRepository.existsByUserIdAndDateBetween(
        //     userId, period.getStartDate(), period.getEndDate());

        // Заглушка - всегда есть данные
        boolean hasData = true;
        log.debug("Заглушка: данные для периода {}: {}", period, hasData);
        return hasData;
    }
}