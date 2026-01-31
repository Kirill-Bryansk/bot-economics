package ru.boteconomics.bot.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.boteconomics.bot.data.entity.ChildEntity;

import java.util.Optional;

@Repository
public interface ChildRepository extends JpaRepository<ChildEntity, Integer> {
    Optional<ChildEntity> findByName(String name);
}