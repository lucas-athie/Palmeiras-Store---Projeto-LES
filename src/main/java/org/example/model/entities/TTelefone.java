package org.example.model.entities;

import com.google.gson.annotations.SerializedName;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.text.Normalizer;

public enum TTelefone {

    @SerializedName("Fixo")
    FIXO("Fixo"),

    @SerializedName("Celular")
    CELULAR("Celular");

    private final String label;
    private final String key;
    private static final Map<String, TTelefone> LOOKUP;

    static {
        Map<String, TTelefone> map = new HashMap<>();
        for (TTelefone t : values()) {
            map.put(t.key, t);
        }
        LOOKUP = Collections.unmodifiableMap(map);
    }

    TTelefone(String label) {
        this.label = label;
        this.key   = normalize(label);
    }

    public static TTelefone from(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo de telefone não pode ser nulo ou vazio");
        }
        String norm = normalize(raw);
        TTelefone t = LOOKUP.get(norm);
        if (t != null) {
            return t;
        }
        throw new IllegalArgumentException("Tipo de telefone inválido: " + raw);
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

    private static String normalize(String input) {
        String folded = Normalizer.normalize(input.trim(), Normalizer.Form.NFD);
        return folded.replaceAll("\\p{M}", "").toUpperCase(Locale.ROOT);
    }
}