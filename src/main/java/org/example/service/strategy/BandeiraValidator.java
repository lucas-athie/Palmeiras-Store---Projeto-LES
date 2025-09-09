package org.example.service.strategy;

import org.example.model.entities.Bandeira;


public class BandeiraValidator {


    public static boolean isValid(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return false;
        }
        try {
            Bandeira.from(raw);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}