package ru.boteconomics.bot.handlers.message;

import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    // Специализированный метод для валидации суммы
    public AmountValidationResult validateAmount(String amountInput) {
        if (amountInput == null || amountInput.trim().isEmpty()) {
            return AmountValidationResult.error("❌ Введите сумму");
        }

        try {
            double amount = Double.parseDouble(amountInput.replace(",", "."));

            if (amount <= 0) {
                return AmountValidationResult.error("❌ Сумма должна быть положительной!");
            }

            if (amount > 1_000_000) {
                return AmountValidationResult.error("❌ Сумма не может превышать 1,000,000₽");
            }

            return AmountValidationResult.valid(amount);

        } catch (NumberFormatException e) {
            return AmountValidationResult.error(
                    "❌ Неверный формат суммы!\nВведите число (например: 500 или 1000.50)"
            );
        }
    }

    // Специализированный record для валидации суммы (возвращает double)
    public record AmountValidationResult(
            boolean isValid,
            double value, // Конкретный тип double
            String errorMessage
    ) {
        public static AmountValidationResult valid(double value) {
            return new AmountValidationResult(true, value, null);
        }

        public static AmountValidationResult error(String message) {
            return new AmountValidationResult(false, 0.0, message);
        }
    }

    // Универсальный ValidationResult для других типов данных (если понадобится)
    public record GenericValidationResult<T>(
            boolean isValid,
            T value,
            String errorMessage
    ) {
        public static <T> GenericValidationResult<T> valid(T value) {
            return new GenericValidationResult<>(true, value, null);
        }

        public static <T> GenericValidationResult<T> error(String message) {
            return new GenericValidationResult<>(false, null, message);
        }
    }

    // Пример других методов валидации (если понадобятся):

    public GenericValidationResult<String> validateCategory(String categoryInput) {
        if (categoryInput == null || categoryInput.trim().isEmpty()) {
            return GenericValidationResult.error("❌ Введите категорию");
        }

        if (categoryInput.length() > 50) {
            return GenericValidationResult.error("❌ Название категории слишком длинное");
        }

        return GenericValidationResult.valid(categoryInput.trim());
    }

    public GenericValidationResult<String> validateDescription(String description) {
        if (description == null) {
            return GenericValidationResult.valid("");
        }

        if (description.length() > 500) {
            return GenericValidationResult.error("❌ Описание слишком длинное (макс. 500 символов)");
        }

        return GenericValidationResult.valid(description.trim());
    }
}