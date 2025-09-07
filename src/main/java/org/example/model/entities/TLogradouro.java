package org.example.model.entities;

public enum TLogradouro {
    RUA("Rua"),
    AVENIDA("Avenida"),
    PRACA("Praça"),
    RODOVIA("Rodovia"),
    ALAMEDA("Alameda"),
    VIELA("Viela"),
    TRAVESSA("Travessa"),
    ESTRADA("Estrada"),
    PARQUE("Parque");

    private final String label;

    TLogradouro(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static TLogradouro from(String dbValue) {
        if (dbValue == null) {
            throw new IllegalArgumentException("Tipo de logradouro não pode ser nulo");
        }
        String trimmed = dbValue.trim();
        for (TLogradouro t : values()) {
            if (t.label.equalsIgnoreCase(trimmed)) {
                return t;
            }
        }
        try {
            return valueOf(trimmed.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de logradouro inválido: " + dbValue);
        }
    }
}