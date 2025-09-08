package org.example.model.entities;

public enum TEndereco {
    COBRANCA("Cobrança"),
    ENTREGA("Entrega");

    private final String label;

    TEndereco(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

    public static TEndereco from(String dbValue) {
        if (dbValue == null || dbValue.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo de endereço não pode ser nulo ou vazio");
        }
        String txt = dbValue.trim();
        for (TEndereco t : values()) {
            if (t.name().equalsIgnoreCase(txt) || t.label.equalsIgnoreCase(txt)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Tipo de endereço inválido: " + dbValue);
    }
}