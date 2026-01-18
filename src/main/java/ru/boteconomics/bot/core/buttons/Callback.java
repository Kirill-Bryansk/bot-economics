package ru.boteconomics.bot.core.buttons;

public final class Callback {
    private Callback() {
        throw new AssertionError("Нельзя создать экземпляр Callback");
    }

    // ========== ОСНОВНЫЕ КОМАНДЫ (НОВЫЕ ДЛЯ STATE MANAGER) ==========
    public static final String MAIN_MENU = "MAIN_MENU";
    public static final String ADD_EXPENSE = "ADD_EXPENSE";
    public static final String HISTORY = "HISTORY";
    public static final String STATISTICS = "STATISTICS";
    public static final String UNKNOWN_COMMAND = "UNKNOWN_COMMAND";

    // ========== КАТЕГОРИИ (общие) ==========
    public static final String CATEGORY_HOUSING = "CATEGORY_HOUSING";
    public static final String CATEGORY_FOOD = "CATEGORY_FOOD";
    public static final String CATEGORY_TRANSPORT = "CATEGORY_TRANSPORT";
    public static final String CATEGORY_HEALTH = "CATEGORY_HEALTH";
    public static final String CATEGORY_PERSONAL = "CATEGORY_PERSONAL";
    public static final String CATEGORY_OTHER = "CATEGORY_OTHER";

    // ========== НАВИГАЦИЯ ==========
    public static final String SHOW_CHILDREN = "SHOW_CHILDREN";
    public static final String BACK_TO_CATEGORIES = "BACK_TO_CATEGORIES";
    public static final String BACK_TO_CHILDREN = "BACK_TO_CHILDREN";

    // ========== ДЕТИ (выбор ребенка) ==========
    public static final String CHILD_ARTEMIY = "CHILD_ARTEMIY";
    public static final String CHILD_ARINA = "CHILD_ARINA";
    public static final String CHILD_EKATERINA = "CHILD_EKATERINA";

    // ========== КАТЕГОРИИ АРТЕМИЙ ==========
    public static final String CHILD_ARTEMIY_SCHOOL = "CHILD_ARTEMIY_SCHOOL";
    public static final String CHILD_ARTEMIY_SECTIONS = "CHILD_ARTEMIY_SECTIONS";
    public static final String CHILD_ARTEMIY_CLOTHES = "CHILD_ARTEMIY_CLOTHES";

    // ========== КАТЕГОРИИ АРИНА ==========
    public static final String CHILD_ARINA_SCHOOL = "CHILD_ARINA_SCHOOL";
    public static final String CHILD_ARINA_SECTIONS = "CHILD_ARINA_SECTIONS";
    public static final String CHILD_ARINA_CLOTHES = "CHILD_ARINA_CLOTHES";

    // ========== КАТЕГОРИИ ЕКАТЕРИНА ==========
    public static final String CHILD_EKATERINA_SCHOOL = "CHILD_EKATERINA_SCHOOL";
    public static final String CHILD_EKATERINA_SECTIONS = "CHILD_EKATERINA_SECTIONS";
    public static final String CHILD_EKATERINA_CLOTHES = "CHILD_EKATERINA_CLOTHES";

    // ========== ДЕЙСТВИЯ ==========
    public static final String BACK = "BACK";
    public static final String CANCEL = "CANCEL";
    public static final String CONFIRM = "CONFIRM";
}