package ru.boteconomics.bot.core;

import org.springframework.stereotype.Component;

@Component
public class CommandMapper {

    public String toCallback(String text) {
        return null;
    }

    public boolean isKnownCommand(String text) {
        return toCallback(text) != null;
    }
}