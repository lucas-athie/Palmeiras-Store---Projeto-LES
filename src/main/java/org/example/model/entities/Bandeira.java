package org.example.model.entities;

public enum Bandeira {
    MASTERCARD("Mastercard"),
    VISA("Visa"),
    AMEX("Amex"),
    ELO("Elo"),
    HIPERCARD("Hipercard");

    private final String label;

    Bandeira(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static Bandeira from(String dbValue) {
        if (dbValue == null) {
            throw new IllegalArgumentException("Bandeira não pode ser nula");
        }
        String trimmed = dbValue.trim();
        for (Bandeira b : values()) {
            if (b.label.equalsIgnoreCase(trimmed)) {
                return b;
            }
        }
        try {
            return valueOf(trimmed.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Bandeira inválida: " + dbValue);
        }
    }
}