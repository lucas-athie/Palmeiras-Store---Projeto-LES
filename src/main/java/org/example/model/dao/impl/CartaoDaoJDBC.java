package org.example.model.dao.impl;

import org.example.model.dao.CartaoDao;
import org.example.model.entities.Cartao;

import java.sql.Connection;
import java.util.List;

public class CartaoDaoJDBC implements CartaoDao {

    public CartaoDaoJDBC(Connection conn) {
    }

    @Override
    public void insert(Cartao cartao) {

    }

    @Override
    public void deleteByid(int id) {

    }

    @Override
    public List<Cartao> findCartaoByClienteId(int idCliente) {
        return List.of();
    }
}
