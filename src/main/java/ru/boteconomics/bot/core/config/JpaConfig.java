package ru.boteconomics.bot.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "ru.boteconomics.bot.data.repository")
public class JpaConfig {
    // Конфигурация JPA
}
