package org.example.service.strategy;

public interface ValidationStrategy<T> {
    ValidationResult validate(T target);
}