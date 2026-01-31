package ru.boteconomics.bot.core.expense.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.boteconomics.bot.data.service.TransactionDataService;
import ru.boteconomics.bot.core.expense.dto.ExpenseDTO;
import ru.boteconomics.bot.core.session.UserSession;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final TransactionDataService transactionDataService;

    /**
     * Сохраняет расход на основе данных из сессии.
     */
    public ExpenseDTO saveExpense(UserSession session) {
        log.info("📥 Получен запрос на сохранение расхода:");
        logDetailedSession(session);

        // Получаем userId из сессии
        Long userId = session.getUserId();
        if (userId == null) {
            log.warn("userId не найден в сессии, используем заглушку");
            userId = 1L; // Заглушка на случай, если userId не установлен
        }

        // Создаем DTO из сессии
        ExpenseDTO expense = ExpenseDTO.fromSession(
                userId,
                session.getCategory(),
                session.getChildName(),
                session.getChildCategory(),
                session.getHousingCategory(),
                session.getTransportCategory(),
                session.getProductsCategory(),
                session.getHealthCategory(),
                session.getMiscellaneousCategory(),
                session.getAmount()
        );

        // Логируем, что будет передано на сохранение
        log.info("📤 DTO для сохранения в БД:");
        logExpenseDto(expense);

        // Сохраняем в базу данных через сервис
        transactionDataService.saveTransaction(expense);

        log.info("✅ Расход успешно сохранен: {}", expense.toSummaryString());

        return expense;
    }

    /**
     * Логирует детальную информацию о сессии.
     */
    private void logDetailedSession(UserSession session) {
        log.info("┌─────────────────────────────────────────");
        log.info("│ Сессия пользователя:");
        log.info("├─────────────────────────────────────────");
        log.info("│ Текущее состояние: {}", session.getCurrentStateId());
        log.info("│ Категория: {}", session.getCategory());

        if (session.getUserId() != null) {
            log.info("│ userId: {}", session.getUserId());
        }

        if (session.getChildName() != null) {
            log.info("│ Ребенок: {}", session.getChildName());
        }

        if (session.getChildCategory() != null) {
            log.info("│ Подкатегория ребенка: {}", session.getChildCategory());
        }

        if (session.getHousingCategory() != null) {
            log.info("│ Подкатегория жилья: {}", session.getHousingCategory());
        }

        if (session.getTransportCategory() != null) {
            log.info("│ Подкатегория транспорта: {}", session.getTransportCategory());
        }

        if (session.getProductsCategory() != null) {
            log.info("│ Подкатегория продуктов: {}", session.getProductsCategory());
        }

        if (session.getHealthCategory() != null) {
            log.info("│ Подкатегория здоровья: {}", session.getHealthCategory());
        }

        if (session.getMiscellaneousCategory() != null) {
            log.info("│ Подкатегория 'Разное': {}", session.getMiscellaneousCategory());
        }

        if (session.getAmount() != null) {
            log.info("│ Сумма: {} ₽", session.getAmount());
        }

        log.info("│ Готово для сохранения: {}", session.isReadyForSaving());
        log.info("└─────────────────────────────────────────");
    }

    /**
     * Логирует содержимое ExpenseDTO перед сохранением.
     */
    private void logExpenseDto(ExpenseDTO expense) {
        log.info("┌─────────────────────────────────────────");
        log.info("│ DTO для сохранения в БД:");
        log.info("├─────────────────────────────────────────");
        log.info("│ userId: {}", expense.getUserId());
        log.info("│ category: {}", expense.getCategory());
        log.info("│ childName: {}", expense.getChildName());
        log.info("│ childCategory: {}", expense.getChildCategory());
        log.info("│ housingCategory: {}", expense.getHousingCategory());
        log.info("│ transportCategory: {}", expense.getTransportCategory());
        log.info("│ productsCategory: {}", expense.getProductsCategory());
        log.info("│ healthCategory: {}", expense.getHealthCategory());
        log.info("│ miscellaneousCategory: {}", expense.getMiscellaneousCategory());
        log.info("│ amount: {} ₽", expense.getAmount());
        log.info("│ description: {}", expense.getDescription());
        log.info("└─────────────────────────────────────────");
    }

    /**
     * Генерирует сообщение об успешном сохранении для пользователя.
     */
    public String generateSuccessMessage(ExpenseDTO expense) {
        StringBuilder sb = new StringBuilder();
        sb.append("✅ Расход успешно сохранен!\n\n");
        sb.append("💰 Сумма: ").append(expense.getAmount()).append(" ₽\n");
        sb.append("📂 Категория: ").append(expense.getCategory()).append("\n");

        if (expense.getChildName() != null) {
            sb.append("👶 Ребенок: ").append(expense.getChildName()).append("\n");
        }

        if (expense.getChildCategory() != null) {
            sb.append("🏷️ Подкатегория: ").append(expense.getChildCategory()).append("\n");
        }

        sb.append("\nЧто хотите сделать дальше?");
        return sb.toString();
    }
}