package org.example.model.entities;

import com.google.gson.annotations.SerializedName;
import java.text.Normalizer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum TEndereco {

    @SerializedName("Cobrança")
    COBRANCA("Cobrança"),

    @SerializedName("Entrega")
    ENTREGA("Entrega");

    private final String label;
    private final String normalized;
    private static final Map<String, TEndereco> LOOKUP;

    static {
        Map<String, TEndereco> map = new HashMap<>();
        for (TEndereco t : values()) {
            map.put(t.normalized, t);
        }
        LOOKUP = Collections.unmodifiableMap(map);
    }

    TEndereco(String label) {
        this.label      = label;
        this.normalized = normalize(label);
    }

    public static TEndereco from(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo de endereço não pode ser nulo ou vazio");
        }
        String key = normalize(raw);
        TEndereco t = LOOKUP.get(key);
        if (t != null) {
            return t;
        }
        throw new IllegalArgumentException("Tipo de endereço inválido: " + raw);
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

    private static String normalize(String input) {
        var folded = Normalizer.normalize(input.trim(), Normalizer.Form.NFD);
        return folded.replaceAll("\\p{M}", "").toUpperCase(Locale.ROOT);
    }
}