package org.example.service.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValidationResult {
    private final List<String> errors = new ArrayList<>();

    public void addError(String message) {
        errors.add(message);
    }

    public boolean isValid() {
        return errors.isEmpty();
    }

    public void merge(ValidationResult other) {
        errors.addAll(other.getErrors());
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }


    public void assertValid() {
        if (!isValid()) {
            throw new IllegalArgumentException("Erros de validação: " +
                    String.join("; ", errors));
        }
    }
}