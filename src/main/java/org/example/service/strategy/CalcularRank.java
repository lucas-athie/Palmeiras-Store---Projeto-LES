package org.example.service.strategy;

import org.example.model.entities.Cliente;
//import org.example.model.entities.Pedido;

import java.util.List;


public class CalcularRank implements RankStrategy {

    @Override
    public double calcular(Cliente cliente) {
        //List<Pedido> pedidos = cliente.getPedidos();
        //if (pedidos == null || pedidos.isEmpty()) {
            return 0.0;
        //}

        // Soma total gasto e calcula quantidade de pedidos
        //double totalGasto = pedidos.stream()
                //.mapToDouble(Pedido::getTotal)
               // .sum();
       // int totalCompras  = pedidos.size();

        // Score base: média de gasto × √(frequência)
        //double media     = totalGasto / totalCompras;
        //double baseScore = media * Math.sqrt(totalCompras);

        // adicionar cálculo de score baseado em detalhes de produtos dos pedidos

        //return baseScore;
    }
}