package ru.boteconomics.bot.util;

import ru.boteconomics.bot.domain.Category;
import ru.boteconomics.bot.domain.Child;
import ru.boteconomics.bot.domain.TransactionType;

import java.time.LocalDate;

public class TransactionFilter {
    private int page = 0;
    private int pageSize = 3; // По вашему требованию: 3 транзакции на страницу
    private TransactionType type; // null = все, INCOME = только доходы, EXPENSE = только расходы
    private LocalDate startDate;
    private LocalDate endDate;
    private Child child; // Фильтр по ребенку
    private Category category; // Фильтр по категории

    // Конструкторы
    public TransactionFilter() {
    }

    public TransactionFilter(int page) {
        this.page = page;
    }

    // Геттеры и сеттеры
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    // Утилитные методы
    public boolean hasTypeFilter() {
        return type != null;
    }

    public boolean hasDateFilter() {
        return startDate != null || endDate != null;
    }

    public boolean hasChildFilter() {
        return child != null;
    }

    public boolean hasCategoryFilter() {
        return category != null;
    }

    public boolean hasFilters() {
        return hasTypeFilter() || hasDateFilter() || hasChildFilter() || hasCategoryFilter();
    }

    public void nextPage() {
        this.page++;
    }

    public void previousPage() {
        if (this.page > 0) {
            this.page--;
        }
    }

    public int getOffset() {
        return page * pageSize;
    }

    // Статические методы для создания фильтров
    public static TransactionFilter pageOnly(int page) {
        return new TransactionFilter(page);
    }

    public static TransactionFilter forType(TransactionType type, int page) {
        TransactionFilter filter = new TransactionFilter(page);
        filter.setType(type);
        return filter;
    }

    public static TransactionFilter forChild(Child child, int page) {
        TransactionFilter filter = new TransactionFilter(page);
        filter.setChild(child);
        return filter;
    }

    public static TransactionFilter forToday(int page) {
        TransactionFilter filter = new TransactionFilter(page);
        filter.setStartDate(LocalDate.now());
        filter.setEndDate(LocalDate.now());
        return filter;
    }

    @Override
    public String toString() {
        return "TransactionFilter{" +
               "page=" + page +
               ", pageSize=" + pageSize +
               ", type=" + type +
               ", startDate=" + startDate +
               ", endDate=" + endDate +
               ", child=" + child +
               ", category=" + category +
               '}';
    }
}