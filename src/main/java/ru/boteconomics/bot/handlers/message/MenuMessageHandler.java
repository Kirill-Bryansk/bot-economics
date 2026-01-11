package ru.boteconomics.bot.handlers.message;

import org.springframework.stereotype.Component;
import ru.boteconomics.bot.handlers.HandlerResult;
import ru.boteconomics.bot.keyboard.InlineKeyboardFactory;
import ru.boteconomics.bot.keyboard.MainKeyboardFactory;
import ru.boteconomics.bot.service.HistoryService;
import ru.boteconomics.bot.service.ReportService;
import ru.boteconomics.bot.state.UserStateService;

import java.util.Set;

@Component
public class MenuMessageHandler extends BaseMessageHandler {

    private static final Set<String> MENU_COMMANDS = Set.of(
            "/start",
            "🏠 Главное меню",
            "📊 Отчет за сегодня",
            "📈 Отчет за месяц",
            "❓ Помощь"
    );

    private final ReportService reportService;

    public MenuMessageHandler(MainKeyboardFactory mainKeyboardFactory,
                              HistoryService historyService,
                              ValidationService validationService,
                              UserStateService userStateService,
                              ReportService reportService) {
        super(mainKeyboardFactory, historyService, validationService, userStateService);
        this.reportService = reportService;
    }

    @Override
    public boolean canHandle(Long chatId, String text) {
        return MENU_COMMANDS.contains(text);
    }

    @Override
    public HandlerResult handle(Long chatId, String text) {
        switch (text) {
            case "/start":
            case "🏠 Главное меню":
                return handleMainMenu(chatId);

            case "📊 Отчет за сегодня":
                return handleDailyReport(chatId);

            case "📈 Отчет за месяц":
                return handleMonthlyReport(chatId);

            case "❓ Помощь":
                return handleHelp();

            default:
                return error("Неизвестная команда меню");
        }
    }

    private HandlerResult handleMainMenu(Long chatId) {
        userStateService.reset(chatId);
        return mainMenu("👋 Привет, Анна!\nВыберите действие:");
    }

    private HandlerResult handleDailyReport(Long chatId) {
        String report = reportService.generateDailyReport(chatId);
        return mainMenu(report);
    }

    private HandlerResult handleMonthlyReport(Long chatId) {
        String report = reportService.generateMonthlyReport(chatId);
        return mainMenu(report);
    }

    private HandlerResult handleHelp() {
        String helpMessage = "❓ Помощь:\n\n" +
                             "💸 Добавить расход - записать новую трату\n" +
                             "💰 Добавить доход - записать новый доход\n" +
                             "📊 Отчет за сегодня - статистика за день\n" +
                             "📈 Отчет за месяц - статистика за месяц\n" +
                             "📋 История операций - просмотр и редактирование всех операций\n" +
                             "🏠 Главное меню - вернуться в основное меню";
        return mainMenu(helpMessage);
    }
}