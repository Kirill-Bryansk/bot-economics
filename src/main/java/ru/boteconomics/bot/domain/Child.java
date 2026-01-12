package ru.boteconomics.bot.domain;

public enum Child {
    ARTEMIY("Артемий", "👦"),    // ← ARTEMIY должно быть ARTEMIY
    ARINA("Арина", "👧"),        // ← ARINA должно быть ARINA
    EKATERINA("Екатерина", "👧"); // ← EKATERINA должно быть EKATERINA

    private final String displayName;
    private final String emoji;

    Child(String displayName, String emoji) {
        this.displayName = displayName;
        this.emoji = emoji;
    }

    public String getFullName() {
        return emoji + " " + displayName;
    }

    public static Child fromCallback(String callbackData) {
        try {
            return valueOf(callbackData.replace("SHOW_CHILD_", ""));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}