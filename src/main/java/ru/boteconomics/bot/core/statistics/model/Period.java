package ru.boteconomics.bot.core.statistics.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Модель периода для статистики.
 * Содержит начальную и конечную даты периода (включительно).
 */
@Slf4j
@Getter
@ToString
@AllArgsConstructor
public class Period {

    private final LocalDate startDate;
    private final LocalDate endDate;

    // Форматтер для красивого вывода дат
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");

    /**
     * Проверяет, попадает ли дата в период.
     */
    public boolean contains(LocalDate date) {
        boolean contains = !date.isBefore(startDate) && !date.isAfter(endDate);
        log.debug("Проверка даты {} в периоде {}: {}", date, this, contains);
        return contains;
    }

    /**
     * Проверяет, пересекается ли период с другим периодом.
     */
    public boolean overlaps(Period other) {
        boolean overlaps = !this.endDate.isBefore(other.startDate) &&
                           !this.startDate.isAfter(other.endDate);
        log.debug("Пересечение периодов {} и {}: {}", this, other, overlaps);
        return overlaps;
    }

    /**
     * Получить количество дней в периоде.
     */
    public long getDaysCount() {
        long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
        log.debug("Количество дней в периоде {}: {}", this, days);
        return days;
    }

    /**
     * Форматированное представление периода для пользователя.
     */
    public String toFormattedString() {
        String formatted = String.format("%s - %s",
                startDate.format(DATE_FORMATTER),
                endDate.format(DATE_FORMATTER));
        log.debug("Форматирование периода: {}", formatted);
        return formatted;
    }

    /**
     * Проверяет валидность периода (начало <= конец).
     */
    public boolean isValid() {
        boolean valid = !startDate.isAfter(endDate);
        log.debug("Проверка валидности периода {}: {}", this, valid);
        return valid;
    }
}