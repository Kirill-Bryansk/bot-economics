package ru.boteconomics.bot.core.statistics.enums;

import lombok.extern.slf4j.Slf4j;
import ru.boteconomics.bot.core.statistics.model.Period;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import static java.time.DayOfWeek.MONDAY;

/**
 * Типы периодов для статистики.
 * Каждый период знает как рассчитать свои границы.
 */
@Slf4j
public enum PeriodType {

    CURRENT_WEEK("Текущая неделя") {
        @Override
        public Period calculate(LocalDate baseDate) {
            log.debug("Расчет периода текущей недели для даты: {}", baseDate);
            LocalDate start = baseDate.with(TemporalAdjusters.previousOrSame(MONDAY));
            LocalDate end = start.plusDays(6);
            log.debug("Неделя: {} - {}", start, end);
            return new Period(start, end);
        }
    },

    CURRENT_MONTH("Текущий месяц") {
        @Override
        public Period calculate(LocalDate baseDate) {
            log.debug("Расчет периода текущего месяца для даты: {}", baseDate);
            LocalDate start = baseDate.withDayOfMonth(1);
            LocalDate end = baseDate.with(TemporalAdjusters.lastDayOfMonth());
            log.debug("Месяц: {} - {}", start, end);
            return new Period(start, end);
        }
    };

    private final String description;

    PeriodType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // Рассчитать период для указанной даты
    public abstract Period calculate(LocalDate baseDate);

    // Рассчитать период для сегодня
    public Period calculateForToday() {
        log.info("Расчет периода {} для сегодняшней даты", this);
        return calculate(LocalDate.now());
    }

    // Найти PeriodType по описанию
    public static PeriodType fromDescription(String description) {
        log.debug("Поиск PeriodType по описанию: '{}'", description);
        for (PeriodType type : values()) {
            if (type.getDescription().equals(description)) {
                log.debug("Найден: {}", type);
                return type;
            }
        }
        log.warn("PeriodType не найден для описания: '{}'", description);
        return null;
    }
}