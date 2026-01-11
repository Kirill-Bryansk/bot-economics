-- Инициализация базы данных семейных финансов
-- Этот файл выполняется при первом запуске контейнера

-- Проверяем, что база данных создана
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'family_finances') THEN
        CREATE DATABASE family_finances;
    END IF;
END $$;

-- Переключаемся на базу данных
\c family_finances;

-- Таблица транзакций
CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    telegram_user_id BIGINT NOT NULL,
    type VARCHAR(10) NOT NULL CHECK (type IN ('INCOME', 'EXPENSE')),
    amount DECIMAL(10,2) NOT NULL CHECK (amount > 0),
    category VARCHAR(50) NOT NULL,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    -- Констрейнты для данных
    CONSTRAINT valid_category CHECK (category ~ '^[A-Z_]+$')
);

-- Индексы для оптимизации запросов
CREATE INDEX IF NOT EXISTS idx_transactions_user_date
    ON transactions(telegram_user_id, created_at DESC);

CREATE INDEX IF NOT EXISTS idx_transactions_category_type
    ON transactions(category, type);

CREATE INDEX IF NOT EXISTS idx_transactions_created_at
    ON transactions(created_at);

-- Таблица для метаданных (если понадобится в будущем)
CREATE TABLE IF NOT EXISTS app_metadata (
    key VARCHAR(50) PRIMARY KEY,
    value TEXT NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Вставляем начальные метаданные
INSERT INTO app_metadata (key, value)
VALUES ('database_version', '1.0.0')
ON CONFLICT (key) DO NOTHING;

-- Создаем представление для удобства
CREATE OR REPLACE VIEW daily_transactions AS
SELECT
    DATE(created_at) as transaction_date,
    type,
    category,
    COUNT(*) as transaction_count,
    SUM(amount) as total_amount
FROM transactions
GROUP BY DATE(created_at), type, category;

-- Логирование для отладки
DO $$
BEGIN
    RAISE NOTICE 'Database family_finances initialized successfully';
END $$;