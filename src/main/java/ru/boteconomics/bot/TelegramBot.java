package ru.boteconomics.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.boteconomics.bot.core.MessageSender;
import ru.boteconomics.bot.core.UpdateProcessor;
import ru.boteconomics.config.BotConfig;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final UpdateProcessor updateProcessor;
    private final MessageSender messageSender;

    public TelegramBot(BotConfig botConfig,
                       UpdateProcessor updateProcessor,
                       MessageSender messageSender) {
        super(botConfig.getBotToken());
        this.botConfig = botConfig;
        this.updateProcessor = updateProcessor;
        this.messageSender = messageSender;
        System.out.println("✅ TelegramBot создан, имя: " + botConfig.getBotName());
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = updateProcessor.extractChatId(update);
        if (chatId == null) {
            System.out.println("[BOT] Не удалось получить chatId из update");
            return;
        }

        System.out.println("\n" + "=".repeat(50));
        System.out.println("[BOT] Получен update от chatId: " + chatId);

        // Проверка доступа (пока закомментируем для теста)
        // if (!botConfig.isAnna(chatId)) {
        //     messageSender.send(this, chatId, "🚫 Доступ запрещен", null);
        //     return;
        // }

        // Отдельная обработка команды /start для приветствия
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            if ("/start".equals(text)) {
                System.out.println("[BOT] Обработка команды /start");
                messageSender.send(this, chatId,
                        """
                        👋 Привет! Я бот для учета расходов семьи.
                        
                        Используйте кнопки ниже для управления:
                        """,
                        null  // Клавиатуру покажет UpdateProcessor
                );
            }
        }

        var result = updateProcessor.process(update);

        if (result != null) {
            System.out.println("[BOT] Отправляю сообщение в чат " + chatId);
            String messagePreview = result.getMessage().length() > 50
                    ? result.getMessage().substring(0, 50) + "..."
                    : result.getMessage();
            System.out.println("[BOT] Текст: " + messagePreview);
            System.out.println("[BOT] Клавиатура: " + (result.getKeyboard() != null ? "есть" : "нет"));

            messageSender.send(this, chatId,
                    result.getMessage(),
                    result.getKeyboard());
        } else {
            System.out.println("[BOT] UpdateProcessor вернул null");
        }

        System.out.println("=".repeat(50));
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }
}