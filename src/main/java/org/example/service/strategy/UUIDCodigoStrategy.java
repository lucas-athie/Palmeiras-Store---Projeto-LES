package org.example.service.strategy;

import java.util.UUID;

public class UUIDCodigoStrategy implements CodigoStrategy {

    @Override
    public String gerarCodigo() {
        return UUID.randomUUID().toString();
    }
}