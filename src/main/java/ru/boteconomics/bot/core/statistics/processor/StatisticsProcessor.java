package ru.boteconomics.bot.core.statistics.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.buttons.ActionButton;
import ru.boteconomics.bot.core.response.HandlerResponse;
import ru.boteconomics.bot.core.statistics.buttons.StatisticsButton;
import ru.boteconomics.bot.core.statistics.service.StatisticsService;
import ru.boteconomics.bot.core.statistics.util.StatsFormatter;
import ru.boteconomics.bot.core.statistics.model.StatsResult;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatisticsProcessor {

    private final StatisticsService statisticsService;
    private final StatsFormatter statsFormatter;

    /**
     * Обработать ввод пользователя в меню статистики.
     * @param currentStateId ID текущего состояния (нужен для stay)
     */
    public HandlerResponse process(String input, Long userId, String currentStateId) {
        log.info("Обработка ввода в статистике: '{}' для userId={}, stateId={}",
                input, userId, currentStateId);

        // Обработка кнопок статистики
        if (StatisticsButton.isCurrentWeek(input)) {
            return handleCurrentWeek(userId);
        }

        if (StatisticsButton.isCurrentMonth(input)) {
            return handleCurrentMonth(userId);
        }

        // Обработка действия "Назад"
        if (ActionButton.BACK.equals(input)) {
            return handleBackAction();
        }

        // Неожиданный ввод
        log.warn("Неизвестный ввод в StatisticsProcessor: '{}'", input);
        return HandlerResponse.stay(
                "Пожалуйста, используйте кнопки меню",
                currentStateId  // ← используем переданный stateId для stay
        );
    }

    /**
     * Обработать запрос статистики за текущую неделю.
     */
    private HandlerResponse handleCurrentWeek(Long userId) {
        log.info("Запрос статистики за текущую неделю для userId={}", userId);

        try {
            StatsResult result = statisticsService.calculateCurrentWeek(userId);
            String report = statsFormatter.formatFullReport(result,
                    ru.boteconomics.bot.core.statistics.enums.PeriodType.CURRENT_WEEK.calculateForToday());

            String fullMessage = statsFormatter.appendNextStepsMessage(report);
            log.debug("Статистика за неделю сформирована");

            return HandlerResponse.next(fullMessage, "MAIN_MENU");

        } catch (Exception e) {
            log.error("Ошибка расчета статистики за неделю: {}", e.getMessage(), e);
            return HandlerResponse.stay(
                    "❌ Ошибка расчета статистики. Попробуйте позже.",
                    "STATISTICS_MENU"  // ← остаемся в меню статистики при ошибке
            );
        }
    }

    /**
     * Обработать запрос статистики за текущий месяц.
     */
    private HandlerResponse handleCurrentMonth(Long userId) {
        log.info("Запрос статистики за текущий месяц для userId={}", userId);

        try {
            StatsResult result = statisticsService.calculateCurrentMonth(userId);
            String report = statsFormatter.formatFullReport(result,
                    ru.boteconomics.bot.core.statistics.enums.PeriodType.CURRENT_MONTH.calculateForToday());

            String fullMessage = statsFormatter.appendNextStepsMessage(report);
            log.debug("Статистика за месяц сформирована");

            return HandlerResponse.next(fullMessage, "MAIN_MENU");

        } catch (Exception e) {
            log.error("Ошибка расчета статистики за месяц: {}", e.getMessage(), e);
            return HandlerResponse.stay(
                    "❌ Ошибка расчета статистики. Попробуйте позже.",
                    "STATISTICS_MENU"  // ← остаемся в меню статистики
            );
        }
    }

    /**
     * Обработать действие "Назад".
     */
    private HandlerResponse handleBackAction() {
        log.info("Действие 'Назад' в статистике");

        return HandlerResponse.next(
                "Возврат в главное меню",
                "MAIN_MENU"
        );
    }

    public boolean hasStatisticsData(Long userId) {
        log.debug("Проверка наличия данных статистики для userId={}", userId);
        return true;
    }
}