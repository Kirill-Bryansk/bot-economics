package ru.boteconomics.bot.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.boteconomics.bot.data.entity.SubcategoryEntity;

import java.util.Optional;

@Repository
public interface SubcategoryRepository extends JpaRepository<SubcategoryEntity, Integer> {
    Optional<SubcategoryEntity> findByCategoryIdAndName(Integer categoryId, String name);
}