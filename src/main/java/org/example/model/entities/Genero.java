package org.example.model.entities;

import com.google.gson.annotations.SerializedName;

public enum Genero {
    @SerializedName("Masculino")
    MASCULINO(1, "Masculino"),

    @SerializedName("Feminino")
    FEMININO(2, "Feminino"),

    @SerializedName("Outros")
    OUTROS(3, "Outros");

    private final int id;
    private final String label;

    Genero(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

    public static Genero from(String dbValue) {
        if (dbValue == null || dbValue.trim().isEmpty()) {
            throw new IllegalArgumentException("Gênero não pode ser nulo ou vazio");
        }
        String trimmed = dbValue.trim();
        // busca pelo label (case-insensitive)
        for (Genero g : values()) {
            if (g.label.equalsIgnoreCase(trimmed)) {
                return g;
            }
        }
        // tenta pelo nome da enum
        try {
            return valueOf(trimmed.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Gênero inválido: " + dbValue, e);
        }
    }

    public static Genero fromId(int id) {
        for (Genero g : values()) {
            if (g.id == id) {
                return g;
            }
        }
        throw new IllegalArgumentException("ID de Gênero inválido: " + id);
    }
}