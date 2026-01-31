package ru.boteconomics.bot.core.statistics.model;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Результат расчета статистики.
 * Содержит суммы по категориям и общие итоги.
 */
@Slf4j
@Getter
@ToString
public class StatsResult {

    // Суммы по категориям (категория → сумма)
    private final Map<String, BigDecimal> categoryTotals = new HashMap<>();

    // Общая сумма всех расходов
    private BigDecimal totalAmount = BigDecimal.ZERO;

    // Количество операций
    private int operationsCount = 0;

    /**
     * Добавить сумму в категорию.
     */
    public void addToCategory(String category, BigDecimal amount) {
        log.debug("Добавление в категорию '{}': {} руб", category, amount);

        categoryTotals.merge(category, amount, BigDecimal::add);
        totalAmount = totalAmount.add(amount);
        operationsCount++;

        log.debug("Категория '{}' теперь: {} руб",
                category, categoryTotals.get(category));
    }

    /**
     * Проверить, есть ли данные в статистике.
     */
    public boolean isEmpty() {
        boolean empty = operationsCount == 0;
        log.debug("Проверка пустоты статистики: {}", empty);
        return empty;
    }

    /**
     * Получить количество категорий.
     */
    public int getCategoriesCount() {
        int count = categoryTotals.size();
        log.debug("Количество категорий: {}", count);
        return count;
    }

    /**
     * Получить среднюю сумму за операцию.
     */
    public BigDecimal getAveragePerOperation() {
        if (operationsCount == 0) {
            log.debug("Нет операций для расчета среднего");
            return BigDecimal.ZERO;
        }

        BigDecimal average = totalAmount.divide(
                BigDecimal.valueOf(operationsCount), 2, BigDecimal.ROUND_HALF_UP);
        log.debug("Среднее за операцию: {} руб", average);
        return average;
    }

    /**
     * Получить долю категории в процентах.
     */
    public BigDecimal getCategoryPercentage(String category) {
        if (totalAmount.compareTo(BigDecimal.ZERO) == 0) {
            log.debug("Общая сумма 0, процент категории '{}': 0", category);
            return BigDecimal.ZERO;
        }

        BigDecimal categoryAmount = categoryTotals.getOrDefault(category, BigDecimal.ZERO);
        BigDecimal percentage = categoryAmount
                .multiply(BigDecimal.valueOf(100))
                .divide(totalAmount, 1, BigDecimal.ROUND_HALF_UP);

        log.debug("Доля категории '{}': {}%", category, percentage);
        return percentage;
    }

    /**
     * Получить отсортированные категории по убыванию суммы.
     */
    public Map<String, BigDecimal> getSortedCategories() {
        log.debug("Сортировка категорий по убыванию суммы");

        Map<String, BigDecimal> sorted = new HashMap<>();
        categoryTotals.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .forEach(entry -> sorted.put(entry.getKey(), entry.getValue()));

        return sorted;
    }
}