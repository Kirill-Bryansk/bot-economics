-- Удалить все представления и таблицы с каскадом
DROP VIEW IF EXISTS daily_transactions CASCADE;
DROP TABLE IF EXISTS transaction_entries CASCADE;
DROP TABLE IF EXISTS children CASCADE;
DROP TABLE IF EXISTS subcategories CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS transactions CASCADE;

-- Основная таблица транзакций
CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    telegram_user_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL CHECK (amount > 0),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица основных категорий
CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Таблица подкатегорий
CREATE TABLE subcategories (
    id SERIAL PRIMARY KEY,
    category_id INTEGER NOT NULL REFERENCES categories(id),
    name VARCHAR(50) NOT NULL,
    UNIQUE(category_id, name)
);

-- Таблица детей
CREATE TABLE children (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Таблица связей транзакций с категориями/подкатегориями/детьми
CREATE TABLE transaction_entries (
    transaction_id BIGINT NOT NULL REFERENCES transactions(id),
    category_id INTEGER REFERENCES categories(id),
    subcategory_id INTEGER REFERENCES subcategories(id),
    child_id INTEGER REFERENCES children(id),
    PRIMARY KEY (transaction_id)
);

-- Индексы для оптимизации запросов
CREATE INDEX idx_transactions_user_date ON transactions(telegram_user_id, created_at DESC);
CREATE INDEX idx_transaction_entries_category ON transaction_entries(category_id);
CREATE INDEX idx_transaction_entries_child ON transaction_entries(child_id);
CREATE INDEX idx_created_at ON transactions(created_at);