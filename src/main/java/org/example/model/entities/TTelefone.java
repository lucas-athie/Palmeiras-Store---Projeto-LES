package org.example.model.entities;

public enum TTelefone {
    FIXO("Fixo"),
    CELULAR("Celular");

    private final String label;

    TTelefone(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static TTelefone from(String dbValue) {
        for (TTelefone t : values()) {
            if (t.label.equalsIgnoreCase(dbValue)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Tipo de telefone inválido: " + dbValue);
    }
}