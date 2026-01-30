package ru.boteconomics.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.boteconomics.bot.core.MessageSender;
import ru.boteconomics.bot.core.UpdateProcessor;
import ru.boteconomics.bot.core.replykeyboard.ReplyKeyboardManager;
import ru.boteconomics.config.BotConfig;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final UpdateProcessor updateProcessor;
    private final MessageSender messageSender;
    private final ReplyKeyboardManager keyboardManager;

    public TelegramBot(BotConfig botConfig,
                       UpdateProcessor updateProcessor,
                       MessageSender messageSender, ReplyKeyboardManager keyboardManager) {
        super(botConfig.getBotToken());
        this.botConfig = botConfig;
        this.updateProcessor = updateProcessor;
        this.messageSender = messageSender;
        this.keyboardManager = keyboardManager;

        // Устанавливаем бота в MessageSender
        this.messageSender.setBot(this);

        log.info("✅ TelegramBot создан, имя: {}", botConfig.getBotName());
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = updateProcessor.extractChatId(update);
        if (chatId == null) {
            log.warn("Не удалось получить chatId из update");
            return;
        }

        log.debug("\n" + "=".repeat(50));
        log.debug("Получен update от chatId: {}", chatId);

        // Проверка доступа (пока закомментируем для теста)
        // if (!botConfig.isAnna(chatId)) {
        //     messageSender.send(chatId, "🚫 Доступ запрещен");
        //     return;
        // }

        // Отдельная обработка команды /start для приветствия
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            if ("/start".equals(text)) {
                log.info("Обработка команды /start для chatId={}", chatId);

                // Получаем клавиатуру для главного меню
                var keyboard = keyboardManager.getKeyboardForState("MAIN_MENU");
                messageSender.send(chatId,
                        """
                        👋 Привет! Я бот для учета расходов семьи.
                        
                        Используйте кнопки ниже для управления:
                        """, keyboard);
                // UpdateProcessor сам добавит клавиатуру при следующем сообщении
                return;
            }
        }

        // Передаем update в процессор
        updateProcessor.process(update);

        log.debug("=".repeat(50));
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }
}