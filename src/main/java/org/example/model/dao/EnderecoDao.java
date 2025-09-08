package org.example.model.dao;

import org.example.model.entities.Endereco;

import java.sql.SQLException;
import java.util.List;

public interface EnderecoDao {
    void inserir(Endereco endereco, int idCliente) throws SQLException;
    void deleteById(int id,int idCliente) throws SQLException;
    List<Endereco> findEnderecoByClientId(int id) throws SQLException;
}
