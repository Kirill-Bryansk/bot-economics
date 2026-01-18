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
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private String description;

    public ExpenseDTO() {
        this.createdAt = LocalDateTime.now();
    }

    // Можно добавить статический фабричный метод
    public static ExpenseDTO fromSession(Long userId, String category,
                                         String childName, String childCategory,
                                         BigDecimal amount) {
        ExpenseDTO dto = new ExpenseDTO();
        dto.setUserId(userId);
        dto.setCategory(category);
        dto.setChildName(childName);
        dto.setChildCategory(childCategory);
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

        return sb.toString();
    }
}