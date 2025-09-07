package org.example.model.entities;

public enum Genero {
    MASCULINO("Masculino"),
    FEMININO("Feminino"),
    OUTROS("Outros");

    private final String label;

    Genero(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static Genero from(String dbValue) {
        if (dbValue == null) {
            throw new IllegalArgumentException("Gênero não pode ser nulo");
        }
        for (Genero g : values()) {
            if (g.label.equalsIgnoreCase(dbValue.trim())) {
                return g;
            }
        }
        try {
            return valueOf(dbValue.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Gênero inválido: " + dbValue);
        }
    }
}