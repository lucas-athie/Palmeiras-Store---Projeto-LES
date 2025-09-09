package org.example.model.dao;

import org.example.model.entities.Endereco;

import java.sql.SQLException;
import java.util.List;


public interface EnderecoDao {

    void insert(int clienteId, Endereco endereco) throws SQLException;

    void deleteById(int clienteId, int enderecoId) throws SQLException;

    List<Endereco> findByClienteId(int clienteId) throws SQLException;
}