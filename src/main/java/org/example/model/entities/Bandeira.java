package org.example.model.entities;

import com.google.gson.annotations.SerializedName;

public enum Bandeira {
    @SerializedName("Mastercard")
    MASTERCARD("Mastercard"),

    @SerializedName("Visa")
    VISA("Visa"),

    @SerializedName("Amex")
    AMEX("Amex"),

    @SerializedName("Elo")
    ELO("Elo"),

    @SerializedName("Hipercard")
    HIPERCARD("Hipercard");

    private final String label;

    Bandeira(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

    public static Bandeira from(String dbValue) {
        if (dbValue == null || dbValue.trim().isEmpty()) {
            throw new IllegalArgumentException("Bandeira não pode ser nula ou vazia");
        }
        String trimmed = dbValue.trim();
        for (Bandeira b : values()) {
            if (b.label.equalsIgnoreCase(trimmed)
                    || b.name().equalsIgnoreCase(trimmed)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Bandeira inválida: " + dbValue);
    }
}