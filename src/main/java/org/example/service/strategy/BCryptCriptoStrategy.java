package org.example.service.strategy;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptCriptoStrategy implements CriptoStrategy {

    private static final int DEFAULT_LOG_ROUNDS = 10;
    private final int logRounds;

    public BCryptCriptoStrategy() {
        this(DEFAULT_LOG_ROUNDS);
    }

    public BCryptCriptoStrategy(int logRounds) {
        if (logRounds < 4 || logRounds > 31) {
            throw new IllegalArgumentException("logRounds deve ficar entre 4 e 31");
        }
        this.logRounds = logRounds;
    }

    @Override
    public String encrypt(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser nula ou vazia");
        }
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(logRounds));
    }

    public boolean verify(String rawPassword, String hashed) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser nula ou vazia");
        }
        if (hashed == null || hashed.isEmpty()) {
            throw new IllegalArgumentException("Hash não pode ser nulo ou vazio");
        }
        return BCrypt.checkpw(rawPassword, hashed);
    }
}