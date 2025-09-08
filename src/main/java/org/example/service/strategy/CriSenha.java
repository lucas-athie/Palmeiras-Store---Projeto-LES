package org.example.service.strategy;

import org.mindrot.jbcrypt.BCrypt;


public class CriSenha implements CriptoStrategy {
    private final int logRounds;

    public CriSenha(int logRounds) {
        if (logRounds < 4 || logRounds > 31) {
            throw new IllegalArgumentException("logRounds deve ficar entre 4 e 31");
        }
        this.logRounds = logRounds;
    }

    @Override
    public String encrypt(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(logRounds));
    }
}