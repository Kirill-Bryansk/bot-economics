package ru.boteconomics.bot.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "transaction_entries")
@IdClass(TransactionEntryEntity.TransactionEntryId.class)
@Getter
@Setter
public class TransactionEntryEntity {

    @Id
    @Column(name = "transaction_id")
    private Long transactionId;

    @Id
    @Column(name = "category_id")
    private Integer categoryId;

    @Id
    @Column(name = "subcategory_id")
    private Integer subcategoryId;

    @Id
    @Column(name = "child_id")
    private Integer childId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", insertable = false, updatable = false)
    private TransactionEntity transaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id", insertable = false, updatable = false)
    private SubcategoryEntity subcategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", insertable = false, updatable = false)
    private ChildEntity child;

    // Вложенный класс для составного ключа
    @Getter
    @Setter
    public static class TransactionEntryId {
        private Long transactionId;
        private Integer categoryId;
        private Integer subcategoryId;
        private Integer childId;
    }

    // Конструктор по умолчанию для JPA
    public TransactionEntryEntity() {}
}