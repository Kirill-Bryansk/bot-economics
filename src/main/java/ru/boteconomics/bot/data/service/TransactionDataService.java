package ru.boteconomics.bot.data.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.boteconomics.bot.core.expense.dto.ExpenseDTO;
import ru.boteconomics.bot.core.buttons.*;
import ru.boteconomics.bot.data.entity.*;
import ru.boteconomics.bot.data.mapper.TransactionMapper;
import ru.boteconomics.bot.data.repository.*;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionDataService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final ChildRepository childRepository;
    private final TransactionEntryRepository transactionEntryRepository;
    private final TransactionMapper transactionMapper;

    @Transactional
    public void saveTransaction(ExpenseDTO expenseDto) {
        log.info("Сохранение транзакции: {}", expenseDto);

        // Сохраняем основную транзакцию
        TransactionEntity transaction = transactionMapper.toTransactionEntity(expenseDto);
        transaction = transactionRepository.save(transaction);

        // Получаем или создаем категорию
        CategoryEntity category = categoryRepository.findByName(expenseDto.getCategory())
                .orElseGet(() -> {
                    CategoryEntity newCategory = new CategoryEntity();
                    newCategory.setName(expenseDto.getCategory());
                    return categoryRepository.save(newCategory);
                });

        // Создаем и сохраняем связь транзакции с категорией
        TransactionEntryEntity entry = new TransactionEntryEntity();
        entry.setTransactionId(transaction.getId());
        entry.setCategoryId(category.getId());

        // Устанавливаем подкатегорию в зависимости от основной категории
        if (CategoryButton.CHILDREN.equals(expenseDto.getCategory())) {
            if (expenseDto.getChildCategory() != null) {
                Optional<SubcategoryEntity> subcategoryOpt = subcategoryRepository
                        .findByCategoryIdAndName(category.getId(), expenseDto.getChildCategory());
                SubcategoryEntity subcategory = subcategoryOpt.orElseGet(() -> {
                    SubcategoryEntity newSubcategory = new SubcategoryEntity();
                    newSubcategory.setCategory(category);
                    newSubcategory.setName(expenseDto.getChildCategory());
                    return subcategoryRepository.save(newSubcategory);
                });
                entry.setSubcategoryId(subcategory.getId());
            }

            // Устанавливаем ребенка
            if (expenseDto.getChildName() != null) {
                Optional<ChildEntity> childOpt = childRepository.findByName(expenseDto.getChildName());
                ChildEntity child = childOpt.orElseGet(() -> {
                    ChildEntity newChild = new ChildEntity();
                    newChild.setName(expenseDto.getChildName());
                    return childRepository.save(newChild);
                });
                entry.setChildId(child.getId());
            }
        } else if (CategoryButton.HOUSING.equals(expenseDto.getCategory())) {
            if (expenseDto.getHousingCategory() != null) {
                Optional<SubcategoryEntity> subcategoryOpt = subcategoryRepository
                        .findByCategoryIdAndName(category.getId(), expenseDto.getHousingCategory());
                SubcategoryEntity subcategory = subcategoryOpt.orElseGet(() -> {
                    SubcategoryEntity newSubcategory = new SubcategoryEntity();
                    newSubcategory.setCategory(category);
                    newSubcategory.setName(expenseDto.getHousingCategory());
                    return subcategoryRepository.save(newSubcategory);
                });
                entry.setSubcategoryId(subcategory.getId());
            }
        } else if (CategoryButton.TRANSPORT.equals(expenseDto.getCategory())) {
            if (expenseDto.getTransportCategory() != null) {
                Optional<SubcategoryEntity> subcategoryOpt = subcategoryRepository
                        .findByCategoryIdAndName(category.getId(), expenseDto.getTransportCategory());
                SubcategoryEntity subcategory = subcategoryOpt.orElseGet(() -> {
                    SubcategoryEntity newSubcategory = new SubcategoryEntity();
                    newSubcategory.setCategory(category);
                    newSubcategory.setName(expenseDto.getTransportCategory());
                    return subcategoryRepository.save(newSubcategory);
                });
                entry.setSubcategoryId(subcategory.getId());
            }
        } else if (CategoryButton.FOOD.equals(expenseDto.getCategory())) {
            if (expenseDto.getProductsCategory() != null) {
                Optional<SubcategoryEntity> subcategoryOpt = subcategoryRepository
                        .findByCategoryIdAndName(category.getId(), expenseDto.getProductsCategory());
                SubcategoryEntity subcategory = subcategoryOpt.orElseGet(() -> {
                    SubcategoryEntity newSubcategory = new SubcategoryEntity();
                    newSubcategory.setCategory(category);
                    newSubcategory.setName(expenseDto.getProductsCategory());
                    return subcategoryRepository.save(newSubcategory);
                });
                entry.setSubcategoryId(subcategory.getId());
            }
        } else if (CategoryButton.HEALTH.equals(expenseDto.getCategory())) {
            if (expenseDto.getHealthCategory() != null) {
                Optional<SubcategoryEntity> subcategoryOpt = subcategoryRepository
                        .findByCategoryIdAndName(category.getId(), expenseDto.getHealthCategory());
                SubcategoryEntity subcategory = subcategoryOpt.orElseGet(() -> {
                    SubcategoryEntity newSubcategory = new SubcategoryEntity();
                    newSubcategory.setCategory(category);
                    newSubcategory.setName(expenseDto.getHealthCategory());
                    return subcategoryRepository.save(newSubcategory);
                });
                entry.setSubcategoryId(subcategory.getId());
            }
        } else if (CategoryButton.OTHER.equals(expenseDto.getCategory())) {
            if (expenseDto.getMiscellaneousCategory() != null) {
                Optional<SubcategoryEntity> subcategoryOpt = subcategoryRepository
                        .findByCategoryIdAndName(category.getId(), expenseDto.getMiscellaneousCategory());
                SubcategoryEntity subcategory = subcategoryOpt.orElseGet(() -> {
                    SubcategoryEntity newSubcategory = new SubcategoryEntity();
                    newSubcategory.setCategory(category);
                    newSubcategory.setName(expenseDto.getMiscellaneousCategory());
                    return subcategoryRepository.save(newSubcategory);
                });
                entry.setSubcategoryId(subcategory.getId());
            }
        }
        // Для категории "Личное" (PERSONAL) подкатегории не предусмотрены

        // Сохраняем связь
        transactionEntryRepository.save(entry);

        log.info("Транзакция успешно сохранена: {}", transaction.getId());
    }
}