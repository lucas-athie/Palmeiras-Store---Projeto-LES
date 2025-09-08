package org.example.service;

import org.example.model.dao.ClienteDao;
import org.example.model.dao.DaoFactory;
import org.example.model.entities.Cliente;
import org.example.service.strategy.CalcularRank;
import org.example.service.strategy.RankStrategy;

import java.sql.Connection;
import java.util.List;

public class RankService {
    private final RankStrategy strategy = new CalcularRank();


    public double gerarRankPara(int clienteId) throws Exception {
        try (Connection conn = org.example.db.DB.getConnection()) {
            ClienteDao cDao = DaoFactory.createClienteDao(conn);
            Cliente cliente = cDao.findById(clienteId);


            return strategy.calcular(cliente);
        }
    }
}