package ru.boteconomics.bot.core.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class UserSessionManager {
    private final Map<Long, UserSession> sessions = new ConcurrentHashMap<>();

    public UserSession getSession(Long chatId) {
        if (chatId == null) return null;
        return sessions.get(chatId);
    }

    public UserSession createSession(Long chatId) {
        if (chatId == null) {
            throw new IllegalArgumentException("chatId не может быть null");
        }

        UserSession session = new UserSession();
        session.setUserId(chatId);
        session.setCurrentStateId("MAIN_MENU");
        sessions.put(chatId, session);

        log.info("Создана сессия для chatId={}", chatId);
        return session;
    }

    public UserSession getOrCreateSession(Long chatId) {
        if (chatId == null) {
            throw new IllegalArgumentException("chatId не может быть null");
        }

        return sessions.computeIfAbsent(chatId, k -> {
            UserSession session = new UserSession();
            session.setUserId(chatId);
            session.setCurrentStateId("MAIN_MENU");
            log.info("Создана новая сессия для chatId={}", chatId);
            return session;
        });
    }

    public void removeSession(Long chatId) {
        if (chatId != null) {
            sessions.remove(chatId);
            log.info("Удалена сессия для chatId={}", chatId);
        }
    }

    // Для отладки
    public void printSession(Long chatId) {
        UserSession session = sessions.get(chatId);
        if (session != null) {
            log.debug("Сессия chatId={}, состояние={}, категория={}, сумма={}",
                    chatId, session.getCurrentStateId(), session.getCategory(), session.getAmount());
        } else {
            log.debug("Нет сессии для chatId={}", chatId);
        }
    }

    // Опционально: метод для очистки всех сессий
    public void clearAll() {
        int count = sessions.size();
        sessions.clear();
        log.info("Очищены все {} сессий", count);
    }
}