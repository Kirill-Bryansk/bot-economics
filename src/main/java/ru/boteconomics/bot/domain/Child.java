package ru.boteconomics.bot.domain;

public enum Child {
    ARTEMIY("Артемий", "👦"),
    ARINA("Арина", "👧"),
    EKATERINA("Екатерина", "👶");

    private final String displayName;
    private final String emoji;

    Child(String displayName, String emoji) {
        this.displayName = displayName;
        this.emoji = emoji;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getFullName() {
        return emoji + " " + displayName;
    }

    public static Child fromCallback(String callbackData) {
        try {
            return valueOf(callbackData.replace("SHOW_", ""));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}