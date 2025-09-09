package org.example.model.entities;

import com.google.gson.annotations.SerializedName;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum TLogradouro {
    @SerializedName("Rua")
    RUA("Rua"),

    @SerializedName("Avenida")
    AVENIDA("Avenida"),

    @SerializedName("Praça")
    PRACA("Praça"),

    @SerializedName("Rodovia")
    RODOVIA("Rodovia"),

    @SerializedName("Alameda")
    ALAMEDA("Alameda"),

    @SerializedName("Viela")
    VIELA("Viela"),

    @SerializedName("Travessa")
    TRAVESSA("Travessa"),

    @SerializedName("Estrada")
    ESTRADA("Estrada"),

    @SerializedName("Parque")
    PARQUE("Parque");

    private final String label;
    private final String key;
    private static final Map<String, TLogradouro> LOOKUP;

    static {
        var map = new HashMap<String, TLogradouro>();
        for (TLogradouro t : values()) {

            map.put(t.name(), t);

            map.put(t.key, t);
        }
        LOOKUP = Collections.unmodifiableMap(map);
    }

    TLogradouro(String label) {
        this.label = label;
        this.key = normalize(label);
    }

    public static TLogradouro from(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo de logradouro não pode ser nulo ou vazio");
        }
        String norm = normalize(raw);
        TLogradouro t = LOOKUP.get(norm);
        if (t != null) {
            return t;
        }
        throw new IllegalArgumentException("Tipo de logradouro inválido: " + raw);
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

    private static String normalize(String s) {
        return s.trim().toUpperCase(Locale.ROOT);
    }
}