package org.example.model.dao;

import org.example.model.entities.Cartao;

import java.sql.SQLException;
import java.util.List;

public interface CartaoDao {

    void insert (Cartao cartao, int idClient);
    void deleteByid (int id);
    List<Cartao> findCartaoByClienteId(int idCliente) throws SQLException;

}
