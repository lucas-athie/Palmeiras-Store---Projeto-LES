package org.example.model.dao;

import org.example.model.entities.Cartao;

import java.sql.SQLException;
import java.util.List;


public interface CartaoDao {


    void insert(int clienteId, Cartao cartao) throws SQLException;


    void deleteById(int clienteId, int cartaoId) throws SQLException;


    List<Cartao> findByClienteId(int clienteId) throws SQLException;
}