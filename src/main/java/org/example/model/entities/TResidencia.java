package org.example.model.entities;

import com.google.gson.annotations.SerializedName;
import java.text.Normalizer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public enum TResidencia {

    @SerializedName("Casa")
    CASA("Casa"),

    @SerializedName("Apartamento")
    APARTAMENTO("Apartamento"),

    @SerializedName("Condomínio")
    CONDOMINIO("Condomínio");

    private final String label;
    private final String key;
    private static final Map<String, TResidencia> LOOKUP;

    static {
        Map<String, TResidencia> map = new HashMap<>();
        for (TResidencia t : values()) {
            map.put(t.key, t);
        }
        LOOKUP = Collections.unmodifiableMap(map);
    }

    TResidencia(String label) {
        this.label = label;
        this.key   = normalize(label);
    }

    public static TResidencia from(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo de residência não pode ser nulo ou vazio");
        }
        String norm = normalize(raw);
        TResidencia t = LOOKUP.get(norm);
        if (t != null) {
            return t;
        }
        throw new IllegalArgumentException("Tipo de residência inválido: " + raw);
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