package org.example.model.entities;

public enum TResidencia {
    CASA("Casa"),
    APARTAMENTO("Apartamento"),
    CONDOMINIO("Condomínio");

    private final String label;

    TResidencia(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static TResidencia from(String dbValue) {
        if (dbValue == null) {
            throw new IllegalArgumentException("Tipo de residência não pode ser nulo");
        }
        String trimmed = dbValue.trim();
        for (TResidencia t : values()) {
            if (t.label.equalsIgnoreCase(trimmed)) {
                return t;
            }
        }
        try {
            return valueOf(trimmed.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de residência inválido: " + dbValue);
        }
    }
}