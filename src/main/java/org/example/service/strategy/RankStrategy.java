package org.example.service.strategy;

import org.example.model.entities.Cliente;

public interface RankStrategy {

    double calcular(Cliente cliente);
}