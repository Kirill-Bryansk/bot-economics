package ru.boteconomics.bot.core.statistics.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boteconomics.bot.core.statistics.model.Period;
import ru.boteconomics.bot.core.statistics.model.StatsResult;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Форматирует результаты статистики в читаемый текст для пользователя.
 */
@Slf4j
@Component
public class StatsFormatter {

    // Эмодзи для разных элементов отчета
    private static final String EMOJI_TOTAL = "💰";
    private static final String EMOJI_CATEGORY = "▫️";
    private static final String EMOJI_AVERAGE = "📊";
    private static final String EMOJI_OPERATIONS = "📈";
    private static final String EMOJI_NO_DATA = "😴";

    /**
     * Форматировать полный отчет статистики.
     */
    public String formatFullReport(StatsResult result, Period period) {
        log.info("Форматирование полного отчета за период: {}", period);

        StringBuilder sb = new StringBuilder();

        // Заголовок
        sb.append("📊 *Статистика расходов*\n");
        sb.append("Период: ").append(period.toFormattedString()).append("\n\n");

        if (result.isEmpty()) {
            sb.append(formatNoDataMessage(period));
            log.debug("Отчет пуст, показано сообщение об отсутствии данных");
            return sb.toString();
        }

        // Категории с сортировкой по убыванию
        Map<String, BigDecimal> sortedCategories = result.getSortedCategories();
        for (Map.Entry<String, BigDecimal> entry : sortedCategories.entrySet()) {
            String category = entry.getKey();
            BigDecimal amount = entry.getValue();
            BigDecimal percentage = result.getCategoryPercentage(category);

            sb.append(String.format("%s %s: *%.0f ₽* (%.1f%%)\n",
                    EMOJI_CATEGORY, category, amount, percentage));
        }

        sb.append("\n");

        // Итоги
        sb.append(String.format("%s Всего: *%.0f ₽*\n",
                EMOJI_TOTAL, result.getTotalAmount()));
        sb.append(String.format("%s Среднее за операцию: *%.0f ₽*\n",
                EMOJI_AVERAGE, result.getAveragePerOperation()));
        sb.append(String.format("%s Количество операций: *%d*\n",
                EMOJI_OPERATIONS, result.getOperationsCount()));

        String formatted = sb.toString();
        log.debug("Отчет сформирован, {} символов", formatted.length());
        return formatted;
    }

    /**
     * Форматировать краткий отчет (только итоги).
     */
    public String formatShortReport(StatsResult result, Period period) {
        log.debug("Форматирование краткого отчета за период: {}", period);

        return String.format(
                "📊 Статистика за %s\n" +
                "💰 Всего: %.0f ₽\n" +
                "📈 Операций: %d\n" +
                "📊 Среднее: %.0f ₽",
                period.toFormattedString(),
                result.getTotalAmount(),
                result.getOperationsCount(),
                result.getAveragePerOperation()
        );
    }

    /**
     * Сообщение при отсутствии данных.
     */
    private String formatNoDataMessage(Period period) {
        log.debug("Формирование сообщения об отсутствии данных за период: {}", period);

        return String.format(
                "%s *Нет данных за период*\n\n" +
                "Период: %s\n" +
                "Расходы за этот период не найдены.",
                EMOJI_NO_DATA, period.toFormattedString()
        );
    }

    /**
     * Форматировать одну категорию для inline вывода.
     */
    public String formatCategoryLine(String category, BigDecimal amount, BigDecimal percentage) {
        String line = String.format("%s %s: %.0f ₽ (%.1f%%)",
                EMOJI_CATEGORY, category, amount, percentage);
        log.debug("Форматирование строки категории: {}", line);
        return line;
    }

    /**
     * Добавить финальное сообщение "Что дальше?".
     */
    public String appendNextStepsMessage(String report) {
        log.debug("Добавление сообщения о следующих шагах к отчету");

        return report + "\n\nЧто хотите сделать дальше?";
    }
}