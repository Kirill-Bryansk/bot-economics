package ru.boteconomics.bot.core.expense.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExpenseDTO {
    private Long id;
    private Long userId;
    private String category;
    private String childName;
    private String childCategory;
    private String housingCategory;
    private String transportCategory;
    private String productsCategory;
    private String healthCategory;
    private String miscellaneousCategory;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private String description;

    public ExpenseDTO() {
        this.createdAt = LocalDateTime.now();
    }

    // Метод для создания DTO из сессии
    public static ExpenseDTO fromSession(Long userId, String category,
                                         String childName, String childCategory,
                                         String housingCategory, String transportCategory,
                                         String productsCategory, String healthCategory,
                                         String miscellaneousCategory,
                                         BigDecimal amount) {
        ExpenseDTO dto = new ExpenseDTO();
        dto.setUserId(userId);
        dto.setCategory(category);
        dto.setChildName(childName);
        dto.setChildCategory(childCategory);
        dto.setHousingCategory(housingCategory);
        dto.setTransportCategory(transportCategory);
        dto.setProductsCategory(productsCategory);
        dto.setHealthCategory(healthCategory);
        dto.setMiscellaneousCategory(miscellaneousCategory);
        dto.setAmount(amount);
        return dto;
    }

    public String toSummaryString() {
        StringBuilder sb = new StringBuilder();
        sb.append("💰 ").append(amount).append(" ₽");
        sb.append(" | ").append(category);

        if (childName != null) {
            sb.append(" | 👶 ").append(childName);
        }
        if (childCategory != null) {
            sb.append(" | ").append(childCategory);
        }
        if (housingCategory != null) {
            sb.append(" | ").append(housingCategory);
        }
        if (transportCategory != null) {
            sb.append(" | ").append(transportCategory);
        }
        if (productsCategory != null) {
            sb.append(" | ").append(productsCategory);
        }
        if (healthCategory != null) {
            sb.append(" | ").append(healthCategory);
        }
        if (miscellaneousCategory != null) {
            sb.append(" | ").append(miscellaneousCategory);
        }

        return sb.toString();
    }
}