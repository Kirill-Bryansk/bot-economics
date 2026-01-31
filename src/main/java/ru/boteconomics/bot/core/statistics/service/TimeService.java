package ru.boteconomics.bot.core.statistics.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

/**
 * Сервис для работы со временем в московском часовом поясе.
 * Все даты в боте приводятся к московскому времени.
 */
@Slf4j
@Service
public class TimeService {

    // Московский часовой пояс
    private static final ZoneId MOSCOW_ZONE = ZoneId.of("Europe/Moscow");

    // Форматтеры для разных целей
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    /**
     * Получить текущую дату в Москве.
     */
    public LocalDate getCurrentMoscowDate() {
        LocalDate date = LocalDate.now(MOSCOW_ZONE);
        log.debug("Текущая дата в Москве: {}", date);
        return date;
    }

    /**
     * Получить текущее время в Москве.
     */
    public LocalDateTime getCurrentMoscowDateTime() {
        LocalDateTime dateTime = LocalDateTime.now(MOSCOW_ZONE);
        log.debug("Текущее время в Москве: {}", dateTime);
        return dateTime;
    }

    /**
     * Конвертировать UTC время в московское.
     */
    public LocalDateTime convertToMoscow(LocalDateTime utcDateTime) {
        ZonedDateTime utcZoned = utcDateTime.atZone(ZoneOffset.UTC);
        ZonedDateTime moscowZoned = utcZoned.withZoneSameInstant(MOSCOW_ZONE);
        LocalDateTime moscowDateTime = moscowZoned.toLocalDateTime();

        log.debug("Конвертация UTC {} → Москва {}", utcDateTime, moscowDateTime);
        return moscowDateTime;
    }

    /**
     * Получить дату начала дня в Москве.
     */
    public LocalDateTime getStartOfDay(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay().atZone(MOSCOW_ZONE).toLocalDateTime();
        log.debug("Начало дня {} в Москве: {}", date, startOfDay);
        return startOfDay;
    }

    /**
     * Получить дату конца дня в Москве.
     */
    public LocalDateTime getEndOfDay(LocalDate date) {
        LocalDateTime endOfDay = date.atTime(23, 59, 59).atZone(MOSCOW_ZONE).toLocalDateTime();
        log.debug("Конец дня {} в Москве: {}", date, endOfDay);
        return endOfDay;
    }

    /**
     * Отформатировать дату для пользователя.
     */
    public String formatDate(LocalDate date) {
        String formatted = date.format(DATE_FORMATTER);
        log.debug("Форматирование даты {} → {}", date, formatted);
        return formatted;
    }

    /**
     * Отформатировать дату и время для пользователя.
     */
    public String formatDateTime(LocalDateTime dateTime) {
        String formatted = dateTime.format(DATE_TIME_FORMATTER);
        log.debug("Форматирование даты-времени {} → {}", dateTime, formatted);
        return formatted;
    }

    /**
     * Проверить, является ли дата сегодняшней в Москве.
     */
    public boolean isToday(LocalDate date) {
        boolean today = date.equals(getCurrentMoscowDate());
        log.debug("Проверка является ли {} сегодняшней датой: {}", date, today);
        return today;
    }

    /**
     * Получить первый день месяца для указанной даты.
     */
    public LocalDate getFirstDayOfMonth(LocalDate date) {
        LocalDate firstDay = date.with(TemporalAdjusters.firstDayOfMonth());
        log.debug("Первый день месяца для {}: {}", date, firstDay);
        return firstDay;
    }

    /**
     * Получить последний день месяца для указанной даты.
     */
    public LocalDate getLastDayOfMonth(LocalDate date) {
        LocalDate lastDay = date.with(TemporalAdjusters.lastDayOfMonth());
        log.debug("Последний день месяца для {}: {}", date, lastDay);
        return lastDay;
    }
}